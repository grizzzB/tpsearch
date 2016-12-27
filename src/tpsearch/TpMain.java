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


public class TpMain {

	public static void main(String args[]) throws Exception{
		
		String path2 = "D:/MCMT/istream/pehnotype_byKAIST";

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

					querySet.add(lineArr[2].trim().toLowerCase());

					//System.out.println(line)
				}
		}
		
		System.out.println( querySet.size() );
		
	
		
		
		String path = "D:/MCMT/istream/res_590.txt";
		
		FileReader fr = new FileReader(new File(path));
		BufferedReader br = new BufferedReader (fr);
		
		
		String indexPath = "index_selected";
		Directory dir = FSDirectory.open(Paths.get(indexPath));
		
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

		IndexWriter writer = new IndexWriter(dir, iwc);
		
		int itr = 0;
		while(true){
			line = br.readLine();
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

		}

		System.out.println(itr );
		writer.close();
	}

}
