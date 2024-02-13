package com.example.data_mining_test_index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Indexer {

    public static void main(String[] args) {
        String indexPath = "src/main/resources/index";
        String jsonDirPath = "src/main/resources/pages_final";

        try {
            Directory directory = FSDirectory.open(Paths.get(indexPath));
            //Analyzer analyzer = new CustomSpecialAnalyzer();
            Analyzer analyzer = new StandardAnalyzer();

            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(directory, config);

            List<File> jsonFiles = listJsonFiles(jsonDirPath);
            int count = 0;
            System.out.println("Indexed files: ");
            for (File file : jsonFiles) {
                indexFile(writer, file);
                count += 1;
                System.out.println(count);
            }

            writer.close();
            directory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<File> listJsonFiles(String directoryPath) {
        List<File> jsonFiles = new ArrayList<>();
        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    jsonFiles.add(file);
                }
            }
        }
        return jsonFiles;
    }

    private static void indexFile(IndexWriter writer, File jsonFile) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(jsonFile));

            for (Object obj : jsonArray) {
                JSONObject jsonObj = (JSONObject) obj;

                String title = (String) jsonObj.get("title");
                String content = TokenizerLematizator.process_text((String) jsonObj.get("content"));
                String categories = TokenizerLematizator.process_text((String) jsonObj.get("category"));

                Document doc = new Document();
                doc.add(new TextField("title", title, Field.Store.YES));
                doc.add(new TextField("category", categories, Field.Store.YES));
                doc.add(new TextField("content", content, Field.Store.YES));

                writer.addDocument(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
