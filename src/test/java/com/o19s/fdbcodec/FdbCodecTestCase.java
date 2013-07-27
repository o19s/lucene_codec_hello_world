package com.o19s.fdbcodec;

import java.nio.ByteBuffer;

import com.foundationdb.Database;
import com.foundationdb.FDB;
import com.foundationdb.Transaction;

public class FdbCodecTestCase extends CodecTestCase {

	// handles to database for reading
	private FDB fdb;
	private Database db;
	
	public FdbCodecTestCase() {
		fdb = FDB.selectAPIVersion(21);
		db = fdb.open().get();
		
	}
	
	protected int byteArrAsInt(byte[] arr) {
		// This is a bit fragile as there's endianness issues
		// to consider
		// Need to formulate this as a question to David
		ByteBuffer wrapped = ByteBuffer.wrap(arr);
		return wrapped.getInt();
	}
	
	protected byte[] intAsbyteArr(int val) {
		ByteBuffer buff = ByteBuffer.allocate(4);
		buff.putInt(val);
		return buff.array();
	}
	
	protected Transaction createFdbTransaction() {	
		
		Transaction tr = db.createTransaction();
		return tr;
	}

}
