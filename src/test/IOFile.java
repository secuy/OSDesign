package test;

import java.io.*;

public class IOFile {  //作业文件信息读入
	public static StringBuffer data;  //所有输出信息
	public IOFile() {}
	
	public static void ReadJobsList() {   //读取作业列表文件到作业队列中
		try {
			File f = new File("test\\19319229-jobs-input.txt");
			FileInputStream fis = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String str=null;
			while((str=br.readLine())!=null) {
				String[] strs = new String[3];
				strs = str.split(",");
				Job job = new Job(Integer.parseInt(strs[0]),Integer.parseInt(strs[1]),Integer.parseInt(strs[2]));
				ReadJobInstructions(job);  //读取作业的指令
				JobsRequest.jobInQueue(job);  //作业入队
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void ReadJobInstructions(Job job) {  //读取作业的指令
		try {
			File f = new File("test\\"+job.getJobsID()+".txt");
			FileInputStream fis = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String str=null;
			while((str=br.readLine())!=null) {
				String[] strs = new String[2];
				strs = str.split(",");
				Instruction ins = new Instruction(Integer.parseInt(strs[0]),Integer.parseInt(strs[1]));
				job.addJobInstruction(ins);
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void initWriteData() {  //初始化写文件
		data = new StringBuffer("");
	}
	public static void writeMessageInData(String str) {  //将字符串加入到data变量中
		data.append((str+"\r\n"));
	}
	public static void outputMessageToFile(int time) {  //将运行信息输出到文件中，参数为所有进程运行完毕的时间
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
