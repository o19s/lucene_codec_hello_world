package com.o19s.fdbcodec;

import com.foundationdb.tuple.Tuple;

public class FdbCodecUtils {
	
	public enum PostingsType {
		TERM_FREQ(0), 
		START_OFFSET(1),
		END_OFFSET(2),
		PAYLOAD(3),
		POSITION(4);
		
		private final int value;
		
		PostingsType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return this.value;
		}
	}
	
	/**
	 * Create a key into FDB for this term
	 * 
	 * @param segment
	 * @param term
	 * @return
	 */
	public static Tuple createKey(String segment, String field, String term, int docId, PostingsType pstType ) {
		// A reeeally big tuple
		int pstAsInt = pstType.getValue();
		
		return new Tuple()
			.add(segment.getBytes())
			.add(field.getBytes())
			.add(term.getBytes())
			.add(docId)
			.add(pstAsInt);
				
	}
}
