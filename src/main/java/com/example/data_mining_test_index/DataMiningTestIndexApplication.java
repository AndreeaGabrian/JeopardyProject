package com.example.data_mining_test_index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static com.example.data_mining_test_index.Utils.readQuestions;

public class DataMiningTestIndexApplication {

    public static void main(String[] args) {
        try {
            // Path to the Lucene index directory
            //String indexDir = "C:/Users/gabri/Documents/FACULTATE/MASTER/AN 1 SEM 1/index/index";
            String indexDir = "src/main/resources/index";
            // Open the index directory
            Directory directory = FSDirectory.open(Paths.get(indexDir));
            // Create an index reader
            IndexReader indexReader = DirectoryReader.open(directory);
            // Create an index searcher
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            // Instantiate the custom analyzer
            Analyzer analyzer = new CustomSpecialAnalyzer(); // the analyzer defined by us (lowering, tokenizer, remove stop words, stemming)

            // Specify the query parser with the custom analyzer
            QueryParser queryParser = new QueryParser("content", analyzer);
            
            //Read the questions
            List<QuestionDTO> questionsList = readQuestions("src/main/java/com/example/data_mining_test_index/questions.txt");
            System.out.println("Number of questions: " + questionsList.size());
            for (QuestionDTO question : questionsList){
                // Specify the search query
                String queryStr = question.category + " " + question.textClue;
                Query query = queryParser.parse(queryStr);
                // Perform the search
                TopDocs topDocs = indexSearcher.search(query, 10); // Search for the top 10 documents
                System.out.println("Clue: " + question.textClue);
                System.out.println("Category: " + question.category + " | Correct answer: " + question.correctAnswer);
                System.out.println("Total Results: " + topDocs.totalHits);
                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                    int docId = scoreDoc.doc;
                    question.answerTop10.add(docId); // add the doc id to the answers list (wiki pages)
                    Document doc = indexSearcher.doc(docId); // search for the doc with a given id
                    System.out.println("Document: " + doc.get("title") + ",  " + scoreDoc);
                }
                System.out.println(" ");
            }

            // Close the index reader
            indexReader.close();
        } catch (IOException | org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }
    }
}


