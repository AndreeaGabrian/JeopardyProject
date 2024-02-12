package com.example.data_mining_test_index;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.IOException;
import java.nio.file.Paths;


@SpringBootApplication
public class DataMiningTestIndexApplication {

    public static void main(String[] args) {
        try {
            // Path to the Solr index directory
            String indexDir = "C:/Users/gabri/Documents/FACULTATE/MASTER/AN 1 SEM 1/index/index";
            // Open the index directory
            Directory directory = FSDirectory.open(Paths.get(indexDir));
            // Create an index reader
            IndexReader indexReader = DirectoryReader.open(directory);
            // Create an index searcher
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            // Specify the term to search for
            String searchTerm = "hall";
            // Create a query for the specified term
            Query query = new TermQuery(new Term("content", searchTerm));
            // Perform the search
            TopDocs topDocs = indexSearcher.search(query, 10); // Search for the top 10 documents
            // Print out the results
            System.out.println("Total Results: " + topDocs.totalHits);
            for (var scoreDoc : topDocs.scoreDocs) {
                var docId = scoreDoc.doc;
                var doc = indexSearcher.doc(docId);
                System.out.println("Document: " + doc.get("title"));
            }
            // Close the index reader
            indexReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //SpringApplication.run(ItiBitsyApplication.class, args);
    }

}