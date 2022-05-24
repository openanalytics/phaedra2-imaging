package eu.openanalytics.phaedra.imaging.render;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import eu.openanalytics.phaedra.imaging.CodecFactory;
import eu.openanalytics.phaedra.imaging.jp2k.ICodec;

/**
 * A pool of JPEG2000 codecs. Multiple codecs can be used in parallel, this
 * pool will take care of instantiating new ones and cleaning up inactive ones.
 */
public class CodecPool extends GenericObjectPool<ICodec> {

	private static final long EVICTION_DELAY = 300000L; // Codecs are eligible for eviction after 5 minutes. 
	private static final long EVICTION_RUN_INTERVAL = 900000L; // Check for eviction every 15 minutes.
	
	public CodecPool() {
		super(new PooledCodecFactory(), getConfig());
	}
	
	private static GenericObjectPoolConfig<ICodec> getConfig() {
		GenericObjectPoolConfig<ICodec> config = new GenericObjectPoolConfig<>();
		//TODO OpenJPEG crashes when used concurrently. Find out why.
		config.setMaxTotal(1);
		config.setMinEvictableIdleTimeMillis(EVICTION_DELAY);
		config.setTimeBetweenEvictionRunsMillis(EVICTION_RUN_INTERVAL);
		return config;
	}
	
	private static class PooledCodecFactory extends BasePooledObjectFactory<ICodec> {

		@Override
		public ICodec create() throws Exception {
			return CodecFactory.createCodec();
		}

		@Override
		public PooledObject<ICodec> wrap(ICodec codec) {
			return new DefaultPooledObject<ICodec>(codec);
		}
		
		@Override
		public void destroyObject(PooledObject<ICodec> codec) throws Exception {
			codec.getObject().close();
		}
	}
}