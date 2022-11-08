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

public class FtParser {

	private static Logger log = LoggerFactory.getLogger(FtParser.class);

	private static BufferedReader br;
	private static int docCount = 0;

	public static void indexFtFiles(IndexWriter iwriter) throws IOException {
		log.info("\n Financial Times Limited (FT) Indexing Started");
		Directory directory = FSDirectory.open(Paths.get(CommonConstants.FT_DIRECTORY));

		for (String file : directory.listAll()) {
			if (!file.equals(CommonConstants.FT_IGNORE_FILES[0]) && !file.equals(CommonConstants.FT_IGNORE_FILES[1])
					&& !file.equals(CommonConstants.FT_IGNORE_FILES[2])) {
				Directory ftFiles = FSDirectory
						.open(Paths.get(CommonConstants.FT_DIRECTORY + CommonConstants.SLASH + file));
				for (String ftFile : ftFiles.listAll()) {
					br = new BufferedReader(new FileReader(CommonConstants.FT_DIRECTORY + CommonConstants.SLASH + file
							+ CommonConstants.SLASH + ftFile));
					parseFtFiles(iwriter);
				}
			}
		}
		log.info("\n Completed indexing of " + docCount + " FT files.");

		directory.close();

	}

	public static void parseFtFiles(IndexWriter iwriter) throws IOException {

		String ftFileContents = CommonUtils.getFile(br);
		org.jsoup.nodes.Document document = Jsoup.parse(ftFileContents);
		List<Element> list = document.getElementsByTag(CommonConstants.DOC_TAG);

		for (Element doc : list) {
			Document ftDocument = new Document();

			if (Objects.nonNull(doc.getElementsByTag(CommonConstants.DOC_NO_TAG)))
				ftDocument.add(new StringField(CommonConstants.DOC_NO_TAG.toLowerCase(),
						CommonUtils.refractorTags(doc, CommonConstants.DOC_NO_TAG, false), Field.Store.YES));
			if (Objects.nonNull(doc.getElementsByTag(CommonConstants.HEADLINE_TAG)))
				ftDocument.add(new TextField(CommonConstants.HEADLINE_TAG.toLowerCase(),
						CommonUtils.refractorTags(doc, CommonConstants.HEADLINE_TAG, false), Field.Store.YES));
			if (Objects.nonNull(doc.getElementsByTag(CommonConstants.TEXT_TAG)))
				ftDocument
				.add(new TextField(CommonConstants.TEXT_TAG.toLowerCase(),
						CommonUtils.refractorTags(doc, CommonConstants.TEXT_TAG, false)
						+ CommonUtils.refractorTags(doc, CommonConstants.DATELINE_TAG, false),
						Field.Store.NO));

			iwriter.addDocument(ftDocument);
			docCount++;
		}
	}

}
