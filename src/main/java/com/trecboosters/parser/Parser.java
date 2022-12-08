package com.trecboosters.parser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trecboosters.constants.CommonConstants;
import com.trecboosters.model.QueryModel;

public class Parser {

	private static Logger log = LoggerFactory.getLogger(Parser.class);

	private static ArrayList<QueryModel> queries = new ArrayList<QueryModel>();

	public static ArrayList<QueryModel> parseQuery(String queryPath) {
		try {
			List<String> fileData = Files.readAllLines(Paths.get(queryPath), StandardCharsets.UTF_8);

			String text = StringUtils.EMPTY;
			QueryModel query = null;

			for (String line : fileData) {

				if(!(line.trim().length() > 0 )){
					continue;
				}

				if (line.charAt(0) == CommonConstants.ANGLE_BRACKET) {
					String field = line.substring(0, 5);

					switch (field) {
						
						case CommonConstants.QUERY_FIELD_TOP:
							query = new QueryModel();
							break;
							
						case CommonConstants.QUERY_FIELD_NUMBER:
							query.setNum(line.substring(14).trim());
							break;
							
						case CommonConstants.QUERY_FIELD_TITLE:
							query.setTitle(line.substring(8).trim());
							break;
							
						case CommonConstants.QUERY_FIELD_DESCRIPTION:
							break;
							
						case CommonConstants.QUERY_FIELD_NARRATIVE:
							query.setDesc(text.trim()); // current line is the beginning of narrative => description is complete
							text = StringUtils.EMPTY; // empty text so narrative can start being collected
							break;
							
						case CommonConstants.QUERY_FIELD_BOTTOM:
							query.setNarr(text.trim()); // current line marks the end of a document => narrative is complete
							text = StringUtils.EMPTY;
							queries.add(query);
//							log.info("Num: " + query.getNum());
//							log.info("Title: " + query.getTitle());
//							log.info("Description: " + query.getDesc());
//							log.info("Narrative: " + query.getNarr());
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
		return queries;
	}

}
