package com.trecboosters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.search.similarities.LMSimilarity;
import org.apache.lucene.search.similarities.MultiSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trecboosters.analyzer.CustomDocumentAnalyzer;
import com.trecboosters.constants.CommonConstants;
import com.trecboosters.parser.FbisParser;
import com.trecboosters.parser.Fr94Parser;
import com.trecboosters.parser.FtParser;
import com.trecboosters.parser.LatimesParser;

public class Indexer {

	public static Logger log = LoggerFactory.getLogger(Indexer.class);

	public static boolean createIndex(String cranDocumentPath, String selectedAnalyser, String selectedSimilarity) 
	{
		try
		{
			Directory directory = FSDirectory.open(Paths.get(CommonConstants.INDEX_PATH));
			
			Analyzer analyzer  = new CustomDocumentAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			
			IndexWriter indexWriter = new IndexWriter(directory, iwc);
			FtParser.indexFtFiles(indexWriter);
			Fr94Parser.indexFr94Files(indexWriter);
			FbisParser.indexFbisFiles(indexWriter);
			LatimesParser.indexLatimesFiles(indexWriter);
			indexWriter.close();
			log.info("Please check index path " + CommonConstants.INDEX_PATH);

		}
		catch (IOException ioe) 
		{
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
		case CommonConstants.CUSTOM_DOCUMENT_ANALYSER:
			return new CustomDocumentAnalyzer();
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
		case CommonConstants.MULTI_SIMILARITY:
			Similarity[] sims = {new BM25Similarity(), new LMJelinekMercerSimilarity(new LMSimilarity.DefaultCollectionModel(), 0.5f)};
            return new MultiSimilarity(sims);	
		}
		return new BM25Similarity();
	}

}
