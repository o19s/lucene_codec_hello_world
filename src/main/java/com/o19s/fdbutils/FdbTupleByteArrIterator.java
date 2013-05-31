package com.o19s.fdbutils;

import java.util.Iterator;

import com.foundationdb.tuple.Tuple;

public class FdbTupleByteArrIterator implements Iterator<byte[]> {
	
	Tuple tup;
	int cursor;
	
	FdbTupleByteArrIterator(Tuple tup) {
		this.tup = tup;
		cursor = 0;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return cursor < tup.size();
	}

	@Override
	public byte[] next() {
		// TODO Auto-generated method stub

		return tup.getBytes(cursor++);
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
