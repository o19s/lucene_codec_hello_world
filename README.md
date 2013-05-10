A starting point for a Lucene Codec. Points of interest


## Publish your Codec correctly

  Notice the services/org.apache.lucene.codecs.Codec
  this uses the SPI (service provider) pattern in Java. Lucene searches
  the class path for META-INF/services/<interfaceName> for classes that
  satisfy that interface. 

  The current contents of services/org.apache.lucene.Codec contains the codec
  we are publishing here:

  - com.o19s.MyCodec

  Maven publishes this to target/classes/META-INF/services

## Run Lucene Unit Tests against your Codec in Eclipse 

1. Have solr setup in Eclipse (also contains Lucene's source)
2. Create a new JUnit debug configuration
3. Set "Run all tests in the selected project, package, source or folder" to:

    `lucene/core/src/test

4. Under the "arguments" tab, for vm arguments you should have

    `-ea
     -Dtests.codec=MyCodec`

replace "MyCodec" with your codec

5. Under the "classpath" tab make sure the following projects are in the class path

    `solr
     codecPlay`

Should be fairly trivial to acheive the same effect at the command line by simply running the Lucene unit tests normally, adding the new codec to the class path and setting -Dtests.codec=MyCodec
