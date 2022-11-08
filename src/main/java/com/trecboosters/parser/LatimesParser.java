package com.trecboosters.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trecboosters.constants.CommonConstants;
import com.trecboosters.utils.CommonUtils;

public class LatimesParser {

	private static Logger log = LoggerFactory.getLogger(LatimesParser.class);
	private static BufferedReader br;
	private static int docCount = 0;

	public static void indexLatimesFiles(IndexWriter iwriter) throws IOException {

		log.info("\n Los Angeles Times (LATIMES) Indexing Started");

		Directory directory = FSDirectory.open(Paths.get(CommonConstants.LATIMES_DIRECTORY));

		for (String latimesFile : directory.listAll()) {
			if (!latimesFile.equals(CommonConstants.LATIMES_IGNORE_FILES[0])
					&& !latimesFile.equals(CommonConstants.LATIMES_IGNORE_FILES[1])) {
				br = new BufferedReader(
						new FileReader(CommonConstants.LATIMES_DIRECTORY + CommonConstants.SLASH + latimesFile));
				parseLatimesFiles(iwriter);
			}
		}
		log.info("\n Completed indexing of " + docCount + " LATIMES files.");

		directory.close();
	}

	public static void parseLatimesFiles(IndexWriter iwriter) throws IOException {

		String fileContents = CommonUtils.getFile(br);
		org.jsoup.nodes.Document document = Jsoup.parse(fileContents);
		List<Element> list = document.getElementsByTag(CommonConstants.DOC_TAG);

		for (Element doc : list) {
			Document laTimesDoc = new Document();

			if (Objects.nonNull(doc.getElementsByTag(CommonConstants.DOC_NO_TAG)))
				laTimesDoc.add(new StringField(CommonConstants.DOC_NO_TAG.toLowerCase(),
						CommonUtils.refractorTags(doc, CommonConstants.DOC_NO_TAG, true), Field.Store.YES));
			if (Objects.nonNull(doc.getElementsByTag(CommonConstants.HEADLINE_TAG)))
				laTimesDoc.add(new TextField(CommonConstants.HEADLINE_TAG.toLowerCase(),
						CommonUtils.refractorTags(doc, CommonConstants.HEADLINE_TAG, true), Field.Store.YES));
			if (Objects.nonNull(doc.getElementsByTag(CommonConstants.TEXT_TAG)))
				laTimesDoc.add(new TextField(CommonConstants.TEXT_TAG.toLowerCase(),
						CommonUtils.refractorTags(doc, CommonConstants.TEXT_TAG, true)
						+ CommonUtils.refractorTags(doc, CommonConstants.GRAPHIC_TAG, true)
						+ CommonUtils.refractorTags(doc, CommonConstants.SUBJECT_TAG, true),
						Field.Store.NO));

			iwriter.addDocument(laTimesDoc);
			docCount++;
		}
	}

}
