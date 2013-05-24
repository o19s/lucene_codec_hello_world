package com.o19s.fdbcodec;

import com.foundationdb.tuple.Tuple;

public class FdbCodecUtils {
	/**
	 * Create a key into FDB for this term
	 * 
	 * @param segment
	 * @param term
	 * @return
	 */
	public static Tuple createKey(String segment, String term) {
		return new Tuple().add(segment).add(term.getBytes());
	}
}
