package com.trecboosters.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.FlattenGraphFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;


public class CustomDocumentAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String s) {
    	Tokenizer stdTokenizer = new ClassicTokenizer();
        TokenStream tokenStream = new ClassicFilter(stdTokenizer);
        tokenStream = new ASCIIFoldingFilter(tokenStream);
        tokenStream = new LengthFilter(tokenStream, 3, 25);
        tokenStream = new LowerCaseFilter(tokenStream);
        tokenStream = new StopFilter(tokenStream, EnglishAnalyzer.getDefaultStopSet());
        tokenStream = new FlattenGraphFilter(new SynonymGraphFilter(tokenStream, createSynonymMap(), true));
        tokenStream = new KStemFilter(tokenStream);
        tokenStream = new PorterStemFilter(tokenStream);
        return new TokenStreamComponents(stdTokenizer, tokenStream);
    }
    
    private SynonymMap createSynonymMap() {
        SynonymMap synMap = new SynonymMap(null, null, 0);
        BufferedReader countries = null;

        try 
        {
        	countries = new BufferedReader(new FileReader(new File("." + File.separator + "documentSet"+ File.separator + "countries.txt")));
            final SynonymMap.Builder builder = new SynonymMap.Builder(true);
            String country = countries.readLine();
            while (country != null)
            {
                builder.add(new CharsRef("country"), new CharsRef(country), true);
                builder.add(new CharsRef("countries"), new CharsRef(country), true);
                country = countries.readLine();
            }
            synMap = builder.build();
            
        } 
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getLocalizedMessage() + "occurred when trying to create synonym map");
        }
        finally 
        {
        	if(countries != null)
        	{
        		try
        	
        		{
					countries.close();
				} 
        		catch (IOException e) 
        		{
					e.printStackTrace();
				}
        	}
        }
        return synMap;
    }

}
