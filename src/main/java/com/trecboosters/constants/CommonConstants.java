package com.trecboosters.constants;

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
	
	public static final String LUCENE_DOC_ID = "docid";
	
	public static final String FBIS_DIRECTORY = "documentSet/fbis";
    
	public static final String [] FBIS_IGNORE_FILES = {"readchg.txt", "readmefb.txt", ".DS_Store"};
	
	public static final String DOC_TAG = "DOC";
	
	public static final String DOC_NO_TAG = "DOCNO";
	
	public static final String DOC_TITLE_TAG = "DOCTITLE";
	
	public static final String HEADLINE_TAG = "HEADLINE";
	
	public static final String FOOTNOTE_TAG = "FOOTNOTE";
	
	public static final String TI_TAG = "TI";
	
	public static final String TEXT_TAG = "TEXT";
	
	public static final String DATELINE_TAG = "DATELINE";
	
	public static final String GRAPHIC_TAG = "GRAPHIC";
	
	public static final String SUBJECT_TAG = "SUBJECT";
	
    public static final String FT_DIRECTORY = "documentSet/ft";
    
    public static final String [] FT_IGNORE_FILES = {"readfrcg", "readmeft", ".DS_Store"};
    
    public static final String FR94_DIRECTORY = "documentSet/fr94";
    
    public static final String [] FR94_IGNORE_FILES = {"readchg", "readmefr", ".DS_Store"};
    
    public static final String LATIMES_DIRECTORY = "documentSet/latimes";
    
    public static final String [] LATIMES_IGNORE_FILES = {"readchg.txt", "readmela.txt", ".DS_Store"};

	public static final String TITLE = "title";

	public static final String AUTHOR = "author";

	public static final String WORDS = "words";

	public static final char DOT = '.';

	public static final char SPACE = ' ';
	
	public static final char SLASH = '/';

	public static final char NEWLINE = '\n';

	public static final String CRAN_DOC_FIELD_TITLE = ".T";

	public static final String CRAN_DOC_FIELD_AUTHOR = ".A";

	public static final String CRAN_DOC_FIELD_WORDS = ".W";

	public static final String CRAN_DOC_FIELD_ID = ".I";
	
	public static final char ANGLE_BRACKET = '<';
	
	public static final String COMMA = ",";
	
	public static final String QUERY_PATH = "topicsForQuery";
	
	public static final String QUERY_FIELD_TOP = "<top>";

	public static final String QUERY_FIELD_NUMBER = "<num>";

	public static final String QUERY_FIELD_TITLE = "<titl";

	public static final String QUERY_FIELD_DESCRIPTION = "<desc";

	public static final String QUERY_FIELD_NARRATIVE = "<narr";

	public static final String QUERY_FIELD_BOTTOM = "</top";


}
