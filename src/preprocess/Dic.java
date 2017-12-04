package preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.Map;
import java.util.Map.Entry;
public class Dic {//����ѵ���������ɴʵ�
	/**
	 * @param ѵ������
	 * @return ������Ｏ��
	 */
	public void themedic(List<Comment> comments) {
		Set<String> themedict = new HashSet<String>();
		for(Comment comment : comments) {
			if (comment.theme == null) continue;
			String themes[] = comment.theme.split(";");
			for(String theme : themes) {
				if(!themedict.contains(theme))
					themedict.add(theme);
			}
		}
		File themedict1 = new File("F:\\projects\\eclipse-workspace\\CCFSentiment\\themedict.txt");
		if(themedict1.exists()&&themedict1.isFile()) {
			
		}
		else {
			try {
				//�����ļ�
				themedict1.createNewFile();
				System.out.println("����themedict.txt");
			}catch(IOException e) {
				System.out.println("themedic�ļ�����ʧ�ܣ�������Ϣ:" + e.getMessage());
				return;
			}
		}
		try {
			PrintWriter pw = new PrintWriter(themedict1);
			for(String theme : themedict) 
			{
				pw.write(theme + " themew\n");
			}
			pw.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * @param ѵ������
	 * @return
	 */
	public void emotiondic(List<Comment> comments){
		//Set<String> posemotiondict = new HashSet<String>();
		double rate;
		//������ֵ�Զ���
		Map<String, Double> posemorating = new HashMap<String, Double>();
		Map<String, Double> neuemorating = new HashMap<String, Double>();
		Map<String, Double> negemorating = new HashMap<String, Double>();
		for(Comment comment : comments) {
			if(comment.sentiment_word == null) continue;
			String emotions[] = comment.sentiment_word.split(";");
			String rating[] = comment.sentiment_anls.split(";");
			for(int i = 0; i < emotions.length; i++) {
				rate = Double.valueOf(rating[i]);
				if(rate == 1.0) {
					if(posemorating.containsKey(emotions[i]))//�Ѿ����и���йؼ��� 
					{
						posemorating.put(emotions[i], posemorating.get(emotions[i]) + 1.0);
					}
					else {
						posemorating.put(emotions[i], 1.0);
					}
			    }
				else if(rate == 0.0) {
					if(neuemorating.containsKey(emotions[i]))//�Ѿ����и���йؼ��� 
					{
						neuemorating.put(emotions[i], neuemorating.get(emotions[i]) + 1.0);
					}
					else {
						neuemorating.put(emotions[i], 1.0);
					}
			    }
				else if(rate == -1.0) {
					if(negemorating.containsKey(emotions[i]))//�Ѿ����и���йؼ��� 
					{
						negemorating.put(emotions[i], negemorating.get(emotions[i]) + 1.0);
					}
					else {
						negemorating.put(emotions[i], 1.0);
					}
			    }
			}
			
		}
		
	//�������ɵ����ݼ����ɴʵ�
	File posdic = new File("F:\\projects\\eclipse-workspace\\CCFSentiment\\emotiondict.txt");
	if(posdic.exists()&&posdic.isFile()) {
		
	}
	else {
		try {
			//�����ļ�
			posdic.createNewFile();
			System.out.println("����emotiondict.txt");
		}catch(IOException e) {
			System.out.println("emotiondic�ļ�����ʧ�ܣ�������Ϣ:" + e.getMessage());
			return;
		}
	}
	String word;
	double count;
	try {
		PrintWriter pw = new PrintWriter(posdic);
		Set<Entry<String, Double>>entrySet=posemorating.entrySet();
		for(Entry<String, Double> entry:entrySet){
			word = entry.getKey();
			count = entry.getValue();
			pw.write(word + " posemo\n");
		}
		Set<Entry<String, Double>>entrySet2=neuemorating.entrySet();
		for(Entry<String, Double> entry:entrySet2) {
			word = entry.getKey();
			count = entry.getValue();
			pw.write(word + " neuemo\n");
		}
		Set<Entry<String, Double>>entrySet3=negemorating.entrySet();
		for(Entry<String, Double> entry:entrySet3) {
			word = entry.getKey();
			count = entry.getValue();
			pw.write(word + " negemo\n");
		}
		pw.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

	return;
	}
	
	/*�Դʵ���кϲ�
	 * 
	 */
	public void dic_merge(String filepaths[], int num) {
		BufferedReader br = null;
		
		int word_count = 0;
		String strs[];
		String line;
		try {
			for(int i = 0; i < num; i++) {
				File single_dic = new File(filepaths[i]);
				br = new BufferedReader(new FileReader(single_dic));
				while((line = br.readLine()) != null) {
					strs = line.split(" ");
					word_count += 1;
				}
			}
			String words[] = new String[word_count];
			String speech[] = new String[word_count];
			word_count = 0;
			for(int i = 0; i < num; i++) {
				File single_dic = new File(filepaths[i]);
				br = new BufferedReader(new FileReader(single_dic));
				while((line = br.readLine()) != null) {
					strs = line.split(" ");
					words[word_count] = strs[0];
					speech[word_count] = strs[1];
					word_count += 1;
				}
			}
			File dicfile = new File("F:\\projects\\eclipse-workspace\\CCFSentiment\\sentiment_dict.txt");
			PrintWriter pw = new PrintWriter(dicfile);
			for(int i =0; i < word_count; i ++) {
				pw.write(words[i] + " " + speech[i] + "\n");
			}
			pw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
