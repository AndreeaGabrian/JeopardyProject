# JeopardyProject
For creating an index of all wikipedia pages using SOLR we had to:
	1. Create a core
	2. Import data into the core (this is the moment where indexing happens)
After the above steps we are given an "index" folder where all the data is stored.

## 1. Creating a core
=======
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
The code for the creation of the pages recieved as an input in the index creation is on the **process_wikipedia_pages** branch and is a python file. We worked with **python3.8**. In order for the program to work the path to the wikipedia files folder and the output json file path should be modified accordingly. 
The final code is on **main** branch and is a java application. We worked with **java jdk-17**, **Maven 4.0.0**, and **Lucene 8.11**
The java project was configured with Maven and all the dependencies are in **pom.xml** file. So, you need to run install for
this file according to your ide (for example, in IntelliJ Idea in the maven panel you have buttons for clear and install). <br>
To run the retrieval part and see the result run *DataMiningTestIndexApplication.java* file and the result will be saved in two files: *performance.txt* (contains p@1, p@3, p@5 scores)
and *results.txt* (contains the clue, category, correct answer and then the returned result = top 10 wiki title pages and the score)

### Files description
- *TextToJson.py* : separate file for creating the json files from the wikipedia pages = *pages_final*
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
Documentation link: https://docs.google.com/document/d/1-Ti7uZPA1M9CQSmWAvrQzTVwvtRHhcEAny-c3DtlAnE/edit
<br>

### Presentation

### Video

### Indexes
<br>
Index with stop words remmoval and stemming: https://we.tl/t-oGSiHv3YUu
<br>
Index with stop words remmoval and lemmatization: https://we.tl/t-n2D3trXkby 


