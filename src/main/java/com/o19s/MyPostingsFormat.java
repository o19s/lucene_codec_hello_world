package com.o19s;

import java.io.IOException;
import java.util.Comparator;

import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.codecs.FieldsProducer;
import org.apache.lucene.codecs.PostingsConsumer;
import org.apache.lucene.codecs.PostingsFormat;
import org.apache.lucene.codecs.TermStats;
import org.apache.lucene.codecs.TermsConsumer;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.lucene.util.BytesRef;


public class MyPostingsFormat extends PostingsFormat {

	protected MyPostingsFormat(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public FieldsConsumer fieldsConsumer(SegmentWriteState arg0)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FieldsProducer fieldsProducer(SegmentReadState arg0)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
