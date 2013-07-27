package com.o19s.fdbcodec;

import java.util.Iterator;

import org.junit.Test;

import com.foundationdb.KeySelector;
import com.foundationdb.KeyValue;
import com.foundationdb.RangeQuery;
import com.foundationdb.Transaction;
import com.foundationdb.tuple.Tuple;

public class TestCollapseOnField extends FdbCodecTestCase {
	
	@Test
	public void testCollapsingOnField() {
		// Just a low-level test to see if we can collapse on 
		// an arbitrary level of the keys
		
		Transaction tr = createFdbTransaction();
		// Get the first thing in the segment
		
		RangeQuery allSegment =  tr.getRangeStartsWith("0".getBytes());
		
		Iterator<KeyValue> iter = allSegment.iterator();
		KeyValue v = iter.next();
		Tuple firstKey = Tuple.fromBytes(v.getKey());
		Tuple firstFieldKey = new Tuple()
			.add(firstKey.getBytes(0))
			.add(firstKey.getBytes(1));
		
		KeySelector fieldCollapsed = new KeySelector(firstFieldKey.pack(), 
													 false, // dont look for equality
													 0);
		
	}
	

}
