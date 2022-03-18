package kernel;

import hardware.*;


public class InterruptOperator {   //中断处理
	
	private static Instruction keyboard_interrupt = null;  //键盘中断指令，对应指令类型为2,阻塞队列4,为空说明暂无
	private static int keyboard_time = -1;  //进入键盘中断处理的时间（注意与进入阻塞队列时间不同）
	
	private static Instruction screen_interrupt = null;  //屏幕中断，对应指令类型为3,阻塞队列2
	private static int screen_time = -1;  //进入屏幕中断处理的时间
	
	private static Instruction readDisk_interrupt = null;  //读磁盘文件中断，对应指令类型为4,阻塞队列3
	private static int readDisk_time = -1;  //读磁盘文件的时间
	
	private static Instruction writeDisk_interrupt = null;  //写磁盘文件中断，对应指令类型为5,阻塞队列4
	private static int writeDisk_time = -1;  //写磁盘文件的时间
	
	private static Instruction print_interrupt = null;  //打印操作中断，对应指令类型为6,阻塞队列5
	private static int print_time = -1;  //打印操作的时间
	
	public InterruptOperator() {
		
	}
	public static int getKeyboard_time() {
		return keyboard_time;
	}
	public static void setKeyboard_time(int keyboardTime) {
		keyboard_time = keyboardTime;
	}
	public static int getScreen_time() {
		return screen_time;
	}
	public static void setScreen_time(int screenTime) {
		screen_time = screenTime;
	}
	
	public static int getReadDisk_time() {
		return readDisk_time;
	}
	public static void setReadDisk_time(int readDiskTime) {
		readDisk_time = readDiskTime;
	}
	public static int getWriteDisk_time() {
		return writeDisk_time;
	}
	public static void setWriteDisk_time(int writeDiskTime) {
		writeDisk_time = writeDiskTime;
	}
	public static int getPrint_time() {
		return print_time;
	}
	public static void setPrint_time(int printTime) {
		print_time = printTime;
	}
	
