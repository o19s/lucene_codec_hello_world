package com.o19s;

import java.io.IOException;

import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.codecs.FieldsProducer;
import org.apache.lucene.codecs.PostingsFormat;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;

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
