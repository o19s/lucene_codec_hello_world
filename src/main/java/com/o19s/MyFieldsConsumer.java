package com.o19s;

import java.io.IOException;

import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.codecs.TermsConsumer;
import org.apache.lucene.index.FieldInfo;

public class MyFieldsConsumer extends FieldsConsumer {

	@Override
	public TermsConsumer addField(FieldInfo arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

}
