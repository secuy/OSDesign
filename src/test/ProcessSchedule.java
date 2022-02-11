package test;

import java.util.*;

public class ProcessSchedule extends Thread{  //进程调度类
	private static Clock clock;   	//时钟
	private static Process runningProcess = null;    //正在执行的进程
	static List<Process> ReadyQ = new LinkedList<Process>();   //就绪队列
	static List<Process> BlockQ1 = new LinkedList<Process>();   //阻塞队列1
	static List<Process> BlockQ2 = new LinkedList<Process>();   //阻塞队列2
	static List<Process> BlockQ3 = new LinkedList<Process>();   //阻塞队列3
	static List<Process> FinishedQ = new LinkedList<Process>();   //进程执行完成队列
	private static List<PCB> pcbList = new ArrayList<PCB>();   //PCB表
	
	public ProcessSchedule() {
		clock = new Clock();
	}
	
	public static Process getRunningProcess() {
		return runningProcess;
	}
	public static void setRunningProcess(Process runningProcess) {
		ProcessSchedule.runningProcess = runningProcess;
	}
	public static void addPcb(PCB pcb) {
		pcbList.add(pcb);
	}
	//只有在入就绪队列和阻塞队列时才会有进程态的转变、加入就绪队列时间、就绪队列编号变化，而其他的则是队列编号的变化
	public static void addReadyQ(Process p) {  //进程加入就绪队列
		p.getPcb().setRqNum(ReadyQ.size());  //设置就绪队列中的编号
		p.getPcb().addRqTimes(clock.getTime());  //加入就绪队列时间
		p.getPcb().setPsw(1);  //调整为就绪态
		ReadyQ.add(p);
	}
	public static void addBlockQ1(Process p) {  //进程加入阻塞队列1
		p.getPcb().setBqNum1(BlockQ1.size());
		p.getPcb().addBqTimes1(clock.getTime());
		p.getPcb().setPsw(2);  //调整为阻塞态
		BlockQ1.add(p);
	}
	public static void addBlockQ2(Process p) {  //进程加入阻塞队列2
		p.getPcb().setBqNum2(BlockQ2.size());
		p.getPcb().addBqTimes2(clock.getTime());
		p.getPcb().setPsw(2);  //调整为阻塞态
		BlockQ2.add(p);
	}
	public static void addBlockQ3(Process p) {  //进程加入阻塞队列3
		p.getPcb().setBqNum3(BlockQ3.size());
		p.getPcb().addBqTimes3(clock.getTime());
		p.getPcb().setPsw(2);  //调整为阻塞态
		BlockQ3.add(p);
	}
	public static Process popReadyQ() {  //就绪队头出队
		Process p = ReadyQ.remove(0);
		for(int i=0;i<ReadyQ.size();i++) {  //更新队中编号信息
			ReadyQ.get(i).getPcb().setRqNum(i);
		}
		return p;
	}
	public static Process popBlockQ1() {  //阻塞队头出队
		Process p = BlockQ1.remove(0);
		for(int i=0;i<BlockQ1.size();i++) {  //更新队中编号信息
			BlockQ1.get(i).getPcb().setBqNum1(i);
		}
		return p;
	}
	public static Process popBlockQ2() {  //阻塞队头出队
		Process p = BlockQ2.remove(0);
		for(int i=0;i<BlockQ2.size();i++) {  //更新队中编号信息
			BlockQ2.get(i).getPcb().setBqNum2(i);
		}
		return p;
	}
	public static Process popBlockQ3() {  //阻塞队头出队
		Process p = BlockQ3.remove(0);
		for(int i=0;i<BlockQ3.size();i++) {  //更新队中编号信息
			BlockQ3.get(i).getPcb().setBqNum3(i);
		}
		return p;
	}
	public static void cpuStateProtection() {   //cpu寄存器现场保护
		runningProcess.getPcb().setPc(CPU.getPc());
		runningProcess.getPcb().setInstruc(CPU.getIr());
	}
	public static void cpuStateRecovery() {   //cpu现场恢复
		CPU.setPc(runningProcess.getPcb().getPc());
		CPU.setIr(runningProcess.getPcb().getInstruc());
	}
	//进程控制原语
	public static Process createProcess(Job j) {  //根据作业创建进程原语
		System.out.println(clock.getTime()+":[创建作业"+j.getJobsID()+"]");
		IOFile.writeMessageInData(clock.getTime()+":[创建作业"+j.getJobsID()+"]");
		ProcessUI.addMessage(clock.getTime()+":[创建作业"+j.getJobsID()+"]");
		
		Process p = new Process(j);
		p.setTime_slice(2);   //设置创建进程时间片为2秒 
		p.getPcb().setInstruc(null);  //当前执行指令为编号1指令
		p.getPcb().setPc(1);  //下一条指令编号为2
		Random r = new Random();
		p.getPcb().setPriority(r.nextInt(5)+1);   //生成随机数作为进程中的优先级数
		
		ProcessUI.addProcessMessage(clock.getTime()+"s进程"+p.getPcb().getPro_ID()+"进入，优先级为："+p.getPcb().getPriority()+"指令数量为："+p.getPcb().getInstrucNum());
		return p;
	}
	public static void cancelProcess(Process p) {  //撤销完成的进程
		p.getPcb().setEndTimes(clock.getTime());
		p.getPcb().setTurnTimes(p.getPcb().getEndTimes()-p.getPcb().getInTimes());
		p.getPcb().setPsw(-1);
		FinishedQ.add(p);
		System.out.println(clock.getTime()+":[终止进程"+p.getPcb().getPro_ID()+"]");
		IOFile.writeMessageInData(clock.getTime()+":[终止进程"+p.getPcb().getPro_ID()+"]");
		ProcessUI.addMessage(clock.getTime()+":[终止进程"+p.getPcb().getPro_ID()+"]");
		ProcessUI.addProcessMessage(clock.getTime()+"s进程"+p.getPcb().getPro_ID()+"执行完毕"+"，耗时："+p.getPcb().getTurnTimes());
	}
	public static void blockProcess(Process p) {  //阻塞进程
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==1) {
			addBlockQ1(p);
			System.out.println(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列1]");
			IOFile.writeMessageInData(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列1]");
			ProcessUI.addMessage(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列1]");
		}
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==2) {
			addBlockQ2(p);
			System.out.println(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列2]");
			IOFile.writeMessageInData(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列2]");
			ProcessUI.addMessage(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列2]");
		}
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==3) {
			addBlockQ3(p);
			System.out.println(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列3]");
			IOFile.writeMessageInData(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列3]");
			ProcessUI.addMessage(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列3]");
		}
		runningProcess.setTime_slice(0);  //使其时间片变清零
	}
	public static void awakeProcess(int BlockQNum) {  //唤醒进程，参数为阻塞队列序号，唤醒阻塞队列队头进程
		switch(BlockQNum) {
			case 1:
				Process p1 = popBlockQ1();   //出阻塞队列，入就绪队列
				p1.getPcb().setPsw(1);
				p1.setTime_slice(2);
				addReadyQ(p1);
				System.out.println(clock.getTime()+":[唤醒进程"+p1.getPcb().getPro_ID()+"进入队列1]");
				IOFile.writeMessageInData(clock.getTime()+":[唤醒进程"+p1.getPcb().getPro_ID()+"进入队列1]");
				ProcessUI.addMessage(clock.getTime()+":[唤醒进程"+p1.getPcb().getPro_ID()+"进入队列1]");
				break;
			case 2:
				Process p2 = popBlockQ2();   //出阻塞队列，入就绪队列
				p2.getPcb().setPsw(1);
				p2.setTime_slice(2);
				addReadyQ(p2);
				System.out.println(clock.getTime()+":[唤醒进程"+p2.getPcb().getPro_ID()+"进入队列1]");
				IOFile.writeMessageInData(clock.getTime()+":[唤醒进程"+p2.getPcb().getPro_ID()+"进入队列1]");
				ProcessUI.addMessage(clock.getTime()+":[唤醒进程"+p2.getPcb().getPro_ID()+"进入队列1]");
				break;
			case 3:
				Process p3 = popBlockQ3();
				p3.getPcb().setPsw(1);
				p3.setTime_slice(2);
				addReadyQ(p3);
				System.out.println(clock.getTime()+":[唤醒进程"+p3.getPcb().getPro_ID()+"进入队列1]");
				IOFile.writeMessageInData(clock.getTime()+":[唤醒进程"+p3.getPcb().getPro_ID()+"进入队列1]");
				ProcessUI.addMessage(clock.getTime()+":[唤醒进程"+p3.getPcb().getPro_ID()+"进入队列1]");
				break;
		}
			
	}
	public void run() {
		while(true) {
			try {
				sleep(100);  //稍作等待，使Job读入
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(FinishedQ.size() == pcbList.size() && JobsRequest.getJobsLength()==0) {  //完成所有作业,则将将所有信息输出到文件中
				IOFile.outputMessageToFile(clock.getTime()-1);
				return;
			}
			InterruptOperator.checkInterruptOperator();   //检查是否有中断处理完成
			if(!CPU.isIs_break_close()) {
				if(runningProcess!=null && runningProcess.getTime_slice()>0) {  //时间片没有用完
					if(runningProcess.getPcb().getPc()>runningProcess.getPcb().getInstrucNum()) {
						cancelProcess(runningProcess);
						runningProcess = null;
						continue;
					}
					CPU.nextInstruction(runningProcess);
					CPU.excute();
					runningProcess.setTime_slice(runningProcess.getTime_slice()-1);
					InterruptOperator.inInterrupt();   //查看当前指令是否需要系统中断调用
					continue;  //由于CPU执行过1s直接进入下一循环
					//进程切换
				} else if(runningProcess!=null && runningProcess.getTime_slice()<=0) {  //时间片用完,切换就绪队列中的队头进程
					cpuStateProtection();  //cpu现场保护
					runningProcess.setTime_slice(2);  //恢复时间片的时长
					if(ReadyQ.size()==0) {  //若就绪队列为空，则继续执行当前进程
						continue;
					}
					addReadyQ(runningProcess);   //将当前进程加入就绪队列  
					runningProcess.setTime_slice(2);  //恢复时间片的时长
					
					runningProcess = popReadyQ();  //将就绪队列队头进程取出转为执行状态
					runningProcess.getPcb().setPsw(0);   //调整为运行态
					cpuStateRecovery();  //cpu现场恢复
					runningProcess.getPcb().addRunTimes(clock.getTime());
					continue;
				} else if(runningProcess==null && ReadyQ.size()>0) {
					//若cpu空闲且没有关中断以及队头进程时间片够用则取出就绪队列队头进程
					runningProcess = popReadyQ();  //取出队头进程
					runningProcess.getPcb().addRunTimes(clock.getTime());
					runningProcess.getPcb().setPsw(0);
					cpuStateRecovery();
					CPU.setIs_busy(true);
					continue;
				}
			}
			try {  //只有CPU无作业或者关中断时使用以下时序控制
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ProcessUI.OutputMessage();
			clock.passOneSec();
			if(ProcessUI.pause) {  //暂停信号
				return;
			}
		}
	}
}
