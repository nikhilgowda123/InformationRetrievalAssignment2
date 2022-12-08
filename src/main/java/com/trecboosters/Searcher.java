package com.trecboosters;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
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
import com.trecboosters.model.QueryModel;
import com.trecboosters.parser.Parser;

public class Searcher {

	private static Logger log = LoggerFactory.getLogger(Searcher.class);

	private static int NUM_RESULTS = 1;

	public static void runQueries(String queryPath, int numResults, String selectedAnalyser,
			String selectedSimilarity) {
		try {
			Directory directory = FSDirectory.open(Paths.get(CommonConstants.INDEX_PATH));
			DirectoryReader directoryReader = DirectoryReader.open(directory);

			IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
			Similarity sm = null;
			if(selectedSimilarity == null || selectedSimilarity.isEmpty())
			{
				sm = new BM25Similarity();
			}
			else
			{
				if("BM25".equalsIgnoreCase(selectedSimilarity))
					sm = new BM25Similarity();
				else if("Multi".equalsIgnoreCase(selectedSimilarity))
				{
					Similarity[] sims = {new BM25Similarity(), new LMJelinekMercerSimilarity(new LMSimilarity.DefaultCollectionModel(), 0.5f)};
		            sm = new MultiSimilarity(sims);					
				}
				else
				{
					sm = new BM25Similarity();
				}
			}
			indexSearcher.setSimilarity(sm);
			
			
			Analyzer analyzer = new CustomDocumentAnalyzer();
			
			File resultFile = new File(CommonConstants.OUTPUT_FILE_PATH);
			resultFile.getParentFile().mkdirs();
			PrintWriter writer = new PrintWriter(resultFile, StandardCharsets.UTF_8.name());
			ArrayList<QueryModel> queries = Parser.parseQuery(queryPath);

			HashMap<String, Float> boosts = new HashMap<String, Float>();
			boosts.put(CommonConstants.HEADLINE_TAG.toLowerCase(), (float) 0.5);
			boosts.put(CommonConstants.TEXT_TAG.toLowerCase(), (float) 10);
			MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
					new String[] { CommonConstants.HEADLINE_TAG.toLowerCase(), CommonConstants.TEXT_TAG.toLowerCase() },
					analyzer, boosts);
			
			NUM_RESULTS = numResults;
			log.debug("Executing the fetched queries, max_hits set to: " + NUM_RESULTS);

			for (QueryModel element : queries) {
				Query titleQuery = queryParser.parse(QueryParser.escape(element.getTitle().trim()));
				Query descriptionQuery = queryParser.parse(QueryParser.escape(element.getDesc().trim()));
				
				List<String> splitNarrative = splitNarrIntoRelNotRel(QueryParser.escape(element.getNarr().trim()));
                String relevantNarr = splitNarrative.get(0).trim();
                String irrelevantNarr = splitNarrative.get(1).trim();

				BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
				booleanQueryBuilder.add(new BoostQuery(titleQuery, (float) 6), BooleanClause.Occur.SHOULD);
				booleanQueryBuilder.add(new BoostQuery(descriptionQuery, (float) 4.0), BooleanClause.Occur.SHOULD);
				
				 Query narrativeQuery = null;
                 Query irrNarrativeQuery = null;
                 if (relevantNarr.length() > 0) {
                     narrativeQuery = queryParser.parse(QueryParser.escape(relevantNarr));
                 }
                 if (irrelevantNarr.length() > 0) {
                     irrNarrativeQuery = queryParser.parse(QueryParser.escape(irrelevantNarr));
                 }
				
                 if (narrativeQuery != null) {
                	 booleanQueryBuilder.add(new BoostQuery(narrativeQuery, (float) 2.0), BooleanClause.Occur.SHOULD);
                 }
                 if (irrNarrativeQuery != null) {
                	 booleanQueryBuilder.add(new BoostQuery(irrNarrativeQuery, (float) 1), BooleanClause.Occur.SHOULD);
                 }
				
				search(indexSearcher, booleanQueryBuilder.build(), writer, element.getNum() ,queries.indexOf(element),
						selectedAnalyser, selectedSimilarity);
			}

			directoryReader.close();
			writer.close();
			directory.close();
			log.info("Searching complete output written to " + CommonConstants.OUTPUT_FILE_PATH);

		} catch (IOException | ParseException e) {
			log.error("Exception occured while running queries", e);
		}
	}
	private static List<String> splitNarrIntoRelNotRel(String narrative) {
        StringBuilder relevantNarr = new StringBuilder();
        StringBuilder irrelevantNarr = new StringBuilder();
        List<String> splitNarrative = new ArrayList<>();
        BreakIterator bi = BreakIterator.getSentenceInstance();
        bi.setText(narrative);
        int index = 0;
        while (bi.next() != BreakIterator.DONE) {
            String sentence = narrative.substring(index, bi.current());
            if (!sentence.contains("not relevant") && !sentence.contains("irrelevant")) {
                relevantNarr.append(sentence.replaceAll(
                        "a relevant document identifies|a relevant document could|a relevant document may|a relevant document must|a relevant document will|a document will|to be relevant|relevant documents|a document must|relevant|will contain|will discuss|will provide|must cite",
                        ""));
            } else {
                irrelevantNarr.append(sentence.replaceAll("are also not relevant|are not relevant|are irrelevant|is not relevant|not|NOT", ""));
            }
            index = bi.current();
        }
        splitNarrative.add(relevantNarr.toString());
        splitNarrative.add(irrelevantNarr.toString());
        return splitNarrative;
    }

	public static void search(IndexSearcher is, Query query, PrintWriter writer, String queryNum, int queryID, String selectedAnalyser,
			String selectedSimilarity) throws IOException {
		ScoreDoc[] hits = is.search(query, NUM_RESULTS).scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			Document hitDocument = is.doc(hits[i].doc);
			writer.println(queryNum + " Q" + queryID + " " + hitDocument.get(CommonConstants.DOC_NO_TAG.toLowerCase()) + " " +(i+1) +" "
					+ hits[i].score + " " + selectedAnalyser + selectedSimilarity);
		}
	}

}
