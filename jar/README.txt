You can run the index creation by executing:
java -jar index/index.jar -wf <wikipedia-file> -od <output-directory>

You can run a word search by executing:
java -jar search/search.jar -id <index-directory> -w sweden

You can execute a contributor search by executing:
java -jar search/search.jar -id <index-directory> -c tom harrison