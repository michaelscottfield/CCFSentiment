package preprocess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kevin.zhang.NLPIR;
import mutualinfo.Mutualinfo;

public class Client {
	public static void main(String args[]) {
		Mutualinfo info_cal = new Mutualinfo();
		Readexcel readfromexcel = new Readexcel();
		Dic dicgene = new Dic();
		Map<String, String> speeches = new HashMap<String, String>();
		BufferedWriter csvFileOutputStream = null;
		//File posdic = new File("F:\\projects\\eclipse-workspace\\CCFSentiment\\emotiondict.txt");
		File themedict1 = new File("F:\\projects\\eclipse-workspace\\CCFSentiment\\themedict.txt");
		File traindata = new File("F:/CCFDF/BDCI2017-taiyi/̩һָ��ѵ����.xlsx");
		File testdata = new File("F:/CCFDF/BDCI2017-taiyi/̩һָ��-���⼯.csv");//��������
		File dicfile = new File("F:\\projects\\eclipse-workspace\\CCFSentiment\\sentiment_dict.txt");
		File result = new File("F:\\projects\\eclipse-workspace\\CCFSentiment\\sentiment_result.csv");
		try {
			NLPIR testNLPIR = new NLPIR();
			String argu = ".";
			System.out.println("NLPIR_Init");
			if (testNLPIR.NLPIR_Init(argu.getBytes("GB2312"),1) == false)
			{
				System.out.println("Init Fail!");
				return;
			}
			List<Comment> comments = readfromexcel.readexcel(traindata);
			
			if(!result.exists() || !result.isFile()) {
				result.createNewFile();
				
			}
			if(!themedict1.exists() || !themedict1.isFile()) {
				//�����ɴʵ�
				
	            dicgene.themedic(comments);
	            dicgene.emotiondic(comments);
	            
	            System.out.println("dictionary generated");
			}
			else {
				System.out.println("dictionary already exists");
			}
			if(!dicfile.exists() || !dicfile.isFile()) {
				String filepaths[] = new String[2];
				filepaths[0] = "F:\\projects\\eclipse-workspace\\CCFSentiment\\themedict.txt";
				filepaths[1] = "F:\\projects\\eclipse-workspace\\CCFSentiment\\emotiondict.txt";
				dicgene.dic_merge(filepaths, 2);
			}
			String content;
			
			//�û��Զ���ʵ�Ĵ������
			int nCount = testNLPIR.NLPIR_ImportUserDict(("F:\\projects\\eclipse-workspace\\CCFSentiment\\sentiment_dict.txt").getBytes());
			List<Comment> testcomments = readfromexcel.readcsv(testdata);
			System.out.println("�Ѷ����������");
			
			String wordpspeech[];
			String strs[];
			
			
			String theme_write[];
			String emotion_write[];
			int polarities[];
			
			int write_wcnt = 0;
			double infos[];
			int info_count;		
			int polar_count;
			csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(result), "UTF-8"),1024);
			csvFileOutputStream.write("row_id" + "," + "content" + "," + "theme" + "," + "sentiment_word" + "," + "sentiment_anls" + "\n");
			for(Comment testcomment : testcomments) {
				content = testcomment.content;
				Set<String> themewords = new HashSet<String>(); 
				//System.out.println(themewords);
				Set<String> emowords = new HashSet<String>();
				theme_write = new String[10];
				emotion_write = new String[10];
				polarities = new int[10];
				byte nativeBytes[] = testNLPIR.NLPIR_ParagraphProcess(content.getBytes("utf8"), 1);
				String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "utf8");
				//System.out.println(nativeStr);
				wordpspeech = nativeStr.split(" ");
				//System.out.println(wordpspeech[0]);
				//System.out.println(wordpspeech.length);
				for(int i = 0; i < wordpspeech.length; i ++) {
					strs = wordpspeech[i].split("/");
					//������Ĵ�����ȷ�����Ϊ"negemo"
					//if(strs[0].equals("�����")) System.out.println(strs[1]);
					if(strs.length < 2) continue;
					//System.out.println(strs[1]);
					if(strs[1].equals("themew")) {
						//System.out.println(strs[0]); 
						//ʲô��û�����������̨��
						themewords.add(strs[0]);
					}
					else if(strs[1].equals("posemo") || strs[1].equals("neuemo") || strs[1].equals("negemo")) {
						emowords.add(strs[0]);
						speeches.put(strs[0], strs[1]);
						//System.out.println("shit");
						//System.out.println(strs[0]);
						//�����strs[0]�ǳ��٣���
					}
				}
				//System.out.println(themewords);
				//if(themewords == null) continue;
				write_wcnt = 0;
				
