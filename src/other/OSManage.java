package other;

import ui.ProcessUI;
import kernel.*;
import hardware.*;


//用于控制整个操作系统以及硬件的管理类
public class OSManage {
	
	private JobsRequest jr;
	private ProcessSchedule ps;
	private IOInterrupt ii;
	private DiskRWInterrupt dri;
	private LackPageInterrupt lpi;
	
	private volatile boolean pause;  //系统暂停信号
	
	private volatile boolean shutdown;  //系统终止信号
	
	private MMU mmu;
	
	BufferManager bm;
	
	public OSManage() {
		
		mmu = new MMU();
		
		jr = new JobsRequest(this);
		ps = new ProcessSchedule(this);
		ii = new IOInterrupt(this);
		dri = new DiskRWInterrupt(this);
		lpi = new LackPageInterrupt(this);
		
		bm = new BufferManager();
		
		pause = true;
		shutdown = false;
	}
	
	public JobsRequest getJobsRequest() {
		return jr;
	}

	public ProcessSchedule getProcessSchedule() {
		return ps;
	}
	
	
	
	public MMU getMmu() {
		return mmu;
	}
	
	public BufferManager getBufferManager() {
		return bm;
	}
	
	public boolean isPause() {
		return pause;
	}

	public boolean isShutdown() {
		return shutdown;
	}

	//暂停系统
	public void pauseSystem() {
		this.pause = true;
	}
	//继续启动系统
	public void proceedSystem() {
		this.pause = false;
	}
	//终止系统
	public void shutdownSystem() {
		this.shutdown = true;
	}
	
	//信息输出系统
	public static void messageOutputSystem(String s) {
		StringBuffer mes = new StringBuffer(s);
		ProcessUI.addMessage(mes.toString());
		IOFile.writeMessageInData(mes.toString());
		System.out.println(mes.toString());
	}
	
	
	public void runSystem() {
		jr.start();
		ps.start();
		ii.start();
		dri.start();
		lpi.start();
		
		
	}

}
