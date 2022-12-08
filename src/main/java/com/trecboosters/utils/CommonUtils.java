package com.trecboosters.utils;

import java.io.BufferedReader;
import java.io.IOException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CommonUtils {
	
	
	public static String getFile(BufferedReader br) throws IOException {
		try {
			StringBuilder stringBuilder = new StringBuilder();
			String nextLine = br.readLine();

			while (nextLine != null) {
				stringBuilder.append(nextLine);
				stringBuilder.append("\n");
				nextLine = br.readLine();
			}
			return stringBuilder.toString();
		} finally {
			br.close();
		}
	}
	
	public static String refractorTags(Element doc, String tag, boolean isLatimesFile) {
		Elements element = doc.getElementsByTag(tag);
		Elements tmpElement = element.clone();
		String data = tmpElement.toString();
		if (data.contains("\n"))
			data = data.replaceAll("\n", " ").trim();
		if (data.contains(("<" + tag + ">").toLowerCase()))
			data = data.replaceAll("<" + tag.toLowerCase() + ">", "").trim();
		if (data.contains(("</" + tag + ">").toLowerCase()))
			data = data.replaceAll("</" + tag.toLowerCase() + ">", "").trim();
		if(isLatimesFile)
		{
			if (data.contains("<p>"))
				data = data.replaceAll("<p>", "").trim();
			if (data.contains("</p>"))
				data = data.replaceAll("</p>", "").trim();
		}
		data = data.trim().replaceAll(" +", " ");
		return data;
	}

}
