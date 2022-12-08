package com.trecboosters;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trecboosters.constants.CommonConstants;

public class App {
	public static Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {

		log.info("Started");
		Options options = new Options();
		options.addOption(CommonConstants.SIMILARITY_OPTION, true, CommonConstants.SIMILARITY_HELP_TEXT);
		options.addOption(CommonConstants.HELP_OPTION, false, "Help");
		String selectedAnalyzer = "";
		String selectedSimilarity = "";
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine commandLine = parser.parse(options, args);
			if (args.length == 0) {
				log.warn("Command Line Arguments not detected. Using Default values");
			} else {

				if (commandLine.hasOption(CommonConstants.SIMILARITY_OPTION))
					selectedSimilarity = commandLine.getOptionValue(CommonConstants.SIMILARITY_OPTION);

				if (commandLine.hasOption(CommonConstants.HELP_OPTION)) {
					HelpFormatter helpFormatter = new HelpFormatter();
					helpFormatter.printHelp(CommonConstants.JAR_NAME, options);
					return;
				}
			}
		} catch (Exception e) {
			log.error("Exception occured while parsing the arguments", e);
		}
		
		selectedAnalyzer = "CustomAnalyser";
		
		if(selectedSimilarity == null || selectedSimilarity.isEmpty())
			selectedSimilarity = "BM25";
		
		log.info("Selected Analyser : " + selectedAnalyzer);
		log.info("Selected Similarity : " + selectedSimilarity);

		log.info("----------------STARTING INDEXING-------------------");
		Indexer.createIndex(CommonConstants.CRAN_DOCUMENT_PATH, selectedAnalyzer, selectedSimilarity);
		log.info("----------------COMPLETED INDEXING-------------------");
		log.info("------------------RUNNING QUERIES--------------------");
		Searcher.runQueries(CommonConstants.QUERY_PATH, 1000, selectedAnalyzer, selectedSimilarity);
		log.info("-------------RUNNING QUERIES COMPLETED---------------");
		log.info("---------------------FINISHED------------------------");
	}
}
