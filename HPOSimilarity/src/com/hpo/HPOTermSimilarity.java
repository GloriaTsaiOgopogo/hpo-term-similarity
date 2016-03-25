package com.hpo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class HPOTermSimilarity {
	
	static ArrayList<String> array2List(String[] tmp){
		ArrayList<String> tmps = new ArrayList<String>();
		for(String str:tmp){
			tmps.add(str);
		}
		return tmps;
	}
	
	static HashSet<String> getCommonAncestors(String hpo1, String hpo2, ArrayList<ArrayList<String>> hpoTree){
		HashSet<String> res1 = new HashSet<String>();
		HashSet<String> res2 = new HashSet<String>();

		for(ArrayList<String> branch : hpoTree){
			if(branch.indexOf(hpo1)!=-1){
				for(int i=0;i<branch.indexOf(hpo1);i++){
					res1.add(branch.get(i));
				}
			}
			if(branch.indexOf(hpo2)!=-1){
				for(int i=0;i<branch.indexOf(hpo2);i++){
					res2.add(branch.get(i));
				}
			}
		}
		res1.retainAll(res2);
		return res1;
	}
	
	static double getGOPairSim(String hpo1, String hpo2, ArrayList<ArrayList<String>> hpoTree,
			HashMap<String,Double> hpoFreq){
		HashSet<String> commonAns = getCommonAncestors(hpo1, hpo2, hpoTree);
		double freq=1;
		Iterator<String> it = commonAns.iterator();
		while(it.hasNext()){
			String comAns = it.next();
			if(hpoFreq.get(comAns)!=0){
				freq = Math.min(freq, hpoFreq.get(comAns));
			}
		}
		return -Math.log(freq);
//		return 2*Math.log(freq)/(Math.log(hpoFreq.get(hpo1))+Math.log(hpoFreq.get(hpo2)));
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 
		String file1 = "input/hpo_tree.txt";
		String file2 = "output/hpo_term50.txt";
		String file3 = "output/HPOFreq.txt";
		String ofile = "output/HPOSim50.txt";
		
		BufferedReader br1 = new BufferedReader(new FileReader(new File(file1)));
		BufferedReader br2 = new BufferedReader(new FileReader(new File(file2)));
		BufferedReader br3 = new BufferedReader(new FileReader(new File(file3)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(ofile)));
		
		//存放所有11479个hpo中标注大于等于50的hpo term
		ArrayList<String> hpoTerms = new ArrayList<String>();
		String line = "";
		while((line=br2.readLine())!=null){
			hpoTerms.add(line);
		}
		br2.close();
		
		//存放11479个hpo每个hpo term的p(term)
		HashMap<String,Double> hpoFreq = new HashMap<String, Double>();
		line = "";
		while((line=br3.readLine())!=null){
			String hpo = line.split(" - ")[0];
			double fre = Double.parseDouble(line.split(" - ")[1]);
			hpoFreq.put(hpo, fre);
		}
		br3.close();
		
		//存放hpo树结构
		ArrayList<ArrayList<String>> hpoTree = new ArrayList<ArrayList<String>>();
		line = "";
		while((line=br1.readLine())!=null){
			ArrayList<String> str = array2List(line.split("\t"));
			hpoTree.add(str);
		}
		br1.close();
		
		DecimalFormat df= new DecimalFormat("######0.00");  
		int c = 0;
		for(int i=0;i<hpoTerms.size();i++){
			for(int j=i+1;j<hpoTerms.size();j++){
				double sim = getGOPairSim(hpoTerms.get(i),hpoTerms.get(j),hpoTree,hpoFreq);
				bw.write(hpoTerms.get(i) + "\t" + hpoTerms.get(j)+ "\t" + df.format(sim) + "\r\n");
				System.out.println(c);
				c++;
			}
		}
		bw.close();
		
	}

}
