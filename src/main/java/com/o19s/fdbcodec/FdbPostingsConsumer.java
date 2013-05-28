package com.o19s.fdbcodec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.apache.lucene.codecs.PostingsConsumer;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRef;

import com.foundationdb.Transaction;
import com.foundationdb.tuple.Tuple;

public class FdbPostingsConsumer extends PostingsConsumer {

	private String segment;
	private String term;
	private String fieldName;
	private int currDocId = -1;
	Transaction postingsTr;
	

	public FdbPostingsConsumer(String segmentName, String fieldName, Transaction allPostingsTr) {
		segment = segmentName;
		this.postingsTr = allPostingsTr;
		this.fieldName = fieldName;
	}
	
	@Override
	public void addPosition(int position, BytesRef payload, int startOffset, int endOffset)
			throws IOException {
		// We're gonna write the following to foundation DB
		// segment.field.term.docId.posn
		
		postingsTr.set(this.currKey(FdbCodecUtils.PostingsType.START_OFFSET).pack(), intToByteArray(startOffset));	
		postingsTr.set(this.currKey(FdbCodecUtils.PostingsType.END_OFFSET).pack(), intToByteArray(startOffset));
		postingsTr.set(this.currKey(FdbCodecUtils.PostingsType.POSITION).pack(), intToByteArray(position));
		postingsTr.set(this.currKey(FdbCodecUtils.PostingsType.PAYLOAD).pack(), payload.bytes);

	}

	@Override
	public void finishDoc() throws IOException {
		// TODO Auto-generated method stub
		postingsTr.commit(); // not really a well defined transaction that has transactional meaning
		this.currDocId = -1;

	}
	
	public Tuple currKey(FdbCodecUtils.PostingsType pstType) {
		return FdbCodecUtils.createKey(segment, fieldName, term, this.currDocId, pstType);
	}
	
	private static byte[] intToByteArray(int val) {
		ByteBuffer bytes = ByteBuffer.allocate(4);
	    bytes.putInt(val);
	    return bytes.array();
	}

	@Override
	public void startDoc(int docID, int freq) throws IOException {
		
		this.currDocId = docID;
	    
		// preallocate this for multiple uses?
		ByteBuffer bytes = ByteBuffer.allocate(4);
	    bytes.putInt(freq);
	    
	    assert(this.term != null);
	    
		// Store tf right away
		postingsTr.set(this.currKey(FdbCodecUtils.PostingsType.TERM_FREQ).pack(), bytes.array());	
	}
	
	public PostingsConsumer setTerm(BytesRef text) {
		this.term = text.utf8ToString();
	    return this;
	}
	
}
