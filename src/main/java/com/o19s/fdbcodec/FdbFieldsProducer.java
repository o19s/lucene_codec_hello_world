package com.o19s.fdbcodec;

import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.codecs.FieldsProducer;
import org.apache.lucene.index.Terms;

public class FdbFieldsProducer extends FieldsProducer {

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator<String> iterator() {
		// Iterate the names of the fields
		// Does foundation db have any field collapsing?
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		// SimpleText returns -1, why!?
		return -1;
	}

	@Override
	public Terms terms(String arg0) throws IOException {
		// TODO Auto-generated method stub
		// Return something that encapsulates the terms 
		// and a TermsEnum
		return null;
	}

}
