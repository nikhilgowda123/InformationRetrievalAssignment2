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

public class Fr94Parser {

	private static Logger log = LoggerFactory.getLogger(Fr94Parser.class);
	private static BufferedReader br;
	private static int docCount = 0;

	public static void indexFr94Files(IndexWriter iwriter) throws IOException {

		log.info("\n Federal Register (FR94) Indexing Started");
		Directory directory = FSDirectory.open(Paths.get(CommonConstants.FR94_DIRECTORY));
		for (String folder : directory.listAll()) {
			if (!folder.equals(CommonConstants.FR94_IGNORE_FILES[0])
					&& !folder.equals(CommonConstants.FR94_IGNORE_FILES[1])
					&& !folder.equals(CommonConstants.FR94_IGNORE_FILES[2])) {
				Directory fr94Files = FSDirectory
						.open(Paths.get(CommonConstants.FR94_DIRECTORY + CommonConstants.SLASH + folder));
				for (String fr94File : fr94Files.listAll()) {
					br = new BufferedReader(new FileReader(CommonConstants.FR94_DIRECTORY + CommonConstants.SLASH
							+ folder + CommonConstants.SLASH + fr94File));
					parseFr94Files(iwriter);
				}
			}
		}
		log.info("\n Completed indexing of " + docCount + " FR94 files.");
		directory.close();
	}

	public static void parseFr94Files(IndexWriter iwriter) throws IOException {

		String fr94FileContents = CommonUtils.getFile(br);
		org.jsoup.nodes.Document document = Jsoup.parse(fr94FileContents);
		List<Element> documentList = document.getElementsByTag(CommonConstants.DOC_TAG);

		for (Element doc : documentList) {
			Document fbisDocument = new Document();

			if (Objects.nonNull(doc.getElementsByTag(CommonConstants.DOC_NO_TAG)))
				fbisDocument.add(new StringField(CommonConstants.DOC_NO_TAG.toLowerCase(),
						CommonUtils.refractorTags(doc, CommonConstants.DOC_NO_TAG, false), Field.Store.YES));
			fbisDocument.getValues(CommonConstants.DOC_NO_TAG.toLowerCase());
			if (Objects.nonNull(doc.getElementsByTag(CommonConstants.DOC_TITLE_TAG)))
				fbisDocument.add(new TextField(CommonConstants.HEADLINE_TAG.toLowerCase(),
						CommonUtils.refractorTags(doc, CommonConstants.DOC_TITLE_TAG, false), Field.Store.YES));
			if (Objects.nonNull(doc.getElementsByTag(CommonConstants.TEXT_TAG)))
				fbisDocument
				.add(new TextField(CommonConstants.TEXT_TAG.toLowerCase(),
						CommonUtils.refractorTags(doc, CommonConstants.TEXT_TAG, false)
						+ CommonUtils.refractorTags(doc, CommonConstants.FOOTNOTE_TAG, false),
						Field.Store.NO));

			iwriter.addDocument(fbisDocument);
			docCount++;
		}
	}

}
