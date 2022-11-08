package com.assignment.lucene.lucenmavenprojectnikhil.model;

public class QueryModel {

	public String queryid = "";

	public String getQueryid() {
		return queryid;
	}

	public void setQueryid(String queryid) {
		this.queryid = queryid;
	}

	public String query = "";

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public QueryModel() {
	}

	public QueryModel(String queryid, String query) {
		this.queryid = queryid;
		this.query = query;
	}

}
