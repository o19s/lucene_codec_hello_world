package com.o19s.fdbcodec;

import java.io.IOException;
import java.util.Comparator;

import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

import com.foundationdb.Database;

public class FdbTerms extends Terms {

    private String segment;
    private Database db;
    
    public FdbTerms(String segment, Database db) {
    	this.db = db;
    	this.segment = segment;
    }
	
	@Override
	public Comparator<BytesRef> getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDocCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSumDocFreq() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSumTotalTermFreq() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasOffsets() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPayloads() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPositions() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TermsEnum iterator(TermsEnum arg0) throws IOException {
		// TODO Auto-generated method stub
		return  new FdbTermsEnum(db, segment);
	}

	@Override
	public long size() throws IOException {
		// TODO Auto-generated method stub
		return -1;
	}

}
