package com.trecboosters.LuceneGroupProject.model;

import java.util.ArrayList;
import java.util.List;

public class TopicModel {

	public String num;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public List<String> titles = new ArrayList<String>();

	public List<String> getTitles() {
		return titles;
	}

	public void setTitle(String title) {
		titles.add(title);
	}

	public String desc = "";

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String narr = "";

	public String getNarr() {
		return narr;
	}

	public void setNarr(String narr) {
		this.narr = narr;
	}


	public TopicModel() {

	}

}
