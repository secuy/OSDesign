package test;

public class InterruptOperator {   //中断处理
	private static Instruction keyboard_interrupt = null;  //键盘中断指令，对应指令类型为1，为空说明暂无
	private static int keyboard_time = -1;  //进入键盘中断处理的时间（注意与进入阻塞队列时间不同）
	private static Instruction screen_interrupt = null;  //屏幕中断，对应指令类型为2
	private static int screen_time = -1;  //进入屏幕中断处理的时间
	private static Instruction PV_interrupt = null;  //PV操作中断，对应指令类型为3
	private static int PV_time = -1;  //进入PV中断处理的时间
	
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
	public static int getPV_time() {
		return PV_time;
	}
	public static void setPV_time(int pVTime) {
		PV_time = pVTime;
	}
	public static Instruction getScreen() {
		return screen_interrupt;
	}
	public static void inInterrupt() {  //送入中断处理
		if(CPU.getIr().getInstruc_state()==0) {
			return;
		}
		ProcessSchedule.getRunningProcess().getPcb().setInterrupt_instruc(CPU.getIr());
		ProcessSchedule.getRunningProcess().setTime_slice(0);   //设置中断进程时间片变为0
		ProcessSchedule.cpuStateProtection();  //cpu现场保护
		CPU.setPsw(0);  //设置中断处理时CPU为内核态
		if(CPU.getIr().getInstruc_state()==1) {
			if(keyboard_interrupt==null) {
				keyboard_interrupt = CPU.getIr();
				keyboard_time = CPU.clock.getTime();
			}
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		if(CPU.getIr().getInstruc_state()==2) {
			if(screen_interrupt==null) {
				screen_interrupt = CPU.getIr();
				screen_time = CPU.clock.getTime();
			}
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		if(CPU.getIr().getInstruc_state()==3) {
			if(PV_interrupt==null) {
				PV_interrupt = CPU.getIr();
				PV_time = CPU.clock.getTime();
			}
			CPU.setIs_break_close(true);  //关中断
			ProcessSchedule.blockProcess(ProcessSchedule.getRunningProcess());
		}
		ProcessSchedule.setRunningProcess(null);  //执行进程设为空
		CPU.setIr(null);
		CPU.setIs_busy(false);
	}
	public static void checkInterruptOperator() {  //检查中断是否完成，并且将后续阻塞队列进行中断处理
		if(ProcessSchedule.BlockQ1.size()>0) {
			if(CPU.clock.getTime()-keyboard_time>=4) {
				keyboard_interrupt.setFinished(true);  //该指令执行完毕
				ProcessSchedule.BlockQ1.get(0).getPcb().setInterrupt_instruc(null);  //将阻塞进程中的中断指令置空
				//设置中断设备为空
				keyboard_interrupt = null;
				keyboard_time = -1;
				//判断是否为最后一个指令
				if(ProcessSchedule.BlockQ1.get(0).getPcb().getPc()>ProcessSchedule.BlockQ1.get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ1();
					ProcessSchedule.cancelProcess(p);
				} else {
					//唤醒阻塞队列队头进程
					ProcessSchedule.awakeProcess(1);
				}
				//中断服务为阻塞队列中的下一个进程服务
				if(ProcessSchedule.BlockQ1.size()>0) {  //调整需要中断调用的指令
					keyboard_interrupt = ProcessSchedule.BlockQ1.get(0).getPcb().getInterrupt_instruc();
					keyboard_time = CPU.clock.getTime();
				}	
			}
		}
		if(ProcessSchedule.BlockQ2.size()>0) {
			if(CPU.clock.getTime()-screen_time>=3) {
				screen_interrupt.setFinished(true);  //该指令执行完毕
				ProcessSchedule.BlockQ2.get(0).getPcb().setInterrupt_instruc(null);  //设置阻塞指令为空
				screen_interrupt = null;
				screen_time = -1;
				//判断是否为最后一个指令
				if(ProcessSchedule.BlockQ2.get(0).getPcb().getPc()>ProcessSchedule.BlockQ2.get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ2();
					ProcessSchedule.cancelProcess(p);
				} else {
					//唤醒阻塞队列队头进程
					ProcessSchedule.awakeProcess(2);
				}
				//中断服务为阻塞队列中的下一个进程服务
				if(ProcessSchedule.BlockQ2.size()>0) {  //调整需要中断调用的指令
					screen_interrupt = ProcessSchedule.BlockQ2.get(0).getPcb().getInterrupt_instruc();
					screen_time = CPU.clock.getTime();
				}
			}
		}
		if(ProcessSchedule.BlockQ3.size()>0) {
			if(CPU.clock.getTime()-PV_time>=2) {
				PV_interrupt.setFinished(true);  //该指令执行完毕
				ProcessSchedule.BlockQ3.get(0).getPcb().setInterrupt_instruc(null);  //设置阻塞指令为空
				PV_interrupt = null;
				PV_time = -1;
				//判断是否为最后一个指令
				if(ProcessSchedule.BlockQ3.get(0).getPcb().getPc()>ProcessSchedule.BlockQ3.get(0).getPcb().getInstrucNum()) {
					Process p = ProcessSchedule.popBlockQ3();
					ProcessSchedule.cancelProcess(p);
					CPU.setIs_break_close(false);  //取消关中断
					
				} else {
					//唤醒阻塞队列队头进程
					ProcessSchedule.awakeProcess(3);
				}
				//中断服务为阻塞队列中的下一个进程服务
				if(ProcessSchedule.BlockQ3.size()>0) {  //调整需要中断调用的指令
					PV_interrupt = ProcessSchedule.BlockQ3.get(0).getPcb().getInterrupt_instruc();
					PV_time = CPU.clock.getTime();
				} else {
					CPU.setIs_break_close(false);  //取消关中断
				}
			}
		}
		if(ProcessSchedule.BlockQ1.size()==0 
				&& ProcessSchedule.BlockQ2.size()==0
				&& ProcessSchedule.BlockQ3.size()==0) {
			CPU.setPsw(1);  //无中断处理，则转换CPU为用户态
		}
	}
}
