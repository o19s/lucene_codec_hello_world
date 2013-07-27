package com.o19s.fdbutils;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.foundationdb.AsyncIterator;
import com.foundationdb.Database;
import com.foundationdb.FDB;
import com.foundationdb.FDBError;
import com.foundationdb.KeySelector;
import com.foundationdb.KeyValue;
import com.foundationdb.RangeQuery;
import com.foundationdb.Transaction;
import com.foundationdb.tuple.ArrayUtil;
import com.foundationdb.tuple.Tuple;

public class TestGroupedIterator {
	
	@Test
	public void test() {

		FDB fdb = FDB.selectAPIVersion(21);
		Database db = fdb.open().get();
		
		Transaction writeSomeStuff = db.createTransaction();
		
		Tuple nextKey;
		
		// Add some a.as
		nextKey = new Tuple().add("a").add("a").add("b");
		writeSomeStuff.set(nextKey.pack(), "".getBytes());
		nextKey = new Tuple().add("a").add("a").add("c");
		writeSomeStuff.set(nextKey.pack(), "".getBytes());
		nextKey = new Tuple().add("a").add("a").add("d");
		writeSomeStuff.set(nextKey.pack(), "".getBytes());
		nextKey = new Tuple().add("a").add("a").add("e");
		writeSomeStuff.set(nextKey.pack(), "".getBytes());		
		nextKey = new Tuple().add("a").add("a").add("e");
		writeSomeStuff.set(nextKey.pack(), "".getBytes());
		
		// ADd some a.bs
		nextKey = new Tuple().add("a").add("b").add("b");
		writeSomeStuff.set(nextKey.pack(), "".getBytes());
		nextKey = new Tuple().add("a").add("b").add("c");
		writeSomeStuff.set(nextKey.pack(), "".getBytes());
		
		
		// ADd some a.cs
		nextKey = new Tuple().add("a").add("c").add("b");
		writeSomeStuff.set(nextKey.pack(), "".getBytes());
		nextKey = new Tuple().add("a").add("c").add("c");
		writeSomeStuff.set(nextKey.pack(), "".getBytes());

		writeSomeStuff.commit().get();
		
		Transaction tr = db.createTransaction();
		Tuple begin = new Tuple().add("a").add("a");
		Tuple end = new Tuple().add("z");
		
		try {
		
			KeySelector ks = FdbGroupedIterator.nextTupleSelector(begin);
			KeySelector endKs = KeySelector.lastLessThan(end.pack());
			byte[] arr = tr.getKey(ks).get();
			
			RangeQuery rq = tr.getRange(ks, endKs);
			AsyncIterator<KeyValue> iter = rq.asyncIterator();
			assert(iter.hasNext().get());
			
			byte[] key = iter.next().get().getKey();
			System.out.println("Got " + ArrayUtil.printable(key));

		}
		catch (FDBError e) {
			System.out.println(e.getMessage());
		}
		
		
		
		Iterator<Tuple> collapsedIter = new FdbGroupedIterator(begin, end, 2, db.createTransaction().snapshot);
		
		Tuple shouldBeAA = collapsedIter.next();
		assert(shouldBeAA.compareTo(new Tuple().add("a").add("a")) == 0);
		Tuple shouldBeAB = collapsedIter.next();
		assert(shouldBeAA.compareTo(new Tuple().add("a").add("b")) == 0);
		Tuple shouldBeAC = collapsedIter.next();
		assert(shouldBeAA.compareTo(new Tuple().add("a").add("c")) == 0);		
				
		
		// write some test data out
		
	}

}
