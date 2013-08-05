package com.o19s.fdbutils;

import org.junit.Test;

import com.foundationdb.AsyncIterator;
import com.foundationdb.Database;
import com.foundationdb.FDB;
import com.foundationdb.FDBError;
import com.foundationdb.KeySelector;
import com.foundationdb.KeyValue;
import com.foundationdb.RangeQuery;
import com.foundationdb.ReadTransaction;
import com.foundationdb.Transaction;
import com.foundationdb.tuple.Tuple;

public class TestMultipleAsyncIterators {
	@Test
	public void test() {

		FDB fdb = FDB.selectAPIVersion(22);
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
		
		ReadTransaction tr = db.createTransaction().snapshot;
		Tuple cursor = new Tuple().add("a").add("a");
		Tuple end = new Tuple().add("z").add("z").add("z");
		
		try {
			
			byte[][] someTuples = {new Tuple().add("a").add("a").pack(), 
								   new Tuple().add("a").add("b").pack(),
								   new Tuple().add("a").add("c").pack()};
			
			for (int i= 0; i < 3; ++i) {	
				KeySelector begKs = KeySelector.firstGreaterOrEqual(someTuples[i]);
				// http://community.foundationdb.com/questions/539/using-keyselectors-in-rangequery-whats-best-way-to-select-the-end-inclusive
				KeySelector endKs = KeySelector.firstGreaterThan(end.pack());
				
				RangeQuery rq = tr.getRange(begKs, endKs);
				AsyncIterator<KeyValue> iter = rq.asyncIterator();	
				
				assert(iter.hasNext().get());
				byte[] key = iter.next().get().getKey();
				
				// Exhausting the future causes no NullPointerException 
				//while (iter.hasNext().get()) {
				//	iter.next().get();
				//}
			}	
		}
		catch (FDBError e) { // occasionally I get past_version while debugging, no biggy
			System.out.println(e.getMessage());
		}
		catch (NullPointerException e) {	// NullPointerException when AsyncIterator from prev range query not exhausted
			e.printStackTrace();
		}
		return;
	}
}
