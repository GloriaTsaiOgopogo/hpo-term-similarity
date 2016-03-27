package com.gene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class GeneCount {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String file1 = "input/BIOGRID-ORGANISM-Homo_sapiens-3.4.134.tab2.txt";
		String file2 = "output/PG.txt";
		
		String outfile1 = "output/GeneTerms.txt";
		String outfile2 = "output/PPI.txt";
		
		BufferedReader br1 = new BufferedReader(new FileReader(new File(file1)));
		BufferedReader br2 = new BufferedReader(new FileReader(new File(file2)));
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(new File(outfile1)));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File(outfile2)));
		
		HashSet<String> ppiGeneTerm = new HashSet<String>();
		String line = "";
		br1.readLine();
		while((line=br1.readLine())!=null){
			String gene1 = line.split("\t")[1];
			String gene2 = line.split("\t")[2];
			ppiGeneTerm.add(gene1);
			ppiGeneTerm.add(gene2);
			bw2.write(gene1 + "\t" + gene2 + "\r\n");
		}
		br1.close();
		bw2.close();
		System.out.println(ppiGeneTerm.size());
		
		HashSet<String> PGGeneTerms = new HashSet<String>();
		line = "";
		while((line=br2.readLine())!=null){
			String gene = line.split("\t")[1];
			PGGeneTerms.add(gene);
		}
		br2.close();
		System.out.println(PGGeneTerms.size());
		
		ppiGeneTerm.addAll(PGGeneTerms);
		System.out.println(ppiGeneTerm.size());
		
		for(String g : ppiGeneTerm){
			bw1.write(g + "\r\n");
		}
		bw1.close();
		
	}

}
