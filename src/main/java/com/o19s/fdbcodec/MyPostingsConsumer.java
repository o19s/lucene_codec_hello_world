package com.o19s.fdbcodec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.apache.lucene.codecs.PostingsConsumer;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRef;

import com.foundationdb.tuple.Tuple;

public class MyPostingsConsumer extends PostingsConsumer {

	private String segment;
	private String term;
	private IntsRef docs = new IntsRef(1024);
	private int doccount = 0;
	
	public MyPostingsConsumer(String segmentName) {
		segment = segmentName;
	}
	
	@Override
	public void addPosition(int arg0, BytesRef arg1, int arg2, int arg3)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishDoc() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDoc(int docID, int freq) throws IOException {
		docs.ints[doccount] = docID;
		if (++doccount > docs.length) {
			docs.grow(docs.length * 2);
		}
	}
	
	public PostingsConsumer setTerm(BytesRef text) {
		this.term = text.utf8ToString();
	    this.docs.length = 0;
	    this.doccount = 0;
	    return this;
	}
	
	public Tuple getKey() {
		// change to FDB tuple
		return new Tuple().add(segment).add(term.getBytes());
	}
	
	public byte[] getPostings() {
	    ByteBuffer bytes = ByteBuffer.allocate(doccount * 4);
	    IntBuffer ib = bytes.asIntBuffer();
	    ib.put(docs.ints, 0, doccount);
	    return bytes.array();
	}
}
