package kernel;

import hardware.CPU;
import hardware.Clock;
import hardware.MMU;

import java.util.*;

import other.IOFile;
import other.OSManage;

import ui.ProcessUI;




public class ProcessSchedule extends Thread{  //进程调度类
	private static Clock clock;   	//时钟
	private static Process runningProcess = null;    //正在执行的进程
	
	//设置常量，最高用户进程并发度为8
	public static final int MAX_PROCESS_NUM = 8;
	
	//常量，时间片大小为2s
	public static final int TIME_SLICE = 2;
	
	//常量，每个用户进程占用的物理块数（页数）为3
	public static final int needPageNum = 3;
	
	//常量，进程运行状态：运行态
	public static final int RUNNING_STATE = 0;
	
	//常量，进程运行状态：就绪态
	public static final int READY_STATE = 1;
	
	//常量，进程运行状态：阻塞态
	public static final int BLOCK_STATE = 2;
	
	
	
	//位于外存的后备队列
	//使用Vector容器是线程安全的，不同线程对其操作不会出错。
	private static Vector<Job> ReverseQ = new Vector<Job>();
	
	private static Vector<Process> ReadyQ = new Vector<Process>();   //就绪队列
	
	private static Vector<Process> BlockQ1 = new Vector<Process>();   //阻塞队列1
	private static Vector<Process> BlockQ2 = new Vector<Process>();   //阻塞队列2
	private static Vector<Process> BlockQ3 = new Vector<Process>();   //阻塞队列3
	private static Vector<Process> BlockQ4 = new Vector<Process>();   //阻塞队列4
	private static Vector<Process> BlockQ5 = new Vector<Process>();   //阻塞队列5
	
	private static Vector<Process> FinishedQ = new Vector<Process>();   //进程执行完成队列
	
	
	private static Vector<PCB> pcbList = new Vector<PCB>();   //正在内存中的PCB表
	
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
	
	
	
	public static Vector<PCB> getPcbList() {
		return pcbList;
	}

	public static List<Job> getReverseQ() {
		return ReverseQ;
	}

	public static List<Process> getReadyQ() {
		return ReadyQ;
	}

	public static List<Process> getBlockQ1() {
		return BlockQ1;
	}

	public static List<Process> getBlockQ2() {
		return BlockQ2;
	}

	public static List<Process> getBlockQ3() {
		return BlockQ3;
	}

	public static List<Process> getBlockQ4() {
		return BlockQ4;
	}
	public static List<Process> getBlockQ5() {
		return BlockQ5;
	}
	
