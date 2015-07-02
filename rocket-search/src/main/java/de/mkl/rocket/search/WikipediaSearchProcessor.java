package de.mkl.rocket.search;

import de.mkl.rocket.common.WikipediaAnalyzer;
import de.mkl.rocket.common.IndexedWikipediaField;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class WikipediaSearchProcessor implements SearchProcessor{

    private IndexSearcher indexSearcher;

    private static final int RESULT_COUNT = 10;

    public WikipediaSearchProcessor(File indexDirectory) throws IOException {
        Directory dir = MMapDirectory.open(indexDirectory);
        IndexReader indexReader  = DirectoryReader.open(dir);
        indexSearcher = new IndexSearcher(indexReader);
    }

    @Override
    public TopGroups executeWordSearch(String word) throws Exception {

        GroupingSearch groupingSearch = getGroupingSearch();
        Query query = getWordQuery(word);
        TopGroups topGroups = groupingSearch.search(indexSearcher, query, 0, RESULT_COUNT);
        for(GroupDocs groupDoc: topGroups.groups){
            for(ScoreDoc scoreDoc: groupDoc.scoreDocs){
                printResult(scoreDoc, false);
            }
            System.out.println();
        }
        return topGroups;
    }

    private GroupingSearch getGroupingSearch(){

        GroupingSearch groupingSearch = new GroupingSearch(IndexedWikipediaField.REDIRECT.getFieldName());
        groupingSearch.setGroupSort(Sort.RELEVANCE);
        groupingSearch.setIncludeScores(true);
        groupingSearch.setGroupDocsLimit(RESULT_COUNT);
        return groupingSearch;
    }

    @Override
    public TopDocs executeContributorSearch(String contributor) throws Exception {

        Query query = getContributorQuery(contributor);
        TopDocs topDocs = indexSearcher.search(query, RESULT_COUNT);
        printTotalHits(topDocs.totalHits);

        for(ScoreDoc scoreDoc: topDocs.scoreDocs){
            printResult(scoreDoc, true);
        }
        return topDocs;
    }

    private Query getWordQuery(String word) throws ParseException {

        Analyzer analyzer = new WikipediaAnalyzer(Version.LUCENE_43);
        QueryParser parser = new QueryParser(Version.LUCENE_43, IndexedWikipediaField.TEXT.getFieldName(), analyzer);
        return parser.parse(word);
    }

    private Query getContributorQuery(String contributor) throws ParseException {

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
        QueryParser parser = new QueryParser(Version.LUCENE_43, IndexedWikipediaField.CONTRIBUTOR.getFieldName(), analyzer);
        return parser.parse(contributor);
    }

    private void printResult(ScoreDoc scoreDoc, boolean newLine) throws IOException {
        String line = scoreDoc.score + " " + indexSearcher.doc(scoreDoc.doc).getField(IndexedWikipediaField.TITLE.getFieldName()).stringValue() + " ";
        if(newLine){
            System.out.println(line);
        } else{
            System.out.print(line);
        }
    }

    private void printTotalHits(int totalCount){
        System.out.println("total hits " + totalCount);
    }
}
