package com.o19s.fdbcodec;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.codecs.PostingsConsumer;
import org.apache.lucene.codecs.TermsConsumer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.SegmentInfo;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Constants;
import org.apache.lucene.util.InfoStream;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;
import org.junit.Before;
import org.junit.Test;

import com.foundationdb.AsyncFuture;
import com.foundationdb.Database;
import com.foundationdb.FDB;
import com.foundationdb.KeyValue;
import com.foundationdb.RangeQuery;
import com.foundationdb.Transaction;
import com.foundationdb.tuple.Range;
import com.foundationdb.tuple.Tuple;
import com.o19s.fdbcodec.FdbPostingsFormat;
import com.o19s.fdbcodec.FdbCodecUtils.PostingsType;

public class TestPostingsWriting extends FdbCodecTestCase {

	final private static String SEGMENT = "0";
	
	@Before
	public void blarg() {
		System.out.println("Here...");
	}

	@Test
	public void testTermFreqWriting() {
		SegmentWriteState sws = createSegmentWriteState();

		System.out.println("Running Test!!");
		
		final String fieldToWrite = "myField";
		final String termToWrite = "dog";
		final int docIdToWrite = 5;
		final int termFreqToWrite = 1;
		
		
		FdbPostingsFormat postingFormatUnderTest = new FdbPostingsFormat("foundationdb");
		try {
			FieldsConsumer fc = postingFormatUnderTest.fieldsConsumer(sws);
			assert(fc != null);
			
			// Write out a document with field myField containing term dog
			FieldInfo fi = createFieldInfo(fieldToWrite);
			TermsConsumer tc = fc.addField(fi);
			
			BytesRef term = new BytesRef(termToWrite.getBytes());
			
			PostingsConsumer postConsumer = tc.startTerm(term);
			postConsumer.startDoc(docIdToWrite, termFreqToWrite);
			postConsumer.finishDoc();
			
			tc.finishTerm(null, null);
			
			Transaction tr = createFdbTransaction();
			
			// Get all terms for segment
			Tuple segmentPrefix = new Tuple();
			Range prefix = segmentPrefix.range();
			
			// This is a bit whitebox, make sure the data is there
			// Specifically here we're looking for term frequency
			Tuple key = FdbCodecUtils.createKey(SEGMENT, fieldToWrite, termToWrite, docIdToWrite, PostingsType.TERM_FREQ);
			int readTermFreq = byteArrAsInt(tr.get(key.pack()).get());
			
			assertEquals(readTermFreq, termFreqToWrite);
			
			fc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}

}
