Steps to Run the Process

1) Compile the Code

-> mvn clean package -DskipTests

2) Execute the Generated JAR file

-> java -server -Xmx2g -cp ./target/lucenmavenprojectnikhil-0.0.1-SNAPSHOT.jar com.assignment.lucene.lucenmavenprojectnikhil.App

Note: option -a can be used for selecting analyzer, -s can be used for selecting similarities. use -h to know more about the list of available analyzers
and similarities to choose. 
Default Analyzer : English
Default Similarity : bm25

3) Execute trec_evac

-> cd trec_evac
-> make
-> ./trec_eval ../cranfieldData/QRelsforTRECeval ../output/results.txt 2>&1 | tee ../output/trec_eval_score.txt
