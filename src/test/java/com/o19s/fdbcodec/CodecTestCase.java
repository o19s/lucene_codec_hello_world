package com.o19s.fdbcodec;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Constants;
import org.apache.lucene.util.InfoStream;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;

public class CodecTestCase extends LuceneTestCase {
		
	FieldInfo[] info = new FieldInfo[0];
	final private String SEGMENT = "0";

   
    private SegmentInfo createSegmentInfo() {
    	Directory dir = newDirectory();
	    Codec codec = Codec.getDefault(); //TODO change to the codec under test...
	    int termIndexInterval = _TestUtil.nextInt(random(), 13, 27);
	    SegmentInfo si = new SegmentInfo(dir, Constants.LUCENE_MAIN_VERSION, SEGMENT, 10000, false, codec, null, null);
	    return si;
    }
    
    SegmentWriteState createSegmentWriteState() {
    	SegmentInfo si = createSegmentInfo();
	    FieldInfos fieldInfos = new FieldInfos(info);
	    int termIndexInterval = _TestUtil.nextInt(random(), 13, 27);


		SegmentWriteState sws = new SegmentWriteState(InfoStream.getDefault(), si.dir, 
				  si, fieldInfos, termIndexInterval,
				  null, newIOContext(random()));
		return sws;

    }
    
    FieldInfo createFieldInfo(String fieldName) {
    	FieldInfo fi = new FieldInfo(fieldName, true, 0, true, true, true, FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS, null, null, null);
		return fi;
    }  
       
}
