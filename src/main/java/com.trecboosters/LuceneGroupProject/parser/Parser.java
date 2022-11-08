package com.trecboosters.LuceneGroupProject.parser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trecboosters.LuceneGroupProject.constants.CommonConstants;
import com.trecboosters.LuceneGroupProject.model.CranDocument;
import com.trecboosters.LuceneGroupProject.model.QueryModel;
import com.trecboosters.LuceneGroupProject.model.TopicModel;

public class Parser {

	private static Logger log = LoggerFactory.getLogger(Parser.class);

	private static ArrayList<Document> cranDocuments = new ArrayList<Document>();
	private static ArrayList<QueryModel> queries = new ArrayList<QueryModel>();
	private static ArrayList<TopicModel> topics = new ArrayList<TopicModel>();

	public static ArrayList<Document> parse(String docPath) throws IOException {
		try {
			List<String> fileData = Files.readAllLines(Paths.get(docPath), StandardCharsets.UTF_8);

			String text = StringUtils.EMPTY;
			String fieldToAdd = StringUtils.EMPTY;
			CranDocument cranDocument = null;

			for (String line : fileData) {
				if (line.trim().length() > 0 && line.charAt(0) == CommonConstants.DOT) {
					if (fieldToAdd != null) {
						switch (fieldToAdd) {
						case CommonConstants.CRAN_DOC_FIELD_TITLE:
							cranDocument.setTitle(text);
							break;
						case CommonConstants.CRAN_DOC_FIELD_AUTHOR:
							cranDocument.setAuthor(text);
							break;
						case CommonConstants.CRAN_DOC_FIELD_WORDS:
							cranDocument.setWords(text);
							break;
						default:
							break;
						}
					}
					text = StringUtils.EMPTY;
					String field = Objects.requireNonNull(line.substring(0, 2));
					switch (field) {
					case CommonConstants.CRAN_DOC_FIELD_ID:
						if (cranDocument != null)
							cranDocuments.add(createLuceneDocument(cranDocument));
						cranDocument = new CranDocument();
						cranDocument.setDocid(line.substring(3));
						break;
					case CommonConstants.CRAN_DOC_FIELD_TITLE:
					case CommonConstants.CRAN_DOC_FIELD_AUTHOR:
					case CommonConstants.CRAN_DOC_FIELD_WORDS:
						fieldToAdd = field;
						break;
					default:
						break;
					}
				} else
					text += line + CommonConstants.SPACE;
			}
			if (cranDocument != null) {
				cranDocument.setWords(text);
				cranDocuments.add(createLuceneDocument(cranDocument));
			}
		} catch (IOException ioe) {
			log.error("Error while parsing document", ioe);
		}
		return cranDocuments;
	}

	public static ArrayList<QueryModel> parseQuery(String queryPath) throws IOException {
		try {
			List<String> fileData = Files.readAllLines(Paths.get(queryPath), StandardCharsets.UTF_8);

			String text = StringUtils.EMPTY;
			QueryModel queryModel = null;
			String fieldToAdd = null;

			for (String line : fileData) {
				if (line.trim().length() > 0 && line.charAt(0) == CommonConstants.DOT) {
					if (fieldToAdd != null) {
						queryModel.setQuery(text);
					}
					text = StringUtils.EMPTY;
					String field = Objects.requireNonNull(line.substring(0, 2));
					switch (field) {
					case CommonConstants.CRAN_DOC_FIELD_ID:
						if (queryModel != null)
							queries.add(queryModel);
						queryModel = new QueryModel();
						queryModel.setQueryid(line.substring(3));
						break;
					case CommonConstants.CRAN_DOC_FIELD_WORDS:
						fieldToAdd = field;
						break;
					default:
						break;
					}
				} else
					text += line + CommonConstants.SPACE;
			}
			if (queryModel != null) {
				queryModel.setQuery(text);
				queries.add(queryModel);
			}
		} catch (IOException e) {
			log.error("Exception occured while parsing query", e);
		}
		return queries;
	}

	public static ArrayList<TopicModel> parseTopic(String topicPath) {
		try {
			List<String> fileData = Files.readAllLines(Paths.get(topicPath), StandardCharsets.UTF_8);

			String text = StringUtils.EMPTY;
			TopicModel topicModel = null;

			for (String line : fileData) {

				if(!(line.trim().length() > 0 )){
					continue;
				}

				if (line.charAt(0) == CommonConstants.ANGLE_BRACKET) {
					String field = line.substring(0, 5);

					switch (field) {
						case CommonConstants.TOPIC_FIELD_TOP:
							topicModel = new TopicModel();
							break;

						case CommonConstants.TOPIC_FIELD_NUMBER:
							topicModel.setNum(line.substring(14));
							break;

						case CommonConstants.TOPIC_FIELD_TITLE:
							String[] titles = line.substring(8).split(CommonConstants.COMMA);
							for(String title : titles) {
								topicModel.setTitle(title.trim());
							}
							break;

						case CommonConstants.TOPIC_FIELD_DESCRIPTION:
							break;

						case CommonConstants.TOPIC_FIELD_NARRATIVE:
							topicModel.setDesc(text); // current line is the beginning of narrative => description is complete
							text = StringUtils.EMPTY; // empty text so narrative can start being collected
							break;

						case CommonConstants.TOPIC_FIELD_BOTTOM:
							topicModel.setNarr(text); // current line marks the end of a document => narrative is complete
							text = StringUtils.EMPTY;
							topics.add(topicModel);

//							log.info("Num: " + topicModel.getNum());
//							log.info("Title: " + topicModel.getTitles());
//							log.info("Description: " + topicModel.getDesc());
//							log.info("Narrative: " + topicModel.getNarr());
							break;

						default:
							log.info("Unknown field parsed: " + field);
					}
				} else {
					text += line + CommonConstants.SPACE;
				}
			}

		} catch (IOException e) {
			log.error("Exception occured while parsing topic", e);
		}
		return topics;
	}

	private static Document createLuceneDocument(CranDocument doc) {
		Document document = new Document();
		document.add(new StringField(CommonConstants.LUCENE_DOC_ID, doc.getDocid(), Field.Store.YES));
		document.add(new TextField(CommonConstants.TITLE, doc.getTitle(), Field.Store.YES));
		document.add(new TextField(CommonConstants.AUTHOR, doc.getAuthor(), Field.Store.YES));
		document.add(new TextField(CommonConstants.WORDS, doc.getWords(), Field.Store.YES));
		return document;
	}

}
