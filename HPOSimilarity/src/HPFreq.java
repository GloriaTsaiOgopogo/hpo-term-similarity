import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;


public class HPFreq {



	//存放每个hpo的后代节点
	static HashMap<String,HashSet<String>> hpoDesc = new HashMap<String, HashSet<String>>();

	static HashSet<String> getDesc(String hpo, HashMap<String,HashSet<String>> hpoChild){
		HashSet<String> desc = new HashSet<String>();
		HashSet<String> child = hpoChild.get(hpo);
		if(child!=null){
			desc.addAll(child);
			for(String s:child){
				desc.addAll(getDesc(s,hpoChild));
			}
		}
		hpoDesc.put(hpo,desc);
		return desc;
	}


	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String file1 = "output/PG.txt";
		String file3 = "input/hpo_pair.txt";
		String file4 = "input/hpo_term.txt";
		String outfile = "output/HPOFreq.txt";
		String outfile1 = "output/hpo_term50.txt";

		BufferedReader br1 = new BufferedReader(new FileReader(new File(file1)));
		BufferedReader br3 = new BufferedReader(new FileReader(new File(file3)));
		BufferedReader br4 = new BufferedReader(new FileReader(new File(file4)));
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outfile)));
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(new File(outfile1)));

		//hpoAnnoNum存放pg关联文件中每个hpo被标注的次数，部分hpo可能在hp.obo中找不到，如果hpo不存在表示未被标注过
		HashMap<String,Integer> hpoAnnoNum = new HashMap<String, Integer>();
		String line = "";
		while((line=br1.readLine())!=null){
			String hpo = line.split("\t")[0];
			if(hpoAnnoNum.get(hpo)!=null){
				int count = hpoAnnoNum.get(hpo);
				count++;
				hpoAnnoNum.put(hpo,count);
			}else{
				hpoAnnoNum.put(hpo, 1);
			}
		}


		//hpoChild存放每个hpo的孩子节点，hpo找不到表示叶节点无孩子
		HashMap<String,HashSet<String>> hpoChild = new HashMap<String, HashSet<String>>();
		line = "";
		br3.readLine();
		while((line=br3.readLine())!=null){
			String child = line.split(" : ")[0];
			String par = line.split(" : ")[1];
			if(hpoChild.get(par)!=null){
				hpoChild.get(par).add(child);
			}else{
				HashSet<String> c = new HashSet<String>();
				c.add(child);
				hpoChild.put(par, c);
			}
		}
		getDesc("HP:0000001",hpoChild);

		line = "";
		DecimalFormat df= new DecimalFormat("######0.000000");  

		HashSet<String> hpoTerms = new HashSet<String>();
		while((line=br4.readLine())!=null){
			hpoTerms.add(line);
			HashSet<String> descs = hpoDesc.get(line);
			int count = 0;
			if(hpoAnnoNum.get(line)!=null){
				count = hpoAnnoNum.get(line);
			}
			for(String hp:descs){
				if(hpoAnnoNum.get(hp)!=null){
					count += hpoAnnoNum.get(hp);
				}
			}
			bw.write(line + " - " + df.format(count/456408.0) + "\r\n");
		}
		bw.close();

		for(Entry<String, Integer> entry:hpoAnnoNum.entrySet()){
			if(entry.getValue()>=50){
				//修改，PG关联里的HPO term有可能在hp.obo中找不到，删除这些没有的HPO
				if(hpoTerms.contains(entry.getKey())){
					bw1.write(entry.getKey()+"\r\n");
				}
			}
		}
		bw1.close();

	}

}
