# Phaedra2 Imaging Library

# System properties

* phaedra2.imaging.jp2k.codec: select the jp2k codec to use: "openjpeg" or "kakadu". The default is "openjpeg".
* phaedra2.imaging.jp2k.codec.kakadu.threading: the nr of threads each kakadu codec should use. The default is "auto" which translates to the processor count.
* phaedra2.imaging.renderservice.codec.pool.size: the max nr of codecs that the ImageRenderService may keep open at any given time. The default is 8.
* phaedra2.imaging.writer.codec: the codec to use to write common image formats (JPG,PNG,TIF): "swt" or "awt". The default is "swt".
