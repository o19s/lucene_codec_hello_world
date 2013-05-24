package com.o19s.fdbcodec;

import java.io.IOException;

import org.apache.lucene.codecs.FieldsConsumer;
import org.apache.lucene.codecs.TermsConsumer;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.SegmentWriteState;

import com.foundationdb.Database;
import com.foundationdb.FDB;

public class FdbFieldsConsumer extends FieldsConsumer {

	private FDB fdb;
	private Database db;
	private String segmentName;
	
	public FdbFieldsConsumer(SegmentWriteState state) {
		fdb = FDB.selectAPIVersion(21);
		db = fdb.open().get();
        segmentName = state.segmentInfo.name;
	}
	
	@Override
	public TermsConsumer addField(FieldInfo arg0) throws IOException {
		// TODO Auto-generated method stub
		return new FdbTermsConsumer(db, segmentName);
	}

	@Override
	public void close() throws IOException {
		fdb.dispose();
	}

}
