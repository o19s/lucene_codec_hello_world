package com.o19s.fdbcodec;

import junit.framework.Assert;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.codecs.PostingsFormat;
import org.apache.lucene.codecs.lucene40.Lucene40Codec;
import org.apache.lucene.codecs.lucene40.Lucene40PostingsFormat;
import org.apache.lucene.codecs.lucene41.Lucene41Codec;
import org.apache.lucene.codecs.lucene41.Lucene41PostingsFormat;
import org.apache.lucene.codecs.lucene42.Lucene42Codec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.foundationdb.Database;
import com.foundationdb.FDB;
import com.o19s.fdbcodec.Diff;
import com.o19s.fdbcodec.FdbPostingsFormat;

import java.io.File;
import java.io.IOException;

/**
 * This is the meat of the idea.  We define a new per-field Codec that
 * uses the RedisUpdatingPostingsFormat for the 'tag' field, and standard
 * Lucene40 for all other fields.  We then add a few documents, and make
 * sure that they're searchable.
 *
 * Then we update one of the documents, changing its tag from 'exampletag'
 * to 'updatedtag'.  We re-run the searches, and show that the number of
 * hits from each has changed accordingly.  Et voila, an updateable field!
 */
public class TestCodec {

    public static final String indexdir = "index/";
    private FDB fdb;
    private Database db;

    @Before
    public void setUp() throws IOException {
        Runtime.getRuntime().exec("rm -rf " + indexdir);
    	FDB fdb = FDB.selectAPIVersion(21);
		Database db = fdb.open().get();
//        JRedisClient redis = new JRedisClient();
//        redis.del("_0_exampletag", "_0_updatedtag");
    }
    
    @After
    public void teardown() {
	    fdb.dispose();
    }

    @Test
    public void testUpdateCodec() throws IOException {

        Directory dir = new SimpleFSDirectory(new File(indexdir));
        writeTestIndex(dir);

        Query q1 = new TermQuery(new Term("tag", "exampletag"));
        Query q2 = new TermQuery(new Term("tag", "updatedtag"));

        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(dir));

        Assert.assertEquals(10, searcher.search(q1, 1).totalHits);
        Assert.assertEquals(0, searcher.search(q2, 1).totalHits);

        Diff diff = new Diff();
        diff.addTerm(new Term("tag", "updatedtag"));
        diff.deleteTerm(new Term("tag", "exampletag"));

        Query updatequery = new TermQuery(new Term("id", "4"));

        //Updater.updateByQuery(dir, updatequery, diff);

        //Assert.assertEquals(1, searcher.search(q2, 1).totalHits);
        //Assert.assertEquals(9, searcher.search(q1, 1).totalHits);


    }

    static void writeTestIndex(Directory dir) throws IOException {

        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_42, new KeywordAnalyzer());
        iwc.setCodec(new MockCodec());

        IndexWriter writer = new IndexWriter(dir, iwc);

        FieldType ft = new FieldType();
        ft.setIndexed(true);
        for (int i = 0; i < 10; i++) {
            Document doc = new Document();
            doc.add(new Field("id", Integer.toString(i), ft));
            doc.add(new Field("tag", "exampletag", ft));
            writer.addDocument(doc);
        }

        writer.close();

    }

    public static class MockCodec extends Lucene42Codec {
        final PostingsFormat lucene40 = new Lucene41PostingsFormat();
        final PostingsFormat foundationdb = new FdbPostingsFormat("foundationdb");

        @Override
        public PostingsFormat getPostingsFormatForField(String field) {
            if (field.equals("tag")) {
                return foundationdb;
            } else {
                return lucene40;
            }
        }
    }

}
