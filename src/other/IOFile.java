package other;

import java.io.*;

import java.util.*;
import kernel.Instruction;
import kernel.Job;

public class IOFile {  //��ҵ�ļ���Ϣ����
	public static StringBuffer data;  //���������Ϣ
	public IOFile() {}
	
	public static List<Job> ReadJobsList() {   //��ȡ��ҵ�б��ļ�����ҵ������
		try {
			File f = new File("test\\19319229-jobs-input.txt");
			FileInputStream fis = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String str=null;
			List<Job> jobs = new ArrayList<Job>();
			
			br.readLine(); //����һ�б����ȥ
			
			while((str=br.readLine())!=null) {
				String[] strs = new String[4];
				strs = str.split(",");
				Job job = new Job(Integer.parseInt(strs[0]),Integer.parseInt(strs[1]),Integer.parseInt(strs[2]),Integer.parseInt(strs[3]));
				jobs.add(job);
			}
			fis.close();
			return jobs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void ReadJobInstructions(Job job) {  //��ȡ��ҵ��ָ��
		try {
			File f = new File("test\\"+job.getJobsID()+".txt");
			FileInputStream fis = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String str=null;
			while((str=br.readLine())!=null) {
				String[] strs = new String[4];
				strs = str.split(",");
				Instruction ins = new Instruction(Integer.parseInt(strs[0]),Integer.parseInt(strs[1]),Integer.parseInt(strs[2]),Integer.parseInt(strs[3]));
				job.addJobInstruction(ins);
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void initWriteData() {  //��ʼ��д�ļ�
		data = new StringBuffer("");
	}
	public static void writeMessageInData(String str) {  //���ַ������뵽data������
		data.append((str+"\r\n"));
	}
	public static void outputMessageToFile(int time) {  //��������Ϣ������ļ��У�����Ϊ���н���������ϵ�ʱ��
		try {
			File message = new File("test\\ProcessResults-"+ time +".txt");
			FileOutputStream fos = new FileOutputStream(message);
			fos.write(data.toString().getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
