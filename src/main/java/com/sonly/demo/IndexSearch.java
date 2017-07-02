package com.sonly.demo;

import java.io.File;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author archko
 */
public class IndexSearch {

	public void search(String keyWords,File indexDir) {
		if (!indexDir.exists()) {
			return;
		}
		try {
			FSDirectory fSDirectory=FSDirectory.getDirectory(indexDir);
			IndexSearcher indexSearcher=new IndexSearcher(fSDirectory);
			//StandardAnalyzer是它自带的分词包,你可以下载自己要的.就在这里替换就行了.
			QueryParser parser=new QueryParser("contents",new StandardAnalyzer());
			Query query=parser.parse(keyWords);
			Hits hits=indexSearcher.search(query);

			System.out.println("共有"+indexSearcher.maxDoc()+"条索引，命中"+hits.length()+"条");
			for (int i=0;i<hits.length();i++) {
				int docId=hits.id(i);
				//这里的URL,NAME跟你建立索引时要一样
				String url=hits.doc(i).get("url");
				//String name=hits.doc(i).get("name");
				System.out.println("url:**"+url);
			}
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		//IndexBuild.index(new File("E:\\lucene\\index"),new File("E:\\lucene\\src"));
		IndexSearch search=new IndexSearch();
		search.search("C01=自生",new File("E:\\lucene\\index"));
	}
}
