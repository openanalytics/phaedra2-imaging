package eu.openanalytics.phaedra.imaging.jp2k.kdu;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import eu.openanalytics.phaedra.imaging.ImageData;
import eu.openanalytics.phaedra.imaging.jp2k.CompressionConfig;
import eu.openanalytics.phaedra.imaging.jp2k.ICodec;
import eu.openanalytics.phaedra.imaging.jp2k.ICodestreamSource;
import eu.openanalytics.phaedra.imaging.util.Rectangle;
import kdu_jni.KduException;
import kdu_jni.Kdu_codestream;
import kdu_jni.Kdu_compressed_source_nonnative;
import kdu_jni.Kdu_coords;
import kdu_jni.Kdu_dims;
import kdu_jni.Kdu_global;
import kdu_jni.Kdu_region_decompressor;
import kdu_jni.Kdu_simple_file_target;
import kdu_jni.Kdu_stripe_compressor;
import kdu_jni.Kdu_thread_env;
import kdu_jni.Siz_params;

public class KakaduCodec implements ICodec {

	static {
		KakaduLibLoader.load();
	}

	private Lock lock = new ReentrantLock();
	private int bgColor = 0x0;
	private Kdu_region_decompressor decompressor;
	private Kdu_thread_env threadingEnv;
	
	public KakaduCodec() {
		configureThreading();
	}
	
