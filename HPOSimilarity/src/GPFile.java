import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;


public class GPFile {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String file1 = "input/ALL_SOURCES_ALL_FREQUENCIES_phenotype_to_genes.txt";
		String file2 = "input/ALL_SOURCES_ALL_FREQUENCIES_genes_to_phenotype.txt";
		String outfile = "output/PG.txt";
		
		BufferedReader br1 = new BufferedReader(new FileReader(new File(file1)));
		BufferedReader br2 = new BufferedReader(new FileReader(new File(file2)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outfile)));
		
		String line = "";
		HashSet<String> PGs = new HashSet<String>();
		br1.readLine();
		while((line=br1.readLine())!=null){
			String[] tmps = line.split("\t");
			String PG = tmps[0] + "\t" + tmps[2];
			PGs.add(PG);
		}
		br1.close();
		line = "";
		br2.readLine();
		while((line=br2.readLine())!=null){
			String[] tmps = line.split("\t");
			String PG = tmps[3] + "\t" + tmps[0];
			PGs.add(PG);
		}
		br2.close();
		
		for(String s : PGs){
			bw.write(s + "\r\n");
		}
		bw.close();
		
	}

}
