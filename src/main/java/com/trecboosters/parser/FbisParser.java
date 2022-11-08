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

public class FbisParser {

	private static Logger log = LoggerFactory.getLogger(FbisParser.class);

	private static BufferedReader br;
	private static int docCount = 0;

	public static void indexFbisFiles(IndexWriter iwriter) throws IOException {
		log.info("\n Foreign Broadcast Information Service (FBIS) Indexing Started");
		Directory directory = FSDirectory.open(Paths.get(CommonConstants.FBIS_DIRECTORY));
		for (String file : directory.listAll()) {
			if (!file.equals(CommonConstants.FBIS_IGNORE_FILES[0])
					&& !file.equals(CommonConstants.FBIS_IGNORE_FILES[1])) {
				br = new BufferedReader(new FileReader(CommonConstants.FBIS_DIRECTORY + CommonConstants.SLASH + file));
				parseFbisFiles(iwriter);
			}
		}
		log.info("\n Completed indexing of " + docCount + " FBIS files.");
		directory.close();
	}

	private static void parseFbisFiles(IndexWriter iwriter) throws IOException {

		String fbisFileContent = CommonUtils.getFile(br);
		org.jsoup.nodes.Document document = Jsoup.parse(fbisFileContent);
		List<Element> list = document.getElementsByTag(CommonConstants.DOC_TAG);

		for (Element doc : list) {
			Document fbisFile = new Document();

			if (Objects.nonNull(doc.getElementsByTag(CommonConstants.DOC_NO_TAG)))
				fbisFile.add(new StringField(CommonConstants.DOC_NO_TAG.toLowerCase(),
						CommonUtils.refractorTags(doc, CommonConstants.DOC_NO_TAG, false), Field.Store.YES));
			if (Objects.nonNull(doc.getElementsByTag(CommonConstants.TI_TAG)))
				fbisFile.add(new TextField(CommonConstants.HEADLINE_TAG.toLowerCase(),
						CommonUtils.refractorTags(doc, CommonConstants.TI_TAG, false), Field.Store.YES));
			if (Objects.nonNull(doc.getElementsByTag(CommonConstants.TEXT_TAG)))
				fbisFile.add(new TextField(CommonConstants.TEXT_TAG.toLowerCase(),
						CommonUtils.refractorTags(doc, CommonConstants.TEXT_TAG, false), Field.Store.NO));

			iwriter.addDocument(fbisFile);
			docCount++;
		}
	}
}