	public static List<Process> getFinishedQ() {
		return FinishedQ;
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
	public static void addBlockQ4(Process p) {  //进程加入阻塞队列3
		p.getPcb().setBqNum4(BlockQ4.size());
		p.getPcb().addBqTimes4(clock.getTime());
		p.getPcb().setPsw(2);  //调整为阻塞态
		BlockQ4.add(p);
	}
	public static void addBlockQ5(Process p) {  //进程加入阻塞队列3
		p.getPcb().setBqNum5(BlockQ5.size());
		p.getPcb().addBqTimes5(clock.getTime());
		p.getPcb().setPsw(2);  //调整为阻塞态
		BlockQ5.add(p);
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
	public static Process popBlockQ4() {  //阻塞队头出队
		Process p = BlockQ4.remove(0);
		for(int i=0;i<BlockQ4.size();i++) {  //更新队中编号信息
			BlockQ4.get(i).getPcb().setBqNum4(i);
		}
		return p;
	}
	public static Process popBlockQ5() {  //阻塞队头出队
		Process p = BlockQ5.remove(0);
		for(int i=0;i<BlockQ5.size();i++) {  //更新队中编号信息
			BlockQ5.get(i).getPcb().setBqNum5(i);
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
	public static Process createProcess(Job j) {  //根据作业创建进程原语放入就绪队列当中
		System.out.println(clock.getTime()+":[创建进程"+j.getJobsID()+"]");
		//IOFile.writeMessageInData(clock.getTime()+":[创建进程"+j.getJobsID()+"]");
		//ProcessUI.addMessage(clock.getTime()+":[创建进程"+j.getJobsID()+"]");
		
		Process p = new Process(j);
		p.setTime_slice(TIME_SLICE);   //设置创建进程时间片为规定时间片的时间
		p.getPcb().setInstruc(null);  //当前执行指令为编号1指令
		p.getPcb().setPc(1);  //下一条指令编号为2
		
		//分配内存页框
		MMU.givePageFrame(p.getPcb());
		
		Random r = new Random();
		p.getPcb().setPriority(r.nextInt(5)+1);   //生成随机数作为进程中的优先级数
		
		//将该进程放入到就绪队列当中
		addReadyQ(p);
		
		//ProcessUI.addProcessMessage(clock.getTime()+"s进程"+p.getPcb().getPro_ID()+"进入，优先级为："+p.getPcb().getPriority()+"指令数量为："+p.getPcb().getInstrucNum());
		return p;
	}
	public static void cancelProcess(Process p) {  //撤销完成的进程
		p.getPcb().setEndTimes(clock.getTime());
		p.getPcb().setTurnTimes(p.getPcb().getEndTimes()-p.getPcb().getInTimes());
		p.getPcb().setPsw(-1);
		//从PCB列表中移出PCB
		for(int i=0;i<pcbList.size();i++) {
			if(pcbList.get(i).getPro_ID() == p.getPcb().getPro_ID()) {
				pcbList.remove(i);
			}
		}
		FinishedQ.add(p);
		System.out.println(clock.getTime()+":[终止进程"+p.getPcb().getPro_ID()+"]");
		//IOFile.writeMessageInData(clock.getTime()+":[终止进程"+p.getPcb().getPro_ID()+"]");
		//ProcessUI.addMessage(clock.getTime()+":[终止进程"+p.getPcb().getPro_ID()+"]");
		//ProcessUI.addProcessMessage(clock.getTime()+"s进程"+p.getPcb().getPro_ID()+"执行完毕"+"，耗时："+p.getPcb().getTurnTimes());
	}
	public static void blockProcess(Process p) {  //阻塞进程
		//如果缺页，调入阻塞队列3中
		if(p.isLackPage()) {
			addBlockQ3(p);
			System.out.println(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列3]");
			return;
		}
		
		//根据指令状态进入阻塞队列
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==2) {
			addBlockQ1(p);
			System.out.println(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列1]");
			//IOFile.writeMessageInData(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列1]");
			//ProcessUI.addMessage(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列1]");
		}
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==3) {
			addBlockQ2(p);
			System.out.println(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列2]");
			//IOFile.writeMessageInData(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列2]");
			//ProcessUI.addMessage(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列2]");
		}
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==4) {
			addBlockQ3(p);
			System.out.println(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列3]");
			//IOFile.writeMessageInData(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列3]");
			//ProcessUI.addMessage(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列3]");
		}
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==5) {
			addBlockQ4(p);
			System.out.println(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列4]");
			//IOFile.writeMessageInData(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列4]");
			//ProcessUI.addMessage(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列4]");
		}
		if(p.getPcb().getInterrupt_instruc().getInstruc_state()==6) {
			addBlockQ5(p);
			System.out.println(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列5]");
			//IOFile.writeMessageInData(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列5]");
			//ProcessUI.addMessage(clock.getTime()+":[阻塞进程"+p.getPcb().getPro_ID()+"进入队列5]");
		}
		runningProcess.setTime_slice(0);  //使其时间片变清零
		
	}
	public static void awakeProcess(int BlockQNum,int now_time) {  //唤醒进程，参数为阻塞队列序号，唤醒阻塞队列队头进程
		switch(BlockQNum) {
			case 2:
				Process p1 = popBlockQ1();   //出阻塞队列，入就绪队列
				p1.getPcb().setPsw(1);
				p1.setTime_slice(2);
				addReadyQ(p1);
				System.out.println(now_time+":[唤醒进程"+p1.getPcb().getPro_ID()+"进入队列1]");
				//IOFile.writeMessageInData(clock.getTime()+":[唤醒进程"+p1.getPcb().getPro_ID()+"进入队列1]");
				//ProcessUI.addMessage(clock.getTime()+":[唤醒进程"+p1.getPcb().getPro_ID()+"进入队列1]");
				break;
			case 3:
				Process p2 = popBlockQ2();   //出阻塞队列，入就绪队列
				p2.getPcb().setPsw(1);
				p2.setTime_slice(2);
				addReadyQ(p2);
				System.out.println(now_time+":[唤醒进程"+p2.getPcb().getPro_ID()+"进入队列1]");
				//IOFile.writeMessageInData(clock.getTime()+":[唤醒进程"+p2.getPcb().getPro_ID()+"进入队列1]");
				//ProcessUI.addMessage(clock.getTime()+":[唤醒进程"+p2.getPcb().getPro_ID()+"进入队列1]");
				break;
			case 4:
				Process p3 = popBlockQ3();
				p3.getPcb().setPsw(1);
				p3.setTime_slice(2);
				addReadyQ(p3);
				System.out.println(now_time+":[唤醒进程"+p3.getPcb().getPro_ID()+"进入队列1]");
				//IOFile.writeMessageInData(clock.getTime()+":[唤醒进程"+p3.getPcb().getPro_ID()+"进入队列1]");
				//ProcessUI.addMessage(clock.getTime()+":[唤醒进程"+p3.getPcb().getPro_ID()+"进入队列1]");
				break;
			case 5:
				Process p4 = popBlockQ4();
				p4.getPcb().setPsw(1);
				p4.setTime_slice(2);
				addReadyQ(p4);
				System.out.println(now_time+":[唤醒进程"+p4.getPcb().getPro_ID()+"进入队列1]");
				//IOFile.writeMessageInData(clock.getTime()+":[唤醒进程"+p4.getPcb().getPro_ID()+"进入队列1]");
				//ProcessUI.addMessage(clock.getTime()+":[唤醒进程"+p4.getPcb().getPro_ID()+"进入队列1]");
				break;
			case 6:
				Process p5 = popBlockQ5();
				p5.getPcb().setPsw(1);
				p5.setTime_slice(2);
				addReadyQ(p5);
				System.out.println(now_time+":[唤醒进程"+p5.getPcb().getPro_ID()+"进入队列1]");
				//IOFile.writeMessageInData(clock.getTime()+":[唤醒进程"+p5.getPcb().getPro_ID()+"进入队列1]");
				//ProcessUI.addMessage(clock.getTime()+":[唤醒进程"+p5.getPcb().getPro_ID()+"进入队列1]");
				break;
		}
			
	}
	
