package com.o19s.fdbcodec;

import java.io.IOException;

import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

// TODO Implement
// Iterating over docs, but only docs that are in liveDocs 
// (see the baseline Lucene Codec) --> needs to be done for 
// advance/nextDocs
public class FdbDocsAndPositionsEnum extends DocsAndPositionsEnum {

	private int[] docs;
	private int current;
	private Bits liveDocs;
	
	public FdbDocsAndPositionsEnum(Bits liveDocs, int[] docs) {
	}
	
	@Override
	public int endOffset() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BytesRef getPayload() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int nextPosition() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int startOffset() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int freq() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int advance(int targetDocId) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int docID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int nextDoc() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