	public static Instruction getScreen() {
		return screen_interrupt;
	}
	public synchronized static void inInterrupt() {  //送入中断处理
		if(CPU.getIr().getInstruc_state()==0 || CPU.getIr().getInstruc_state()==1) {
			if(!ProcessSchedule.getRunningProcess().isLackPage()) {
				return;
			}
		}
		ProcessSchedule.getRunningProcess().getPcb().setInterrupt_instruc(CPU.getIr());
		ProcessSchedule.getRunningProcess().setTime_slice(0);   //设置中断进程时间片变为0
		ProcessSchedule.cpuStateProtection();  //cpu现场保护
		CPU.switchToKernelState();  //设置中断处理时CPU为内核态
		
		//缺页的时候加入阻塞队列3
		if(ProcessSchedule.getRunningProcess().isLackPage()) {
			if(readDisk_interrupt==null) {
				readDisk_interrupt = CPU.getIr();
				readDisk_time = IOInterrupt.getClock().getTime();
			}
			//CPU.setIs_break_close(true);  //关中断
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
			
			ProcessSchedule.setRunningProcess(null);  //执行进程设为空
			//CPU.switchToUserState();   //中断阻塞完成CPU设置为用户态
			CPU.setIr(null);
			CPU.setIs_busy(false);
			return;
		}
		
		if(CPU.getIr().getInstruc_state()==2) {
			if(keyboard_interrupt==null) {
				keyboard_interrupt = CPU.getIr();
				keyboard_time = IOInterrupt.getClock().getTime();
			}
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		if(CPU.getIr().getInstruc_state()==3) {
			if(screen_interrupt==null) {
				screen_interrupt = CPU.getIr();
				screen_time = IOInterrupt.getClock().getTime();
			}
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		if(CPU.getIr().getInstruc_state()==4) {
			if(readDisk_interrupt==null) {
				readDisk_interrupt = CPU.getIr();
				readDisk_time = IOInterrupt.getClock().getTime();
			}
			//CPU.setIs_break_close(true);  //关中断
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		if(CPU.getIr().getInstruc_state()==5) {
			if(writeDisk_interrupt==null) {
				writeDisk_interrupt = CPU.getIr();
				writeDisk_time = IOInterrupt.getClock().getTime();
			}
			//CPU.setIs_break_close(true);  //关中断
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		if(CPU.getIr().getInstruc_state()==6) {
			if(print_interrupt==null) {
				print_interrupt = CPU.getIr();
				print_time = IOInterrupt.getClock().getTime();
			}
			//CPU.setIs_break_close(true);  //关中断
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		
		ProcessSchedule.setRunningProcess(null);  //执行进程设为空
		//CPU.switchToUserState();   //中断阻塞完成CPU设置为用户态
		CPU.setIr(null);
		CPU.setIs_busy(false);
	}
	public synchronized static void checkIOInterrupt() {  //检查输入输出中断是否完成，并且将后续阻塞队列进行中断处理
		if(ProcessSchedule.getBlockQ1().size()>0) {
			int time = IOInterrupt.getClock().getTime();
			if(time - keyboard_time>=2) {
				keyboard_interrupt.setFinished(true);  //该指令执行完毕
				ProcessSchedule.getBlockQ1().get(0).getPcb().setInterrupt_instruc(null);  //将阻塞进程中的中断指令置空
				//设置中断设备为空
				keyboard_interrupt = null;
				keyboard_time = -1;
				//判断是否为最后一个指令
				if(ProcessSchedule.getBlockQ1().get(0).getPcb().getPc()>ProcessSchedule.getBlockQ1().get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ1();
					ProcessSchedule.cancelProcess(p);
				} else {
					//唤醒阻塞队列队头进程
					ProcessSchedule.awakeProcess(2,time);
				}
				//中断服务为阻塞队列中的下一个进程服务
				if(ProcessSchedule.getBlockQ1().size()>0) {  //调整需要中断调用的指令
					keyboard_interrupt = ProcessSchedule.getBlockQ1().get(0).getPcb().getInterrupt_instruc();
					keyboard_time = time;
				}	
			}
		}
		if(ProcessSchedule.getBlockQ2().size()>0) {
			int time = IOInterrupt.getClock().getTime();
			if(time - screen_time>=1) {
				screen_interrupt.setFinished(true);  //该指令执行完毕
				ProcessSchedule.getBlockQ2().get(0).getPcb().setInterrupt_instruc(null);  //设置阻塞指令为空
				screen_interrupt = null;
				screen_time = -1;
				//判断是否为最后一个指令
				if(ProcessSchedule.getBlockQ2().get(0).getPcb().getPc()>ProcessSchedule.getBlockQ2().get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ2();
					ProcessSchedule.cancelProcess(p);
				} else {
					//唤醒阻塞队列队头进程
					ProcessSchedule.awakeProcess(3,time);
				}
				//中断服务为阻塞队列中的下一个进程服务
				if(ProcessSchedule.getBlockQ2().size()>0) {  //调整需要中断调用的指令
					screen_interrupt = ProcessSchedule.getBlockQ2().get(0).getPcb().getInterrupt_instruc();
					screen_time = time;
				}
			}
		}
		
		
		if(ProcessSchedule.getBlockQ5().size()>0) {
			int time = IOInterrupt.getClock().getTime();
			if(time - print_time>=4) {
				print_interrupt.setFinished(true);  //该指令执行完毕
				ProcessSchedule.getBlockQ5().get(0).getPcb().setInterrupt_instruc(null);  //设置阻塞指令为空
				print_interrupt = null;
				print_time = -1;
				//判断是否为最后一个指令
				if(ProcessSchedule.getBlockQ5().get(0).getPcb().getPc()>ProcessSchedule.getBlockQ5().get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ5();
					ProcessSchedule.cancelProcess(p);
					CPU.setIs_break_close(false);  //取消关中断
					
				} else {
					//唤醒阻塞队列队头进程
					ProcessSchedule.awakeProcess(6,time);
				}
				//中断服务为阻塞队列中的下一个进程服务
				if(ProcessSchedule.getBlockQ5().size()>0) {  //调整需要中断调用的指令
					print_interrupt = ProcessSchedule.getBlockQ5().get(0).getPcb().getInterrupt_instruc();
					print_time = time;
				} else {
					CPU.setIs_break_close(false);  //取消关中断
				}
			}
		}
		if(ProcessSchedule.getBlockQ1().size()==0 
				&& ProcessSchedule.getBlockQ2().size()==0
				&& ProcessSchedule.getBlockQ3().size()==0
				&& ProcessSchedule.getBlockQ4().size()==0
				&& ProcessSchedule.getBlockQ5().size()==0) {
			CPU.setPsw(1);  //无中断处理，则转换CPU为用户态
		}
	}
	
	public synchronized static void checkDiskRWInterrupt() {  //检查磁盘文件读写中断是否完成，并且将后续阻塞队列进行中断处理
		if(ProcessSchedule.getBlockQ3().size()>0) {
			int time = DiskRWInterrupt.getClock().getTime();
			if(time-readDisk_time>=3) {
				if(!ProcessSchedule.getBlockQ3().get(0).isLackPage()) {
					readDisk_interrupt.setFinished(true);  //该指令执行完毕
				} else {
					//不再缺页
					ProcessSchedule.getBlockQ3().get(0).setLackPage(false);
					ProcessSchedule.getBlockQ3().get(0).setFinishLRUTag(false);
				}
				ProcessSchedule.getBlockQ3().get(0).getPcb().setInterrupt_instruc(null);  //设置阻塞指令为空
				readDisk_interrupt = null;
				readDisk_time = -1;
				//判断是否为最后一个指令,并且该指令已经完成
				if(ProcessSchedule.getBlockQ3().get(0).getPcb().getPc()>ProcessSchedule.getBlockQ3().get(0).getPcb().getInstrucNum()
					&& ProcessSchedule.getBlockQ3().get(0).getPcb().getInstruc().isFinished()) {
					Process p = ProcessSchedule.popBlockQ3();
					ProcessSchedule.cancelProcess(p);
					CPU.setIs_break_close(false);  //取消关中断
				} else {
					//唤醒阻塞队列队头进程
					ProcessSchedule.awakeProcess(4,time);
				}
				//中断服务为阻塞队列中的下一个进程服务
				if(ProcessSchedule.getBlockQ3().size()>0) {  //调整需要中断调用的指令
					readDisk_interrupt = ProcessSchedule.getBlockQ3().get(0).getPcb().getInterrupt_instruc();
					readDisk_time = time;
				} else {
					CPU.setIs_break_close(false);  //取消关中断
				}
			}
		}
		
		if(ProcessSchedule.getBlockQ4().size()>0) {
			int time = DiskRWInterrupt.getClock().getTime();
			if(time-writeDisk_time>=4) {
				writeDisk_interrupt.setFinished(true);  //该指令执行完毕
				ProcessSchedule.getBlockQ4().get(0).getPcb().setInterrupt_instruc(null);  //设置阻塞指令为空
				writeDisk_interrupt = null;
				writeDisk_time = -1;
				//判断是否为最后一个指令
				if(ProcessSchedule.getBlockQ4().get(0).getPcb().getPc()>ProcessSchedule.getBlockQ4().get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ4();
					ProcessSchedule.cancelProcess(p);
					CPU.setIs_break_close(false);  //取消关中断
					
				} else {
					//唤醒阻塞队列队头进程
					ProcessSchedule.awakeProcess(5,time);
				}
				//中断服务为阻塞队列中的下一个进程服务
				if(ProcessSchedule.getBlockQ4().size()>0) {  //调整需要中断调用的指令
					writeDisk_interrupt = ProcessSchedule.getBlockQ4().get(0).getPcb().getInterrupt_instruc();
					writeDisk_time = time;
				} else {
					CPU.setIs_break_close(false);  //取消关中断
				}
			}
		}
		
		if(ProcessSchedule.getBlockQ1().size()==0 
				&& ProcessSchedule.getBlockQ2().size()==0
				&& ProcessSchedule.getBlockQ3().size()==0
				&& ProcessSchedule.getBlockQ4().size()==0
				&& ProcessSchedule.getBlockQ5().size()==0) {
			CPU.setPsw(1);  //无中断处理，则转换CPU为用户态
		}
	}
}
