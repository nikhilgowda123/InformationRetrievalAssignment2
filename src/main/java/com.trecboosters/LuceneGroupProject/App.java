package com.trecboosters.LuceneGroupProject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trecboosters.LuceneGroupProject.constants.CommonConstants;
import com.trecboosters.LuceneGroupProject.parser.Parser;

public class App 
{
	public static Logger log = LoggerFactory.getLogger(App.class);

	public static void main( String[] args )
	{

		log.info("Started");
		Options options = new Options();
		options.addOption(CommonConstants.ANALYSER_OPTION, true, CommonConstants.ANALYSER_HELP_TEXT);
		options.addOption(CommonConstants.SIMILARITY_OPTION, true, CommonConstants.SIMILARITY_HELP_TEXT);
		options.addOption(CommonConstants.HELP_OPTION, false, "Help");
		String selectedAnalyzer = CommonConstants.DEFAULT_ANALYSER;
		String selectedSimilarity = CommonConstants.DEFAULT_SIMILARITY;
		CommandLineParser parser = new DefaultParser();



		try {
			CommandLine commandLine = parser.parse(options, args);
			if (args.length == 0) {
				log.warn("Command Line Arguments not detected. Using Default values");
			} else {

				if (commandLine.hasOption(CommonConstants.ANALYSER_OPTION)) 
					selectedAnalyzer = commandLine.getOptionValue(CommonConstants.ANALYSER_OPTION).toLowerCase();                    
				
				if (commandLine.hasOption(CommonConstants.SIMILARITY_OPTION)) 
					selectedSimilarity = commandLine.getOptionValue(CommonConstants.SIMILARITY_OPTION).toLowerCase();                     
				
				if (commandLine.hasOption(CommonConstants.HELP_OPTION)) {
					HelpFormatter helpFormatter = new HelpFormatter();
					helpFormatter.printHelp(CommonConstants.JAR_NAME, options);
					return;
				}
			}
		} catch (Exception e) {
			log.error("Exception occured while parsing the arguments",e);
		}
		log.info("Selected Analyser : "+selectedAnalyzer);
		log.info("Selected Similarity : "+selectedSimilarity);
		
		log.info("----------------STARTING INDEXING-------------------");
		Indexer.createIndex(CommonConstants.CRAN_DOCUMENT_PATH, selectedAnalyzer, selectedSimilarity);
		log.info("----------------COMPLETED INDEXING-------------------");
		log.info("---------PARTING TOPICS FOR QUERY GENERATION---------");
		Parser.parseTopic(CommonConstants.TOPIC_PATH);
		log.info("------------------RUNNING QUERIES--------------------");
		Searcher.runQueries(CommonConstants.CRAN_QUERY_PATH, 1000, selectedAnalyzer, selectedSimilarity);
		log.info("-------------RUNNING QUERIES COMPLETED---------------");		
		log.info("---------------------FINISHED------------------------");
	}
}