	@Override
	public void open() throws IOException {
		lock.lock();
		if (decompressor != null) return;
		try {
			decompressor = new Kdu_region_decompressor();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void close() {
		lock.lock();
		try {
			if (decompressor != null) {
				decompressor.Native_destroy();
				decompressor = null;
			}
			if (threadingEnv != null) {
				threadingEnv.Destroy();
				threadingEnv = null;
			}
		} catch (Exception e) {
			// Ignore
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	@Override
	public int[] getDimensions(ICodestreamSource source) throws IOException {
		lock.lock();
		try (var stream = initCodestream(source)) {
			int[] bounds = getFullImageBounds(stream);
			return new int[] { bounds[2], bounds[3] };
		} catch (Exception e) {
			throw new IOException("Failed to obtain image size", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public int getBitDepth(ICodestreamSource source) throws IOException {
		lock.lock();
		try (var stream = initCodestream(source)) {
			return stream.Get_bit_depth(0);
		} catch (Exception e) {
			throw new IOException("Failed to obtain image size", e);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public ImageData renderImage(int w, int h, ICodestreamSource source) throws IOException {
		ImageData data = null;
		
		lock.lock();
		try (var stream = initCodestream(source)) {
			int[] fullSize = getFullImageBounds(stream);
			// Calculate a scale factor that makes the image fit inside the requested dimensions.
			float scale = Math.min((float)w/fullSize[2], (float)h/fullSize[3]);
			int[] expand = calculateExpand(scale, 0, stream);
			int bpp = getBitDepth(stream);
			data = createImageData(w, h, bpp);
			
			//TODO There may be additional black pixels below and to the right of the rendered image, due to the scale rounding.
			doRender(fullSize, expand, data, 0, 0, stream);
		} catch (Exception e) {
			throw new IOException("Failed to render image", e);
		} finally {
			lock.unlock();
		}
		
		return data;
	}

	@Override
	public ImageData renderImage(float scale, ICodestreamSource source) throws IOException {
		ImageData data = null;
		
		lock.lock();
		try (var stream = initCodestream(source)) {
			int[] fullSize = getFullImageBounds(stream);
			int[] expand = calculateExpand(scale, 0, stream);
			int[] scaledSize = getScaledImageBounds(expand, stream);
			int bpp = getBitDepth(stream);
			data = createImageData(scaledSize[2], scaledSize[3], bpp);
			
			doRender(fullSize, expand, data, 0, 0, stream);
		} catch (Exception e) {
			throw new IOException("Failed to render image", e);
		} finally {
			lock.unlock();
		}
		
		return data;
	}

	@Override
	public ImageData renderImageRegion(float scale, Rectangle region, ICodestreamSource source) throws IOException {
		ImageData data = null;
		
		lock.lock();
		try (var stream = initCodestream(source)) {
			int[] fullBounds = getFullImageBounds(stream);
			int[] expand = calculateExpand(scale, 0, stream);
			int[] scaledBounds = getScaledRegionBounds(region.getCoordinates(), expand, stream);
			int bpp = getBitDepth(stream);

			data = createImageData(scaledBounds[2], scaledBounds[3], bpp);
			clipRegion(region, Rectangle.of(fullBounds));
			doRender(region.getCoordinates(), expand, data, 0, 0, stream);
		} catch (Exception e) {
			throw new IOException("Failed to render image", e);
		} finally {
			lock.unlock();
		}
		
		return data;
	}

	@Override
	public void compressCodeStream(CompressionConfig config, ImageData data, String outputFile) throws IOException {
		Kdu_simple_file_target target = null;
		Kdu_codestream kduCodestream = null;

		try {
			// Open input file, generate an appropriate siz_params block.
			Siz_params sizBlock = createSizBlock(data);
			double qstep = 1.0f / (Math.pow(2, data.depth));
			
			new File(outputFile).getParentFile().mkdirs();
			target = new Kdu_simple_file_target(outputFile);

			kduCodestream = new Kdu_codestream();
			kduCodestream.Create(sizBlock, target, null, 0, 0, threadingEnv);

			kduCodestream.Access_siz().Parse_string("Clevels=" + config.resolutionLevels);
			kduCodestream.Access_siz().Parse_string("Corder=" + config.order);
			kduCodestream.Access_siz().Parse_string("Creversible=" + (config.reversible ? "yes" : "no"));
			kduCodestream.Access_siz().Parse_string("ORGgen_plt=yes");
			kduCodestream.Access_siz().Parse_string("Cprecincts=" + config.precincts);
			kduCodestream.Access_siz().Parse_string("Cycc=no");
			kduCodestream.Access_siz().Access_cluster("QCD").Set("Qstep", 0, 0, qstep);
			kduCodestream.Access_siz().Finalize_all();

			//TODO Allow slope configuration
			if (!config.reversible) kduCodestream.Set_min_slope_threshold(39000);
			
			// Create compressor
			Kdu_stripe_compressor compressor = new Kdu_stripe_compressor();
			compressor.Start(kduCodestream,
					0,				// num_layer_specs
					null,			// layer_sizes
					null,			// layer_slopes
					0,				// min_slope_threshold
					false,			// no_prediction
					false,			// force_precise
					true,			// record_layer_info_in_comment
					0.0,			// size_tolerance
					0,				// num_components
					false,			// want_fastest
					threadingEnv);	// threading env

			// Split the image into stripes (avoids loading entire array in memory).
			int maxStripeSize = 50*1024*1024;
			StripeProvider stripeProvider = new StripeProvider(data, maxStripeSize);
			Stripe stripe = stripeProvider.getNextStripe();
			
			while (stripe != null) {
				int[] stripeHeights = {stripe.stripeHeight};
				int[] sampleOffsets = {0};
				int[] precisions = {data.depth};
				int[] samples = stripe.data;
				
				compressor.Push_stripe(
						samples, 		// sample data
						stripeHeights,	// sample heights
						sampleOffsets,	// sample offsets
						null,			// sample gaps
						null,			// row gaps
						precisions,		// precisions
						null,			// is signed
						0
				);
				
				stripe = stripeProvider.getNextStripe();
			}

			compressor.Finish();

		} catch (Exception e) {
			throw new IOException("JP2 compression failed: " + e.getMessage(), e);
		} finally {
			if (target != null) {
				try { target.Close(); } catch (Exception e) {}
			}
			if (kduCodestream != null) {
				try { kduCodestream.Destroy(); } catch (Exception e) {}
			}
		}
	}
	
	private void configureThreading() {
		if (threadingEnv != null) return;
		
		int threadCount = 1;
		String threadingSetting = System.getProperty("phaedra2.imaging.jp2k.codec.kakadu.threading", "auto");
		if (threadingSetting.equalsIgnoreCase("auto")) {
			threadCount = Runtime.getRuntime().availableProcessors();
		} else {
			threadCount = Math.max(1, Integer.parseInt(threadingSetting));
		}
		
		try {
			threadingEnv = new Kdu_thread_env();
			threadingEnv.Create();
			// Thread env starts with 1 thread already.
			for (int i=2; i<=threadCount; i++) threadingEnv.Add_thread();
		} catch (KduException e) {
			// Threading setup failed. Proceed single-threaded.
		}
	}
	
	private void doRender(int[] imageRegion, int[] expand, ImageData output, int outOffsetX, int outOffsetY, Kdu_codestream stream) throws KduException {
		
		Kdu_coords expand_num = new Kdu_coords(expand[0], expand[0]);
		Kdu_coords expand_denom = new Kdu_coords(expand[1], expand[1]);

		// Calculate the region to render using the requested image region.
		int[] scaledRegion = getScaledRegionBounds(imageRegion, expand, stream);
		Kdu_dims region = new Kdu_dims();
		region.Access_pos().Set_x(scaledRegion[0]);
		region.Access_pos().Set_y(scaledRegion[1]);
		region.Access_size().Set_x(scaledRegion[2]);
		region.Access_size().Set_y(scaledRegion[3]);
		
		decompressor.Start(stream, null, 0, expand[2], Integer.MAX_VALUE, region, expand_num, expand_denom, true, Kdu_global.KDU_WANT_CODESTREAM_COMPONENTS, false, threadingEnv);
		
		try {
			// Process incrementally by rendering sub-regions of the requested region.
			int region_buf_size = region.Access_size().Get_x() * 150;
			int[] region_buf = new int[region_buf_size];
			
			Kdu_dims new_region = new Kdu_dims();
			Kdu_dims incomplete_region = new Kdu_dims();
			incomplete_region.Assign(region);
			
			int precision = output.depth;
			// Special case for thumbnails of 1bit channels: with precision 1 they do not render properly.
			if (precision == 1 && (expand[0]/expand[1]) < 1.0) precision = 8;
			
			while (decompressor.Process(region_buf, new int[]{0}, 1, region.Access_pos(), 0, 100000, region_buf_size, incomplete_region, new_region, precision)) {
				Kdu_coords offset = new_region.Access_pos().Minus(region.Access_pos());
				Kdu_coords newRegionSize = new_region.Access_size();
				int currentOffsetX = outOffsetX+offset.Get_x();
				int currentOffsetY = outOffsetY+offset.Get_y();
				int currentWidth = newRegionSize.Get_x();
				int currentHeight = newRegionSize.Get_y();
				
				// Add pixels to the ImageData, line per line.
				for (int i=0; i<currentHeight; i++) {
					int bufferOffset = i*currentWidth;
					int outputOffset = currentOffsetX + (currentOffsetY+i)*output.width;
					System.arraycopy(region_buf, bufferOffset, output.pixels, outputOffset, currentWidth);
				}
			}
		} finally {
			decompressor.Finish();
		}
	}

	private int[] getFullImageBounds(Kdu_codestream stream) throws KduException {
		Kdu_dims fullSize = new Kdu_dims();
		stream.Get_dims(0, fullSize);
		int imgX = fullSize.Access_size().Get_x();
		int imgY = fullSize.Access_size().Get_y();
		return new int[] { 0, 0, imgX, imgY };
	}
	
	private int[] getScaledImageBounds(int[] expand, Kdu_codestream stream) throws KduException {
		Kdu_coords expand_num = new Kdu_coords(expand[0], expand[0]);
		Kdu_coords expand_denom = new Kdu_coords(expand[1], expand[1]);
		stream.Apply_input_restrictions(0, 0, expand[2], 0, null, Kdu_global.KDU_WANT_CODESTREAM_COMPONENTS);
		Kdu_dims scaledDims = decompressor.Get_rendered_image_dims(stream, null, 0, expand[2], expand_num, expand_denom, Kdu_global.KDU_WANT_CODESTREAM_COMPONENTS);
		stream.Apply_input_restrictions(0, 0, 0, 0, null, Kdu_global.KDU_WANT_CODESTREAM_COMPONENTS);
		return new int[] {
				scaledDims.Access_pos().Get_x(), scaledDims.Access_pos().Get_y(),
				scaledDims.Access_size().Get_x(), scaledDims.Access_size().Get_y()
		};
	}
	
	private int[] getScaledRegionBounds(int[] region, int[] expand, Kdu_codestream stream) throws KduException {
		Kdu_dims regionDims = new Kdu_dims();
		regionDims.Access_pos().Set_x(region[0]);
		regionDims.Access_pos().Set_y(region[1]);
		regionDims.Access_size().Set_x(region[2]);
		regionDims.Access_size().Set_y(region[3]);
		
		Kdu_coords subs = new Kdu_coords();
		stream.Apply_input_restrictions(0, 0, expand[2], 0, null, Kdu_global.KDU_WANT_CODESTREAM_COMPONENTS);
		stream.Get_subsampling(0, subs);
		stream.Apply_input_restrictions(0, 0, 0, 0, null, Kdu_global.KDU_WANT_CODESTREAM_COMPONENTS);
		
		Kdu_coords expand_num = new Kdu_coords(expand[0], expand[0]);
		Kdu_coords expand_denom = new Kdu_coords(expand[1], expand[1]);
		Kdu_dims scaledDims = decompressor.Find_render_dims(regionDims, subs, expand_num, expand_denom);
		return new int[] {
				scaledDims.Access_pos().Get_x(), scaledDims.Access_pos().Get_y(),
				scaledDims.Access_size().Get_x(), scaledDims.Access_size().Get_y()
		};
	}
	
	private int getBitDepth(Kdu_codestream stream) throws KduException {
		if (stream == null) return 0;
		return stream.Get_bit_depth(0);
	}
	
	private int[] calculateExpand(float scale, int additionalDiscardLevels, Kdu_codestream stream) throws KduException {
		
		// First, calculate the amount of discard levels needed.
		int discardLevels = 0;
		int maxDiscardLevels = stream.Get_min_dwt_levels();
		Kdu_coords subs = new Kdu_coords();
		stream.Apply_input_restrictions(0, 0, discardLevels, 0, null, Kdu_global.KDU_WANT_CODESTREAM_COMPONENTS);
		stream.Get_subsampling(0, subs);
		while (discardLevels < maxDiscardLevels && scale*subs.Get_x() < 0.6) {
			discardLevels++;
			stream.Apply_input_restrictions(0, 0, discardLevels, 0, null, Kdu_global.KDU_WANT_CODESTREAM_COMPONENTS);
			stream.Get_subsampling(0, subs);
		}
		stream.Apply_input_restrictions(0, 0, 0, 0, null, Kdu_global.KDU_WANT_CODESTREAM_COMPONENTS);
		
		additionalDiscardLevels = Math.min(additionalDiscardLevels, maxDiscardLevels - discardLevels);
		discardLevels += additionalDiscardLevels;
		
		// Adjust scale to take the discard levels into account.
		scale = scale * (float)Math.pow(2, discardLevels);
		
		// Calculate the rational scale value.
		long bits = Double.doubleToLongBits((double)scale);
		long sign = bits >>> 63;
		long exponent = ((bits >>> 52) ^ (sign << 11)) - 1023;
		long fraction = bits << 12; // bits are "reversed" but that's not a problem

		long a = 1L;
		long b = 1L;
		for (int i = 63; i >= 12; i--) {
		    a = a * 2 + ((fraction >>> i) & 1);
		    b *= 2;
		}
		if (exponent > 0) a *= 1 << exponent;
		else b *= 1 << -exponent;
		if (sign == 1) a *= -1;

		// Simplify the fraction
		BigInteger bigA = new BigInteger("" + a);
		BigInteger bigB = new BigInteger("" + b);
		long gcd = bigA.gcd(bigB).longValue();
		a = a / gcd;
		b = b / gcd;
		
		int[] expand = new int[3];
		expand[0] = (int)a;
		expand[1] = (int)b;
		expand[2] = discardLevels;
		
		return expand;
	}

	private void clipRegion(Rectangle region, Rectangle fullSize) {
		int maxX = fullSize.width-1;
		int maxY = fullSize.height-1;
		
		region.x = Math.max(0, region.x);
		region.y = Math.max(0, region.y);
		
		//Note: this may cause a small duplicate stripe on the edge.
		if (region.x + region.width > maxX) region.x = Math.max(0, maxX - region.width);
		if (region.y + region.height > maxY) region.y = Math.max(0, maxY - region.height);
		
		if (region.x == 0 && region.width > fullSize.width) region.width = fullSize.width;
		if (region.y == 0 && region.height > fullSize.height) region.height = fullSize.height;
	}
	
	private ImageData createImageData(int w, int h, int depth) {
		ImageData data = new ImageData();
		data.width = w;
		data.height = h;
		data.depth = depth;
		data.pixels = new int[w*h];
		Arrays.fill(data.pixels, bgColor);
		return data;
	}
	
	private Siz_params createSizBlock(ImageData data) throws KduException {

		int imageW = data.width;
		int imageH = data.height;
		int imageDepth = data.depth;
		
		// Prepare a SIZ block, including size and depth.
		Siz_params sizBlock = new Siz_params();
		sizBlock.Set(Kdu_global.Scomponents, 0, 0, 1);
		sizBlock.Set(Kdu_global.Sdims, 0, 0, imageH); // ! height first
		sizBlock.Set(Kdu_global.Sdims, 0, 1, imageW);
		sizBlock.Set(Kdu_global.Sprecision, 0, 0, imageDepth);
		sizBlock.Set(Kdu_global.Ssigned, 0, 0, false);
		sizBlock.Finalize();
		
		return sizBlock;
	}
	
	private AutoclosingCodestream initCodestream(ICodestreamSource source) throws KduException {
		AutoclosingCodestream stream = new AutoclosingCodestream();
		stream.Create(new SimpleCodestreamSource(source));
		return stream;
	}
	
	private static class AutoclosingCodestream extends Kdu_codestream implements AutoCloseable {
		@Override
		public void close() throws Exception {
			try {
				this.Destroy();
			} catch (KduException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private static class SimpleCodestreamSource extends Kdu_compressed_source_nonnative {

		private long pos;
		private ICodestreamSource wrappedSource;
		
		public SimpleCodestreamSource(ICodestreamSource wrappedSource) {
			this.pos = 0;
			this.wrappedSource = wrappedSource;
		}
		
		@Override
		public int Get_capabilities() throws KduException {
			return Kdu_global.KDU_SOURCE_CAP_SEQUENTIAL | Kdu_global.KDU_SOURCE_CAP_SEEKABLE;
		}
		
		@Override
		public boolean Seek(long offset) throws KduException {
			pos = offset;
			return true;
		}
		
		@Override
		public long Get_pos() throws KduException {
			return pos;
		}
		
		@Override
		public int Post_read(int num_bytes) throws KduException {
			try {
				byte[] data = wrappedSource.getBytes(pos, num_bytes);
				pos += data.length;
				Push_data(data, 0, data.length);
				return data.length;
			} catch (IOException e) {
				throw new KduException(e.getMessage());
			}
		}
	}
	
	private static class StripeProvider {
		
		private ImageData data;
		private int lineOffset;
		private int stripeHeight;
		
		public StripeProvider(ImageData data, int maxStripeSize) {
			this.data = data;
			
			this.lineOffset = 0;
			this.stripeHeight = data.height;
			
			// 4, because we use integers to store pixels in memory.
			int bytesPerPixel = 4; //data.depth / 8;
			
			int imageSize = data.width * data.height * bytesPerPixel;
			if (imageSize > maxStripeSize) {
				int lineSize = data.width * bytesPerPixel;
				// Split into multiple stripes.
				stripeHeight = maxStripeSize / lineSize;
			}
		}
		
		public Stripe getNextStripe() {
			if (lineOffset >= data.height) {
				// End of image reached.
				return null;
			}
			else if (lineOffset + stripeHeight > data.height) {
				// Remainder does not fill an entire stripe. Make a smaller stripe (the last stripe).
				int lastStripeHeight = data.height - lineOffset;
				return createStripe(lastStripeHeight);
			}
			else {
				// At least one entire stripe can be filled.
				return createStripe(stripeHeight);
			}
		}
		
		private Stripe createStripe(int height) {
			int stripeSize = data.width * height;
			
			Stripe stripe = new Stripe();
			stripe.data = new int[stripeSize];
			stripe.stripeHeight = height;
			
			System.arraycopy(data.pixels, (lineOffset * data.width), stripe.data, 0, stripeSize);
			for (int i=0; i<stripeSize; i++) stripe.data[i] -= Math.pow(2, data.depth - 1);
			
			// Increment the offset for the next stripe.
			lineOffset += height;
			return stripe;
		}
	}
	
	private static class Stripe {
		public int[] data;
		public int stripeHeight;
	}
}
