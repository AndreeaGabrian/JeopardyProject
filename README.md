# JeopardyProject

For creating an index of all wikipedia pages using SOLR we had to:
	<br> 1. Create a core
	<br> 2. Import data into the core (this is the moment where indexing happens)
After the above steps we are given an "index" folder where all the data is stored.

## 1. Creating a core

To create a core in SOLR you will first need to go in the **data** folder where found where you installed solr on your personal computer.
In that **data** folder you will need to create a folder with the name of the core you wish to create.
In the **data/<your_core_name>** folder you usually have to create a **conf** and a **data** folder.
<br>
In the **conf** folder you will add the most important files of a solr core: **solrconfig.xml** and **schema.xml**.
<br>
After everything is prepared you go to the solr admin interface and select **Core admin -> Add Core**. A popup will appear: ![image](https://github.com/AndreeaGabrian/JeopardyProject/assets/78824410/cb5315ff-d6a1-4c4a-a0dd-5c98a743c425)

<br> You will need to point the exact path of the files required for the creation of the solr core.
Meaning you need the path four you **<your_core_name>** folder, the path to **<your_core_name>/data** folder
and the path to your **solrconfig.xml** and **schema.xml** files.


## 2. Importing data into core

(TO mention: the documents or the json entries related to the wikipedia data were preprocessed before this step into files with .json extension)<br>
After the core is done creating, we need to import all the wikipedia pages into to the solr for indexing. 
One document in the solr index looks like this: <br>
**{title: <wiki_page_title>, category: <wiki_page_categorie>, content: <content_of_wikipages>}**
<br>

We created a run.sh script that will insert all the documents related to the wikipedia pages into the solr:
<br>
**run.sh** 
<br>
for f in ./*.json; do
<br>
    curl 'http://localhost:8983/solr/<your_core_name>/update?commit=true' --data-binary "@$f" -H 'Content-type:application/json'
    <br>
done


<br>

## SOLR official documentation

1. Creating index: https://solr.apache.org/guide/solr/latest/getting-started/tutorial-diy.html
<br>

Apache Solr creates its index through a process called indexing, which involves parsing and analyzing the documents to be indexed, extracting relevant information from them, and then storing that information in an optimized data structure for fast retrieval. <br>

Detailed description about how SOLR creates it's index:
<br>
1.Document Ingestion: Solr ingests documents, which can be in various formats such as XML, JSON, or CSV. These documents typically represent the data that needs to be indexed, such as web pages, documents, or database records.
<br>
2. Analysis: Solr processes each document through a series of analysis steps. This includes tokenization (breaking text into individual words or tokens), filtering (removing stopwords, applying stemming, etc.), and other linguistic processing based on the configured analyzers.
<br>
3. Document Indexing: After the analysis phase, Solr indexes the processed documents. It creates an inverted index, which is a mapping of terms to the documents that contain those terms. This allows for efficient full-text searching.
<br>
4. Index Storage: The indexed data is stored in an optimized data structure. Solr typically uses a combination of in-memory data structures and on-disk storage to efficiently store and retrieve the indexed documents.
<br>
5. Index Optimization: Solr continuously optimizes the index to ensure fast search performance. This includes strategies like segment merging, where smaller index segments are combined into larger segments to improve query performance.
<br>
6. Commit and Refresh: Solr periodically commits the changes to the index to make them durable. This involves flushing in-memory changes to disk and updating metadata to reflect the latest state of the index. Additionally, Solr supports real-time indexing, where documents are immediately available for search without the need for a commit operation.
<br>

