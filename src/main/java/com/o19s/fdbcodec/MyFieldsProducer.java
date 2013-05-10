package com.o19s.fdbcodec;

import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.codecs.FieldsProducer;
import org.apache.lucene.index.Terms;

public class MyFieldsProducer extends FieldsProducer {

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Terms terms(String arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
