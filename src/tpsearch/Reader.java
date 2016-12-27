package tpsearch;

import java.net.*;
import java.io.*;

public class Reader {
    public static void main(String[] args) throws Exception {
       
       
          String fileName = "C:/Users/hjB/workspace/tpsearch/temp.txt";
          int NameCounter = 1;
          int Abracadabra = 0;
          
          BufferedReader br = new BufferedReader(new FileReader(fileName));
          String line = null;
          
          
          int TIMEOUT_VALUE = 100000;
          int counter = 0;
          
          
          //for(int i = 47291 ; i <=27450000 ; i++){
          while((line = br.readLine()) != null){
             try {
            	++counter;
            	System.out.println("Current PMID : " + line);
            	System.out.println("counter : " + String.valueOf(counter));
            	if(line.toLowerCase().trim().equals("end")){
            		break;
            	}
                BufferedWriter out = new BufferedWriter(new FileWriter("C:/Users/hjB/workspace/tpsearch/output.txt", true));
                /*
                if(file.length() >  1000000000){
                   ++NameCounter;
                   fileName = "collection_"+String.valueOf(NameCounter)+".txt";
                }
                */
                String PMID = String.valueOf(line);
                
                
                URL testUrl = new URL("https://www.ncbi.nlm.nih.gov/CBBresearch/Lu/Demo/RESTful/tmTool.cgi/BioConcept/"+PMID+"/PubTator/");
                //BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
   
                 long start = System.nanoTime();
   
                 URLConnection testConnection = testUrl.openConnection();
                 testConnection.setConnectTimeout(TIMEOUT_VALUE);
                 testConnection.setReadTimeout(TIMEOUT_VALUE);
                 BufferedReader in = new BufferedReader(new InputStreamReader(testConnection.getInputStream()));
                 
                 String regt = PMID+"|t|";
                 String rega = PMID+"|a|";
                 String inputLine;
                 int notemptyCounter = 0;
                 while ((inputLine = in.readLine()) != null){
                    if(!inputLine.isEmpty()){
                       if(!inputLine.trim().equals("")){
                          System.out.println(inputLine);
                          out.write(inputLine);
                          out.newLine();
                       }
                    }
                    if(inputLine.contains(regt)){
                       ++notemptyCounter;
                    }
                    if(inputLine.contains(rega)){
                       ++notemptyCounter;
                    }
                 }
                 
                 if(notemptyCounter > 0){
                    out.write("Abracadabra");
                    out.newLine();
                    System.out.println("Abracadabra");
                    ++Abracadabra;
                 }
                 in.close();
                 out.close();
                 
                 
                 
             }catch (SocketTimeoutException e) {
                System.out.println("More than " + TIMEOUT_VALUE + " elapsed.");
             }
             catch(IOException ex){
                 System.out.println (ex.toString());
             }

          
          }          
          System.out.println("Total PMIDs : "+ String.valueOf(Abracadabra));
       }
}