	//对就绪队列中的进程按优先级数进行从小到大的排序
	public void sortReadyQ() {
		int min_priority,min_index;
		for(int i=0;i<ReadyQ.size()-1;i++) {
			min_priority=99; min_index=i;
			for(int k=i;k<ReadyQ.size();k++) {
				if(ReadyQ.get(k).getPcb().getPriority() < min_priority) {
					min_priority = ReadyQ.get(k).getPcb().getPriority();
					min_index = k;
				}
			}
			Process temp = ReadyQ.get(i);
			ReadyQ.set(i, ReadyQ.get(min_index));
			ReadyQ.set(min_index, temp);
		}
	}
	
	//高级调度，从外存中的后备队列中创建进程到内存中的就绪队列
	public void highSchedule() {
		//如果后备队列中没有作业，或者当前在内存中的PCB的数量已经大于最大并发进程数，就不进行高级调度
		if(ReverseQ.size()<=0 || pcbList.size()>=MAX_PROCESS_NUM) {
			return;
		}
		
		//找到优先级最大的作业（优先级数最小）按顺序加入到就绪队列当中
		int min_priority,min_index;
		while(ReverseQ.size()>0 && pcbList.size()<MAX_PROCESS_NUM) {
			min_priority=100;
			min_index=0;
			for(int i=0;i<ReverseQ.size();i++) {
				if(ReverseQ.get(i).getPriority() < min_priority) {
					min_index = i;
					min_priority = ReverseQ.get(i).getPriority();
				}
			}
			createProcess(ReverseQ.remove(min_index));
		}
	}
	
	//低级调度，内存与cpu之间的调度
	public void lowSchedule() {
		//如果就绪队列为空或者，有正在执行的程序那么直接结束低级调度
		if(ReadyQ.size()==0 || runningProcess!=null) {
			return;
		}
		
		//调度前先对优先级进行排序
		sortReadyQ();
		
		//将CPU切换为内核态
		CPU.switchToKernelState();
		
		//就绪队列队头元素出队
		runningProcess = popReadyQ();
		//设置为运行状态
		runningProcess.getPcb().setPsw(RUNNING_STATE);
		//恢复cpu现场
		cpuStateRecovery();
		//增加运行时间
		runningProcess.getPcb().addRunTimes(clock.getTime());
		//将CPU切换为用户态
		CPU.switchToUserState();
		
	}
	
	public void run() {
		
		while(true) {
			
			highSchedule();
			lowSchedule();
			
			
			if(runningProcess!=null) {
				
				
				//切换下一条指令
				CPU.nextInstruction(runningProcess);
				
				//缺页中断检查
				if(LackPageInterrupt.checkLackPage(runningProcess)) {
					//缺页则直接跳过下面步骤
					continue;
				}
				
				//CPU执行指令
				CPU.excute();
				
				//检查是否需要中断
				InterruptOperator.inInterrupt();
			}
			
			//时钟经过一秒
			clock.passOneSec();
			
			
			//如果该指令没有完成，那么不能进行进程调度，需要直接继续完成
			if(runningProcess!=null && !runningProcess.getPcb().getInstruc().isFinished()) {
				continue;
			}
			
			//如果执行到了最后一条指令
			if(runningProcess!=null && runningProcess.getPcb().getPc()>runningProcess.getPcb().getInstrucNum()) {
				cancelProcess(runningProcess);
				runningProcess = null;
				continue;
			}
			
			if(runningProcess!=null) {
				//时间片没有用完
				if(runningProcess.getTime_slice()>1) {
					runningProcess.setTime_slice(runningProcess.getTime_slice()-1);
				} else {
					//时间片用完
					cpuStateProtection();  //cpu现场保护
					runningProcess.setTime_slice(2);  //恢复时间片的时长
					if(ReadyQ.size()==0) {  //若就绪队列为空，则继续执行当前进程
						continue;
					}
					runningProcess.getPcb().setPsw(READY_STATE);
					addReadyQ(runningProcess);   //将当前进程加入就绪队列
					runningProcess = null;
				}
			}
			
		}
		
		/*while(true) {
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
			if(ProcessUI.isPause()) {  //暂停信号
				return;
			}
		}*/
	}
}
