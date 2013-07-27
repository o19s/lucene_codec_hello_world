package com.o19s.fdbutils;

import java.util.Iterator;

import com.foundationdb.AsyncFuture;
import com.foundationdb.AsyncIterator;
import com.foundationdb.FDBError;
import com.foundationdb.KeySelector;
import com.foundationdb.KeyValue;
import com.foundationdb.RangeQuery;
import com.foundationdb.ReadTransaction;
import com.foundationdb.Transaction;
import com.foundationdb.tuple.ArrayUtil;
import com.foundationdb.tuple.Tuple;

public class FdbGroupedIterator implements Iterator<Tuple> {
	// Use a transaction to iterate form begin <= end returning
	// all the unique keys in the tuples from 0 ... tupleLevel
	//
	// So if 
	//   abcd.1234.xyz
	//   abcd.1234.zzz
	//   abcd.3333.blah
	//   abcd.3333.blah2
	//   abcd.4445
	//   abcd.4445.yay
	//   abcd.6666
	//
	// FdbGroupedIterator with begin = abcd.1234, end = abcd.5555 at level 1 
	// will iterate and return all the unique keys in that part of the tuple
	// IE, the iterator will return
	//   abcd.1234
	//   abcd.3333
	//   abcd.4445
	
	
	private ReadTransaction tr;
	private Tuple cursor;
	private KeySelector endKs;
	private boolean done = false;	
	
	private static Tuple getSubTuple(Tuple tup, int depth) {
		Iterator<Object> origIter = tup.iterator();
		
		Tuple subTuple = new Tuple();
		
		for (int i = 0; i < depth && origIter.hasNext(); i++) {
			subTuple = subTuple.addObject(tup.get(i));
		}
		return subTuple;
	}
	
	private static Tuple nextPossibleTuple(Tuple fdbKey) {
		assert (fdbKey.size() > 0);
		
		Tuple rVal = getSubTuple(fdbKey, fdbKey.size() - 1);
		int lastIdx = fdbKey.size() - 1;
		
		Object lastKey = fdbKey.get(lastIdx);
		if (lastKey.getClass() == "".getClass()) {
			String asStr = (String)(lastKey);
			StringBuilder newSb = new StringBuilder();
			char c = asStr.charAt(asStr.length() - 1);
			++c;
			if (asStr.length() > 1) {
				newSb.append(asStr.substring(0, asStr.length() - 2));
			}
			newSb.append(c);
		
			rVal = rVal.add(newSb.toString()); 
		}
		return rVal;
		
	}
	
	public static KeySelector nextTupleSelector(Tuple currTuple) { 
		Tuple nextPossibleKey = nextPossibleTuple(currTuple);
		System.out.println("Next Possible Tuple: " + ArrayUtil.printable(nextPossibleKey.pack()));
		return KeySelector.firstGreaterOrEqual(nextPossibleKey.pack());
	}
	
	private KeySelector nextTupleSelector() {
		return nextTupleSelector(cursor);
	}
	
	
	public FdbGroupedIterator(Tuple begin, Tuple end, int tupleDepth, ReadTransaction tr) {
		
		this.tr = tr;
		this.endKs = KeySelector.lastLessThan(end.pack());
		
		// Assumption is that something starting with "begin" exists in the database
		cursor = getSubTuple(begin, tupleDepth); 
		RangeQuery rq = tr.getRangeStartsWith(cursor.pack());
		if (!rq.iterator().hasNext()) {
			KeySelector ks = nextTupleSelector(cursor);
			cursor = getSubTuple(Tuple.fromBytes(tr.getKey(ks).get()), tupleDepth);
		}
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return cursor.compareTo(Tuple.fromBytes(endKs.getKey())) >= 0;
	}

	@Override
	public Tuple next() {
		// TODO Auto-generated method stub
		Tuple rKey = cursor;

		try {
		
	
			System.out.println("Current cursor:" + ArrayUtil.printable(cursor.pack()));
			
			KeySelector ks = nextTupleSelector(cursor);
			
			// Preget
			AsyncFuture<byte[]> result = tr.getKey(ks);
			byte[] foo = result.get();
			
			// Some debug code
			Tuple t = Tuple.fromBytes(foo);
			String s1 = t.getString(0);
			String s2 = t.getString(1);
					
			// Get via range
			RangeQuery getResult = tr.getRange(ks, endKs); 
			AsyncIterator<KeyValue> iter = getResult.asyncIterator();
			assert(iter.hasNext().get());
			
			AsyncFuture<KeyValue> keyValue = iter.next();
			
			byte[] key = keyValue.get().getKey();
			
			cursor = getSubTuple(Tuple.fromBytes(key), cursor.size());
		}
		catch (FDBError e) {
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		return rKey;
		
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException(); 
		
	}	
}
