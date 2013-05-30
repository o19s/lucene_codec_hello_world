package com.o19s.fdbcodec;

import static org.junit.Assert.*;

import java.io.IOException;
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

import com.foundationdb.Database;
import com.foundationdb.FDB;
import com.foundationdb.KeyValue;
import com.foundationdb.RangeQuery;
import com.foundationdb.Transaction;
import com.foundationdb.tuple.Range;
import com.foundationdb.tuple.Tuple;
import com.o19s.fdbcodec.FdbPostingsFormat;

public class TestPostingsWriting extends CodecTestCase {

	final private static String SEGMENT = "0";
	
	@Before
	public void blarg() {
		System.out.println("Here...");
	}

	@Test
	public void testMe() {
		SegmentWriteState sws = createSegmentWriteState();

		System.out.println("Running Test!!");
		
		FdbPostingsFormat postingFormatUnderTest = new FdbPostingsFormat("foundationdb");
		try {
			FieldsConsumer fc = postingFormatUnderTest.fieldsConsumer(sws);
			assert(fc != null);
			
			FieldInfo fi = createFieldInfo("myField");
			TermsConsumer tc = fc.addField(fi);
			
			BytesRef term = new BytesRef("dog".getBytes());
			
			PostingsConsumer postConsumer = tc.startTerm(term);
			postConsumer.startDoc(5, 0);
			postConsumer.finishDoc();
			
			tc.finishTerm(null, null);
			
			FDB fdb;
			Database db;
			
			fdb = FDB.selectAPIVersion(21);
			db = fdb.open().get();
			
			Transaction tr = db.createTransaction();
			
			Tuple segmentPrefix = new Tuple();
			Range prefix = segmentPrefix.range();
			
			RangeQuery rq = tr.getRange(prefix.begin, prefix.end);		
			
			List<KeyValue> list =  rq.asList().get();
			
			try {
				
				for (KeyValue k: list) {
					Tuple t1 = Tuple.fromBytes(k.getKey());
					System.out.println(t1.toString());
					BytesRef shouldBeDog = new BytesRef(t1.getBytes(1));
					System.out.println(shouldBeDog.utf8ToString());
				}
			}
			catch (Exception e ) {
				System.out.println(e.toString());
			}
			
//			Tuple rangeEnd = new Tuple(");
//			rangeEnd.add("1".getBytes());
//			
			
			
			
			
			fc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}

}
