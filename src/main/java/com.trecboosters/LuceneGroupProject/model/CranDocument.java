package com.trecboosters.LuceneGroupProject.model;

public class CranDocument {

	public String getDocid() {
		return documentId;
	}

	public void setDocid(String docid) {
		this.documentId = docid;
	}

	public String getTitle() {
		return documentTitle;
	}

	public void setTitle(String title) {
		this.documentTitle = title;
	}

	public String getAuthor() {
		return documentAuthor;
	}

	public void setAuthor(String author) {
		this.documentAuthor = author;
	}

	public String getWords() {
		return documentWords;
	}

	public void setWords(String words) {
		this.documentWords = words;
	}

	public String documentId = "";
	public String documentTitle = "";
	public String documentAuthor = "";
	public String documentWords = "";

	public CranDocument(String docid, String title, String author, String biblio, String words) {
		this.documentId = docid;
		this.documentTitle = title;
		this.documentAuthor = author;
		this.documentWords = words;
	}

	public CranDocument() {
	}

}
