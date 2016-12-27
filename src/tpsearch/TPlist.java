package tpsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class TPlist {

	public static void main(String args[]) throws Exception{

		String path = "D:/MCMT/istream/total_vocab_tb.txt";
		String path2 = "D:/MCMT/istream/T_Name.txt";


		FileReader fr = new FileReader(new File(path));
		BufferedReader br = new BufferedReader (fr);

		Set<String> querySet = new HashSet<String>();

		String line ="";
		while(true){
			line = br.readLine();
			if(line == null) break;
			String lineArr[] = line.split("\t");

			if(lineArr.length <= 1) continue;

			querySet.add(lineArr[0].trim());

			String lineArr2[] = lineArr[1].split("|");
			if(!lineArr2[0].trim().equals("NULL")) querySet.add(lineArr2[0].trim());
			if(!lineArr2[1].trim().equals("NULL")) querySet.add(lineArr2[1].trim());

			//System.out.println(line);

		}
		for(String query : querySet){
			//System.out.println(query);
		}

		String indexPath = "index";
		Directory dir = FSDirectory.open(Paths.get(indexPath));
		
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

		IndexWriter writer = new IndexWriter(dir, iwc);


		File file = new File( path2 );
		FileReader fr2 = new FileReader( file );
		BufferedReader br2 = new BufferedReader (fr2);
		
		Document doc = new Document();
		String content = line.substring(line.indexOf("\t")+1);
		System.out.println(content);
		
		
		/*
		for(File f : file.listFiles()){
			System.out.println(f.getAbsolutePath());

			FileReader fr2 = new FileReader(f);
			BufferedReader br2 = new BufferedReader (fr2);

			while(true){
				line = br2.readLine();
				if(line == null) break;
				if(line.contains("\t") || line.contains("Abracadabra")) continue;

				if(!line.contains("|")) continue;
				String id = line.substring(0, line.indexOf("|"));
						
				//System.out.println(id);

				String type = line.substring(line.indexOf("|")+1, line.indexOf("|")+2);
				//System.out.println(type);

				String content = line.substring(line.indexOf("|")+3);
				//System.out.println(Field.Store.YES);

				Document doc = new Document();

				doc.add(new StringField("id", id, Field.Store.YES));
				doc.add(new TextField("contents",content,Field.Store.YES));
				doc.add(new StringField("type",type,Field.Store.YES));
				
				writer.addDocument(doc);


				//System.out.println(line);

			}



		}*/
		
		
		writer.close();




	}

}
