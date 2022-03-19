package other;

import java.io.*;

import java.util.*;
import kernel.Instruction;
import kernel.Job;

public class IOFile {  //��ҵ�ļ���Ϣ����
	
	private static int outPutFileTag=0;  //����ļ���־�������ս��ֻ���һ��
	
	public static StringBuffer data = new StringBuffer("");  //���������Ϣ
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
	
	public static void createRandomInstructionFile(Job j) {
		//���ĳ�ʼjob�б�
		File jobList = new File("test\\19319229-jobs-input.txt");
		try {
			FileOutputStream fjob = new FileOutputStream(jobList,true);
			fjob.write((j.toString()+"\r\n").getBytes());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		File f = new File("test\\"+j.getJobsID()+".txt");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Random r = new Random();
			FileOutputStream fos = new FileOutputStream(f);
			int no,stat,addr=0,time,lastAddr=0;
			for(int i=0;i<j.getInstrucNum();i++) {
				no = i+1;
				if(no == 1) {
					if(r.nextDouble()>=0.5) {
						stat = 2;
					} else {
						stat = 0;
					}
				} else {
					stat = r.nextInt(7);
				}
				if(stat == 1 || stat == 2) {
					time = 2;
				} else if(stat == 3 || stat == 4) {
					time = 3;
				} else if(stat == 5 || stat == 6) {
					time = 4;
				} else {
					time = 1;
				}
				if(stat == 1 || stat == 4 || stat == 5) {
					addr = r.nextInt(11)+10;
				} else {
					if(no == 1) {
						if(r.nextDouble()>=0.5) {
							addr = 1;
						} else {
							addr = 0;
						}
						lastAddr = addr;
					} else {
						if(r.nextDouble()>=0.6) {
							addr = lastAddr+2;
						}
						else {
							addr = lastAddr;
						}
						lastAddr = addr;
					}
				}
				String s = no+","+stat+","+addr+","+time+"\r\n";
				fos.write(s.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeMessageInData(String str) {  //���ַ������뵽data������
		data.append((str+"\r\n"));
	}
	public static void outputMessageToFile(int time) {  //��������Ϣ������ļ��У�����Ϊ���н���������ϵ�ʱ��
		if(outPutFileTag==0) {
			try {
				File message = new File("test\\ProcessResults-"+ time +".txt");
				FileOutputStream fos = new FileOutputStream(message);
				fos.write(data.toString().getBytes());
				fos.close();
				outPutFileTag = 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	}
}
