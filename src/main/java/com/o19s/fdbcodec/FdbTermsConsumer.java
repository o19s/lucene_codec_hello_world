package com.o19s.fdbcodec;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Comparator;

import org.apache.lucene.codecs.PostingsConsumer;
import org.apache.lucene.codecs.TermStats;
import org.apache.lucene.codecs.TermsConsumer;
import org.apache.lucene.util.BytesRef;

import com.foundationdb.Database;
import com.foundationdb.FDB;
import com.foundationdb.Transaction;


//FDB fdb = FDB.selectAPIVersion(21);
//Database db = fdb.open().get();
//
//Charset c = Charset.forName("UTF-8");
//
//Transaction tr = db.createTransaction();
//tr.set("Hello".getBytes(c), "World".getBytes(c));
//tr.commit().get();
//
//tr = db.createTransaction();
//byte[] result = tr.get("Hello".getBytes(c)).get();
//System.out.println("Hello" + new String(result, c));
//
//System.out.println( "Hello World!" );
//
//fdb.dispose();
//}


public class FdbTermsConsumer extends TermsConsumer {
	
	private Database db;
	private FdbPostingsConsumer postingsConsumer;
	
	public FdbTermsConsumer(Database db, String segmentName) {
		this.db = db;
		postingsConsumer = new FdbPostingsConsumer(segmentName);
	}

	@Override
	public void finish(long arg0, long arg1, int arg2) throws IOException {

	}

	@Override
	public void finishTerm(BytesRef arg0, TermStats arg1) throws IOException {
		Transaction tr = db.createTransaction();
		tr.set(postingsConsumer.getKey().pack(), postingsConsumer.getPostings());
		tr.commit().get();
	}

	@Override
	public Comparator<BytesRef> getComparator() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PostingsConsumer startTerm(BytesRef term) throws IOException {
		// TODO Auto-generated method stub
		return postingsConsumer.setTerm(term);
	}
}
