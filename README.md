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
