package com.assignment.lucene.lucenmavenprojectnikhil.constants;

public class CommonConstants {

	public static final String CRAN_DOCUMENT_PATH = "cranfieldData/cran.all.1400";

	public static final String CRAN_QUERY_PATH = "cranfieldData/cran.qry";

	public static final String ANALYSER_OPTION = "a";

	public static final String SIMILARITY_OPTION = "s";

	public static final String HELP_OPTION = "h";

	public static final String DEFAULT_ANALYSER = "english";

	public static final String DEFAULT_SIMILARITY = "bm25";

	public static final String ANALYSER_HELP_TEXT = "Choose the analysers to use from the list :  \n"
			+ "english\nsimple\nstandard\nstop\nwhitespace\n 'e.g -a simple'\n Default Analyser : english\n";

	public static final String SIMILARITY_HELP_TEXT = "Choose the similarity to use from the list :  \n"
			+ "classic\nboolean\nbm25\nlmds\n'e.g -s boolean'\nDefault Analyser : bm25\n";

	public static final String JAR_NAME = "Lucene-Cranfield.jar";

	public static final String STOPWORD_FILE_PATH = "cranfieldData/stopwords.txt";

	public static final String INDEX_PATH = "index/";

	public static final String SIMPLE_ANALYSER = "simple";

	public static final String STANDARD_ANALYSER = "standard";

	public static final String WHITESPACE_ANALYSER = "whitespace";

	public static final String ENGLISH_ANALYSER = "english";

	public static final String STOP_ANALYSER = "stop";

	public static final String CLASSIC_SIMILARITY = "classic";

	public static final String BOOLEAN_SIMILARITY = "boolean";

	public static final String BM25_SIMILARITY = "bm25";

	public static final String LMDS_SIMILARITY = "lmds";

	public static final String OUTPUT_FILE_PATH = "output/results.txt";

	public static final String TITLE = "title";

	public static final String AUTHOR = "author";

	public static final String WORDS = "words";

	public static final char DOT = '.';

	public static final char SPACE = ' ';

	public static final char NEWLINE = '\n';

	public static final String CRAN_DOC_FIELD_TITLE = ".T";

	public static final String CRAN_DOC_FIELD_AUTHOR = ".A";

	public static final String CRAN_DOC_FIELD_WORDS = ".W";

	public static final String CRAN_DOC_FIELD_ID = ".I";

	public static final String LUCENE_DOC_ID = "docid";

}