				for(String themeword : themewords) {
					//System.out.println(themewords);
					infos = new double[10];
					String temp = "";
					double max = 0;
					info_count = 0;
					//System.out.println(themeword);
					for(String emoword : emowords) {
						//��д��Ѿ��ܹ��������
						//System.out.println(emoword);
						infos[info_count] = info_cal.infoforwords(comments, themeword, emoword);
						//�кܶ໥��Ϣ����NaN??
						
						//System.out.println(infos[info_count]);
						//System.out.println(infos[info_count]);
						info_count += 1;
						if(infos[info_count - 1] > max && speeches.containsKey(emoword)) {
							max = infos[info_count - 1];
							temp = emoword;
							}
					}
					theme_write[write_wcnt] = themeword;
					//System.out.println(theme_write[write_wcnt]);
					if(temp == "") {
						emotion_write[write_wcnt] = null;
					}
					emotion_write[write_wcnt] = temp;
					write_wcnt += 1;
					//if(write_wcnt > 1) System.out.println("shit");
					//System.out.println(write_wcnt);
				}
				
				String tempp = testcomment.row_id + "," + content + ",";
				//System.out.println(write_wcnt);
				//write_wcntȫ��1��
				if(write_wcnt > 0) {
					for(int i = 0; i < write_wcnt; i++) {
						//accidentһ��Ҳû�������
						//if(theme_write[i].equals(null)) System.out.println("accident");
						//if(theme_write[i].equals("")) System.out.println("shit");
						tempp += theme_write[i] + ";";
						//theme_writeȫ��null?
						//System.out.println(theme_write[write_wcnt]);
					}
					
					//tempp = tempp.substring(0, tempp.length() - 1);
					
					//System.out.println(tempp);
					tempp += ",";
					//polar_count = 0;
					for(int i = 0; i < write_wcnt; i ++) {
						//�����emotion_writeȫ��null??
						//System.out.println(emotion_write[i]);
						//if(emotion_write[i].equals("")) System.out.println("no emotion");
						if(emotion_write[i].equals("")) {
							tempp += "NULL" + ";";
							
						}
						else {
							tempp += emotion_write[i] + ";";
						}
						//����info_polar������ʱ�����nullpointerexception
						if(emotion_write[i].equals("")) {
							polarities[i] = -2;
						}
						if(!speeches.containsKey(emotion_write[i])) continue;
						else if(speeches.get(emotion_write[i]).equals("posemo")) polarities[i] = 1;
						else if(speeches.get(emotion_write[i]).equals("neuemo")) polarities[i] = 0;
						else if(speeches.get(emotion_write[i]).equals("negemo")) polarities[i] = -1;
						
						//polarities[i] = info_cal.info_polar(comments, emotion_write[i]);
						//System.out.println(polarities[i]);
					}
					//tempp = tempp.substring(0, tempp.length() - 1);
					tempp += ",";
					
					for(int i = 0; i < write_wcnt; i ++) {
						if(polarities[i] > -2) {
						tempp += polarities[i] + ";";
						}
						else {
							tempp += "NULL;";
						}
					}
					//tempp = tempp.substring(0, tempp.length() - 1);
				}
				else {//û��ʶ����κ�����ʣ���Ҫ�����д�
					int j = 0;
					int len = emowords.size();
					for(int i = 0; i< len; i ++) {
						tempp += "NULL" + ";";
					}
					
					//tempp = tempp.substring(0, tempp.length() - 1);
					tempp += ",";
					for(String emoword : emowords) {
						if(!speeches.containsKey(emoword)) continue;
						else if(speeches.get(emoword).equals("posemo")) polarities[j] = 1;
						else if(speeches.get(emoword).equals("neuemo")) polarities[j] = 0;
						else if(speeches.get(emoword).equals("negemo")) polarities[j] = -1;
						tempp += emoword + ";";
						j += 1;
					}
					//tempp = tempp.substring(0, tempp.length() - 1);
					tempp += ",";
					for(int i = 0; i < j; i ++) {
						if(polarities[i] > -2) {
						tempp += polarities[i] + ";";
						}
						else {
							tempp += "NULL;";
						}
					}
					//tempp = tempp.substring(0, tempp.length() - 1);
				}
				//System.out.println(tempp);
				csvFileOutputStream.write(tempp + "\n");
				//�����������дʼ���ȫ��0????
			}
			csvFileOutputStream.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
	}
}
