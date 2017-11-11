package test;

import kevin.zhang.NLPIR;

import java.util.*;
import java.io.*;

public class TestNLPIR {

	public static void main(String[] args) throws Exception
	{
		try
		{
			String sInput = "�Ż�ƽ�Ƴ���NLPIR�ִ�ϵͳ������ICTCLAS2013�������´�ʶ�𡢹ؼ�����ȡ��΢���ִʹ��ܡ�";

			//����Ӧ�ִ�
			test(sInput);		
			
		}
		catch (Exception ex)
		{
		} 


	}

	public static void test(String sInput)
	{
		try
		{
			NLPIR testNLPIR = new NLPIR();
	
			String argu = ".";
			System.out.println("NLPIR_Init");
			if (testNLPIR.NLPIR_Init(argu.getBytes("GB2312"),1) == false)
			{
				System.out.println("Init Fail!");
				return;
			}
			
					//�����û��ʵ�ǰ
			byte nativeBytes[] = testNLPIR.NLPIR_ParagraphProcess(sInput.getBytes("utf8"), 1);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "utf8");
	
			System.out.println("�ִʽ��Ϊ�� " + nativeStr);

	
			File srcdata = new File("F:/projects/DataSet/TaobaoSentiment/TaobaoData/attempt_201303052028_4242712_m_000003_0");
			//��ʼ���ִ����
			String argu1 = "F:/projects/DataSet/TaobaoSentiment/TaobaoData/newworddata";
			String argu2 = "F:/projects/DataSet/TaobaoSentiment/TaobaoData/newwordres.txt";
			
			File textdata = new File(argu1);
			FileOutputStream out = new FileOutputStream(textdata);
			PrintStream p = new PrintStream(out);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					new FileInputStream(srcdata), "utf8"));
			String s = "";
			int line = 0;
			while((s = rd.readLine())!=null) {
				if(++line >= 10000) break;
				String text = s.split("\1")[1];
				p.println(text);
			}
			p.close();
			out.close();
			rd.close();
		
			nativeBytes  =testNLPIR.NLPIR_GetFileNewWords(argu1.getBytes("utf8"),50,true);
			//����Ǵ����ڴ棬���Ե���testNLPIR.NLPIR_GetNewWords
			nativeStr = new String(nativeBytes, 0, nativeBytes.length, "utf8");
			System.out.println("�´�ʶ����Ϊ�� " + nativeStr);
			
			nativeBytes  =testNLPIR.NLPIR_GetFileKeyWords(argu1.getBytes("GB2312"),50,true);
			//����Ǵ����ڴ棬���Ե���testNLPIR.NLPIR_GetKeyWords
			nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
			System.out.println("�ؼ���ʶ����Ϊ�� " + nativeStr);
			
			
			
			testNLPIR.NLPIR_FileProcess(argu1.getBytes("GB2312"), argu2.getBytes("GB2312"), 1);
	
			testNLPIR.NLPIR_NWI_Start();
			testNLPIR.NLPIR_NWI_AddFile(argu1.getBytes("GB2312"));
			
			testNLPIR.NLPIR_NWI_Complete();
			
			nativeBytes= testNLPIR.NLPIR_NWI_GetResult(true);
			nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
	
			System.out.println("�´�ʶ���� " + nativeStr);
		
			testNLPIR.NLPIR_NWI_Result2UserDict();//�´�ʶ����
			argu2 = "F:/projects/DataSet/TaobaoSentiment/TaobaoData/newwordres2.txt";
			testNLPIR.NLPIR_FileProcess(argu1.getBytes("GB2312"), argu2.getBytes("GB2312"), 1);
	
			testNLPIR.NLPIR_Exit();
		}
		catch (Exception ex)
		{
		} 
}
}
 
