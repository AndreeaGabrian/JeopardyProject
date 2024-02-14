# JeopardyProject
This is a question answering project based on the Jeopardy game. The task is to
implement a system which receive a clue (question) with a category and should
return the correct answer. For this we implemented an information-retrieval system
which:
- read a database with wikipedia pages and parse the content to multiple json files
- use a state-of-the-art indexing tool to index wikipedia info
- performing information retrieval and return the top 10 best matches for the queries
- first evaluation and error analysis
- improvement
- second evaluation and error analysis

### How to run the project
The final code is on **main** branch and is a java application. We worked with **java jdk-17**, **Maven 4.0.0**, and **Lucene 8.11**
The java project was configured with Maven and all the dependencies are in **pom.xml** file. So, you need to run install for
this file according to your ide (for example, in IntelliJ Idea in the maven panel you have buttons for clear and install).

### Files description
- *DataMiningTestIndexApplication.java* : from here you run the project
- *Indexer.java*: from here you run the lucene index (i.e create an index with lucene)
- *WikiPageDTO.java*, *QuestionDTO.java*: entity classes for storing the content of wiki pages and questions
- *Utils.java*: remove special characters and read the questions from the questions txt file  
- *Custom Analyzer*: a custom defined analyzer for query parsing to match the way solr index was made. 

### Resources
- *pages_final*: a folder with all json files resulted after reading the wikipedia dataset
- index_stemming: the index with solr
- index_lemmatization: the index with lucene

### Documentation

### Presentation

### Video






