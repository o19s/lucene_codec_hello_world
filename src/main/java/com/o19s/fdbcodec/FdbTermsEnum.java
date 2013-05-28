package com.o19s.fdbcodec;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

import com.foundationdb.AsyncIterator;
import com.foundationdb.Database;
import com.foundationdb.KeyValue;
import com.foundationdb.Transaction;
import com.foundationdb.tuple.Tuple;
import com.foundationdb.RangeQuery;

public class FdbTermsEnum extends TermsEnum {
	
	private Database db;
	private String segment;
	private AsyncIterator<KeyValue> results;
	private BytesRef lastSearchTerm;
	
	public FdbTermsEnum(Database db, String segment) {
		this.db = db;
		this.segment = segment;
	}

	@Override
	public Comparator<BytesRef> getComparator() {
        return BytesRef.getUTF8SortedAsUnicodeComparator();
	}

	@Override
	public BytesRef next() throws IOException {
		return new BytesRef(results.next().get().getValue());
	}

	@Override
	public int docFreq() throws IOException {
		// TODO Auto-generated method stub
		// Should this get stored elsewhere in the database?
		// there's no way to know this with an async future
		return 0;
	}

	@Override
	public DocsEnum docs(Bits liveDocs, DocsEnum reuse, int flags) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocsAndPositionsEnum docsAndPositions(Bits liveDocs,
			DocsAndPositionsEnum reuse, int flags) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long ord() throws IOException {
        throw new UnsupportedOperationException();
	}

	@Override
	public SeekStatus seekCeil(BytesRef text, boolean useCache) throws IOException {
		// Need to think on this -- how can we cache this on the client side -- FST?
//		Transaction tr = db.createTransaction();
//		byte[] key = FdbCodecUtils.createKey(segment, text.utf8ToString()).pack();
//		RangeQuery resultFuture = tr.getRangeStartsWith(key);
//		results = resultFuture.asyncIterator();
//		if (results.hasNext().get()) {
//			return SeekStatus.NOT_FOUND;
//		}
//		else {
//			lastSearchTerm = text.clone();
//			return SeekStatus.FOUND;
//		}
		return SeekStatus.NOT_FOUND;
		

	}

	@Override
	public void seekExact(long arg0) throws IOException {
		// we should be able to do this
        throw new UnsupportedOperationException();		
	}

	@Override
	public BytesRef term() throws IOException {
		return lastSearchTerm;
	}

	@Override
	public long totalTermFreq() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
