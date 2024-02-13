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

After the core is done creating, we need to import all the wikipedia pages into to the solr for indexing. 
One document in the solr index looks like this: <br>
**{title: <wiki_page_title>,category: <wiki_page_categorie>,content: <content_of_wikipages>}**
<br>
