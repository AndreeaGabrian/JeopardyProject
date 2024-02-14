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
// import org.apache.commons.text.similarity.LevenShteinDistance;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;

import static com.example.data_mining_test_index.Utils.readQuestions;
import static com.example.data_mining_test_index.TokenizerLematizator.process_text;

public class DataMiningTestIndexApplication {

    public static void main(String[] args) {
        try {
            // Path to the Lucene index directory
            String indexDir = "JeopardyProject/index/stemming"; //stemming
            // String indexDir = "JeopardyProject/index/lemming"; //lemming
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


            // Creating two files for writing the saved results and for analysis
            PrintWriter results = new PrintWriter("JeopardyProject/results/results.txt", "UTF-8");
            PrintWriter performance = new PrintWriter("JeopardyProject/results/performance.txt", "UTF-8");


            //Read the questions
            List<QuestionDTO> questionsList = readQuestions("JeopardyProject/src/main/java/com/example/data_mining_test_index/questions.txt");
            // System.out.println("Number of questions: " + questionsList.size());
            results.println("Number of questions: " + questionsList.size() + "\n");
            int correct_1 = 0;
            int correct_3 = 0;
            int correct_5 = 0;
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
                results.println("Clue: " + question.textClue);
                results.println("Category: " + question.category + " | Correct answer: " + question.correctAnswer);
                results.println("Total Results: " + topDocs.totalHits);
                // System.out.println("Clue: " + question.textClue);
                // System.out.println("Category: " + question.category + " | Correct answer: " + question.correctAnswer);
                // System.out.println("Total Results: " + topDocs.totalHits);
                int counter = 0;

                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                    int docId = scoreDoc.doc;
                    question.answerTop10.add(docId); // add the doc id to the answers list (wiki pages)
                    Document doc = indexSearcher.doc(docId); // search for the doc with a given id
                    String result_answer = doc.get("title");
                    results.println("Document: " + result_answer + ", " + scoreDoc);
                    // System.out.println("Document: " + result_answer + ",  " + scoreDoc);
                    // if (counter == 0){
                    // if (result_answer.equals(question.correctAnswer)){
                    if (question.correctAnswer.contains("|")){
                        String[] parts = question.correctAnswer.split("\\|");
                        if (result_answer.equals(parts[0]) || result_answer.equals(parts[1])){
                            // System.out.println(result_answer.equalsIgnoreCase(question.correctAnswer));
                            if (counter < 1){
                                question.answerTop1 = docId;
                                correct_1++;
                                correct_3++;
                                correct_5++;
                            }
                            else if (counter < 3){
                                correct_3++;
                                correct_5++;
                            }
                            else if (counter < 5){
                                correct_5++;
                            }
                        }
                    }
                    else{
                        if (result_answer.equals(question.correctAnswer)){
                            // System.out.println(result_answer.equalsIgnoreCase(question.correctAnswer));
                            if (counter < 1){
                                question.answerTop1 = docId;
                                correct_1++;
                                correct_3++;
                                correct_5++;
                            }
                            else if (counter < 3){
                                correct_3++;
                                correct_5++;
                            }
                            else if (counter < 5){
                                correct_5++;
                            }
                        }
                    }
                    // }
                    counter++;

                }
                results.println(" ");
                // System.out.println(" ");
            }
            performance.println("Top 1 correct answers: " + correct_1);
            performance.println("Top 3 correct answers: " + correct_3);
            performance.println("Top 5 correct answers: " + correct_5);
            // System.out.println("Top 1 correct answers: " + correct);

            // Close the index reader
            performance.close();
            results.close();
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

