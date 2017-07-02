package com.sonly.demo;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

/**
 *
 * @author archko
 */
public class IndexBuild {

	/**
	 * 建立索引
	 * @param indexDir 索引存放的目录
	 * @param dataDir 需要索引的目录或文件
	 */
	public static void index(File indexDir,File dataDir) {
		try {
			Analyzer analyzer=new StandardAnalyzer();
			//有第三个参数时,索引文件必须要已经存在
			IndexWriter indexWriter=new IndexWriter(indexDir,analyzer);

			long start=System.currentTimeMillis();

			indexDocs(indexWriter,dataDir);

			indexWriter.optimize();
			indexWriter.close();
			long end=System.currentTimeMillis();
			System.out.println("共花费"+(end-start)+"毫秒建立索引,索引文件存放在:"+indexDir.
					getCanonicalPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 索引目录,使用递归扫描所有的目录文件,然后全部索引.
	 * @param writer
	 * @param dataDir 要索引的目录名
	 */
	public static void indexDocs(IndexWriter writer,File dataDir) {
		if (dataDir.isDirectory()) {
			File[] dataFiles=dataDir.listFiles();
			if (dataFiles!=null) {
				for (File file:dataFiles) {
					indexDocs(writer,file);
				}
			}
		} else /*if (dataDir.getName().endsWith(".txt")||dataDir.getName().endsWith(
  ".htm")||dataDir.getName().endsWith(".html")||dataDir.getName().endsWith(
  ".java")) {*/ {
//上面是对特定的文件索引.比如TXT结尾的.
			indexFiles(dataDir,writer);
		}
	}

	/**
	 * 索引文件
	 * @param dataFile 要索引的文件名
	 * @param writer
	 */
	private static void indexFiles(File dataFile,IndexWriter writer) {
		try {
			Document document=new Document();
			Reader reader=new FileReader(dataFile);

			document.add(new Field("url",dataFile.getCanonicalPath(),Field.Store.YES,
					Field.Index.NO));
			//这里是文件的内容,从reader来的.
			document.add(new Field("contents",reader));
			//文件名就直接获得了,然后索引
			document.add(new Field("name",dataFile.getName(),Field.Store.YES,
					Field.Index.NO));
			writer.addDocument(document);
		} catch (Exception e) {
		}
	}
}
