package other;

import kernel.*;
import hardware.*;


//用于控制整个操作系统以及硬件的管理类
public class OSManage {
	
	private JobsRequest jr;
	private ProcessSchedule ps;
	private IOInterrupt ii;
	private DiskRWInterrupt dri;
	private LackPageInterrupt lpi;
	
	private MMU mmu;
	
	
	public OSManage() {
		
		mmu = new MMU();
		
		jr = new JobsRequest(this);
		ps = new ProcessSchedule();
		ii = new IOInterrupt();
		dri = new DiskRWInterrupt();
		lpi = new LackPageInterrupt();
		
		
	}
	
	public JobsRequest getJobsRequest() {
		return jr;
	}

	public ProcessSchedule getProcessSchedule() {
		return ps;
	}
	
	public void runSystem() {
		jr.start();
		ps.start();
		ii.start();
		dri.start();
		lpi.start();
	}

}
