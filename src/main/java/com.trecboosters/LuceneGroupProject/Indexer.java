package com.trecboosters.LuceneGroupProject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trecboosters.LuceneGroupProject.constants.CommonConstants;
import com.trecboosters.LuceneGroupProject.parser.Parser;

public class Indexer {

	public static Logger log = LoggerFactory.getLogger(Indexer.class);

	public static boolean createIndex(String cranDocumentPath, String selectedAnalyser, String selectedSimilarity) {

		Analyzer analyzer = getSelectedAnalyzerObject(selectedAnalyser);
		Similarity similarity = getSelectedSimilarityObject(selectedSimilarity);

		try {
			assert analyzer != null;
			Directory directory = FSDirectory.open(Paths.get(CommonConstants.INDEX_PATH));
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			iwc.setSimilarity(similarity);
			IndexWriter indexWriter = new IndexWriter(directory, iwc);

			ArrayList<Document> crandocuments = Parser.parse(cranDocumentPath);
			indexWriter.addDocuments(crandocuments);
			indexWriter.close();
			log.info("Indexing done for " + crandocuments.size() + "Documents");
			log.info("Please check index path " + CommonConstants.INDEX_PATH);

		} catch (IOException ioe) {
			log.error("Error occured while indexing", ioe);
		}
		return true;
	}

	public static CharArraySet getStopWords() {
		CharArraySet stopwords = null;
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(CommonConstants.STOPWORD_FILE_PATH));
			String[] words = new String(encoded, StandardCharsets.UTF_8).split("\n");
			stopwords = new CharArraySet(Arrays.asList(words), true);
		} catch (IOException e) {
			log.error("Error occured while reading stopwords file" + CommonConstants.STOPWORD_FILE_PATH, e);
		}
		return stopwords;
	}

	public static Analyzer getSelectedAnalyzerObject(String selectedAnalyser) {

		switch (selectedAnalyser) {
		case CommonConstants.SIMPLE_ANALYSER:
			return new SimpleAnalyzer();
		case CommonConstants.STANDARD_ANALYSER:
			return new StandardAnalyzer();
		case CommonConstants.WHITESPACE_ANALYSER:
			return new WhitespaceAnalyzer();
		case CommonConstants.ENGLISH_ANALYSER:
			return new EnglishAnalyzer(getStopWords());
		case CommonConstants.STOP_ANALYSER:
			return new StopAnalyzer(getStopWords());
		default:
			break;
		}
		return new EnglishAnalyzer();
	}

	public static Similarity getSelectedSimilarityObject(String selectedSimilarity) {
		switch (selectedSimilarity) {
		case CommonConstants.CLASSIC_SIMILARITY:
			return new ClassicSimilarity();
		case CommonConstants.BOOLEAN_SIMILARITY:
			return new BooleanSimilarity();
		case CommonConstants.BM25_SIMILARITY:
			return new BM25Similarity();
		case CommonConstants.LMDS_SIMILARITY:
			return new LMDirichletSimilarity();
		}
		return new BM25Similarity();
	}

}
