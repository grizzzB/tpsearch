package tpsearch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class TpSearch {

	public static void main(String args[]) throws Exception{

		String path = "D:/MCMT/istream/total_vocab_tb_origin2.txt";;
		String path2 = "D:/MCMT/istream/pehnotype_byKAIST/";

		Set<String> querySet = new HashSet<String>();

		String line ="";
		File file = new File(path2);
		
		for( File f : file.listFiles() ){
				System.out.println(f.getAbsolutePath());

				FileReader fr_UML = new FileReader(f);
				BufferedReader br_UML = new BufferedReader (fr_UML);
				
				while(true){
					line = br_UML.readLine();
					if(line == null) break;
					String lineArr[] = line.split("\t");

					if(lineArr.length <= 1) continue;
					if(lineArr[2].contains("]")) {
						System.out.println( lineArr[2] );
						lineArr[2] = lineArr[2].substring( lineArr[2].indexOf("]")+1 );
					}

					querySet.add(lineArr[2].trim().toLowerCase());

					//System.out.println(line)
				}
			
		}

		
		System.out.println( querySet.size() );
		/*
		for(String query : querySet){
			System.out.println(query);
		}*/
		

		String index ="index_selected";
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();

		String path3 = "D:/MCMT/istream/total_vocab_tb3.txt";
		FileWriter fw = new FileWriter(new File(path3));
		BufferedWriter bw = new BufferedWriter (fw);

		for(String querystr : querySet){
			if(querystr.equals("")) continue;
			Query q = new QueryParser("contents", analyzer).parse(querystr);

			PhraseQuery.Builder builder = new PhraseQuery.Builder();
			
			String[] words = querystr.split(" ");
			
			int ct = 0;
			
			for (String word : words) {
			    //query.add(new Term("contents", word));
			    builder.add(new Term("contents", word), ct);
			    ct++;
				
			}			
			PhraseQuery pq = builder.build();
			
			TopDocs docs = searcher.search(pq, 10000);
			ScoreDoc[] hits = docs.scoreDocs;

			for(int i=0;i<hits.length;++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				String id = d.get("id");
				String type = d.get("type");
				String contents = d.get("contents");
				int indexStart = contents.indexOf(querystr);
				int indexEnd =  indexStart + querystr.length();
				if(indexStart < 0) continue;
				if(contents.contains(querystr)){
					System.out.println(contents);
					System.out.println(id + "\t" + type + "\t" + indexStart + "\t" + indexEnd + "\t" + querystr);
					System.out.println();
					
					bw.write(id + "\t" + type + "\t" + indexStart + "\t" + indexEnd + "\t" + querystr);
					bw.newLine();
				}

			}
		}
		bw.close();
		fw.close();


	}

}
