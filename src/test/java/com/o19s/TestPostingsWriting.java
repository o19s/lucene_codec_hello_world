package com.o19s;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Constants;
import org.apache.lucene.util.InfoStream;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;
import org.junit.Test;

public class TestPostingsWriting extends CodecTestCase {

	final private static String SEGMENT = "0";

	@Test
	public void test() {
		SegmentWriteState sws = createSegmentWriteState();

		MyPostingsFormat postingFormatUnderTest = new MyPostingsFormat("foundationdb");
		try {
			FieldsConsumer fc = postingFormatUnderTest.fieldsConsumer(sws);
			assert(fc != null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
