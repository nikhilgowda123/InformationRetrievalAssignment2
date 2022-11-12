package com.trecboosters.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;

public class CustomDocumentAnalyzer_WhiteTokenizer extends Analyzer
{

	@Override
	protected TokenStreamComponents createComponents(String fieldName)
	{
		WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
		
        TokenStream tokenstream = new LowerCaseFilter(tokenizer);
        tokenstream = new StopFilter(tokenstream, EnglishAnalyzer.getDefaultStopSet());
        tokenstream = new LengthFilter(tokenstream, 2, 20);
        tokenstream = new PorterStemFilter(tokenstream);

        return new TokenStreamComponents(tokenizer, tokenstream);
	}


}
