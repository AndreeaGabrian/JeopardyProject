package com.example.data_mining_test_index;

import edu.stanford.nlp.simple.Sentence;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.List;

public class TokenizerLematizator {

    public static String process_text(String text) throws IOException {
        Analyzer analyzer = new CustomSpecialAnalyzer();
        StringBuilder tokens = new StringBuilder();
        try (TokenStream tokenStream = analyzer.tokenStream("fieldName", text)) {
            // Get the attributes of the token stream
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

            tokenStream.reset(); // Reset the token stream to the beginning

            // Iterate over tokens

            while (tokenStream.incrementToken()) {
                // Print token attributes
                tokens.append(charTermAttribute.toString()).append(" ");

            }
            ;
            tokenStream.end();
        }
        catch (NullPointerException ne){
            tokens.append("!");
        }
        analyzer.close();

        String string_tokens = tokens.toString();
        if (string_tokens.equals("!") ) {
            return "";
        }
        if (string_tokens.equals("") || string_tokens.equals("[]")) {
            return string_tokens;
        }

        Sentence sentence = null;
        try {
            sentence = new Sentence(string_tokens);
        } catch (Exception ignored) {
        }
        if (sentence == null) {
            return string_tokens;
        }
        List<String> processed_text = sentence.lemmas();

        return String.join(" ", processed_text);

    }

}
