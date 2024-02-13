package com.example.data_mining_test_index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static com.example.data_mining_test_index.Utils.readQuestions;
import static com.example.data_mining_test_index.TokenizerLematizator.process_text;

public class DataMiningTestIndexApplication {

    public static void main(String[] args) {
        try {
            // Path to the Lucene index directory
            String indexDir = "C:/Users/gabri/Documents/FACULTATE/MASTER/AN 1 SEM 1/index/index"; //stemming
            //String indexDir = "src/main/resources/index"; //lemming
            // Open the index directory
            Directory directory = FSDirectory.open(Paths.get(indexDir));
            // Create an index reader
            IndexReader indexReader = DirectoryReader.open(directory);
            // Create an index searcher
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            // Instantiate the custom analyzer
            Analyzer analyzer = new CustomSpecialAnalyzer(); // CUSTOM - the analyzer defined by us when indexing (lowering, tokenizer, remove stop words, stemming)
            //Analyzer analyzer = new StandardAnalyzer();   // FROM LUCENE LIBRARY

            // Specify the query parser with the custom analyzer
            QueryParser queryParser1 = new QueryParser("content", analyzer);
            QueryParser queryParser2 = new QueryParser("category", analyzer);


            //Read the questions
            List<QuestionDTO> questionsList = readQuestions("src/main/java/com/example/data_mining_test_index/questions.txt");
            System.out.println("Number of questions: " + questionsList.size());
            int correct = 0;
            for (QuestionDTO question : questionsList){
                // Specify the search query
                //String queryStr = TokenizerLematizator.process_text(question.category) + " " + TokenizerLematizator.process_text(question.textClue);
                String queryStr = question.category + " " + question.textClue;
                Query query1 = queryParser1.parse(queryStr);
                Query query2 = queryParser2.parse(queryStr);

                BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
                booleanQueryBuilder.add(query1, BooleanClause.Occur.SHOULD);
                booleanQueryBuilder.add(query2, BooleanClause.Occur.SHOULD);

                // Build the final query
                BooleanQuery booleanQuery = booleanQueryBuilder.build();

                // Perform the search
                TopDocs topDocs = indexSearcher.search(booleanQuery, 10); // Search for the top 10 documents
                System.out.println("Clue: " + question.textClue);
                System.out.println("Category: " + question.category + " | Correct answer: " + question.correctAnswer);
                System.out.println("Total Results: " + topDocs.totalHits);
                int counter = 0;

                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                    int docId = scoreDoc.doc;
                    question.answerTop10.add(docId); // add the doc id to the answers list (wiki pages)
                    Document doc = indexSearcher.doc(docId); // search for the doc with a given id
                    String result_answer = doc.get("title");
                    System.out.println("Document: " + result_answer + ",  " + scoreDoc);
                    if (counter == 0){
                        question.answerTop1 = docId;
                        if (result_answer.equals(question.correctAnswer)){
                            correct++;
                        }
                    }
                    counter++;

                }
                System.out.println(" ");
            }
            System.out.println("Top 1 correct answers: " + correct);

            // Close the index reader
            indexReader.close();
        } catch (IOException | org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }
    }

}


//-----------------------------------------------RESULTS-----------------------------------------
//  Index stemming
//- Standard Analyzer + lematizare pe intrebare : 21 corecte
//- Standard Analyzer fara extra procesare pe intrebare: 11 corecte
//- Custom Analyzer = stemming pe intrebare : 26 corecte
//
//  Index lemmatization
//- Standard Analyzer + lematizare pe intrebare: 15 corecte
//- Standard Analyser fara extra procesare pe intrebare: 16 corecte

