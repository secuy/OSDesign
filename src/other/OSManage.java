package other;

import kernel.*;
import hardware.*;


//���ڿ�����������ϵͳ�Լ�Ӳ���Ĺ�����
public class OSManage {
	
	private JobsRequest jr;
	private ProcessSchedule ps;
	private IOInterrupt ii;
	private DiskRWInterrupt dri;
	private LackPageInterrupt lpi;
	
	private boolean pause;  //ϵͳ��ͣ�ź�
	
	private boolean shutdown;  //ϵͳ��ֹ�ź�
	
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
		
		pause = false;
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

	//��ͣϵͳ
	public void pauseSystem() {
		this.pause = true;
	}
	//��������ϵͳ
	public void proceedSystem() {
		this.pause = false;
	}
	//��ֹϵͳ
	public void shutdownSystem() {
		this.shutdown = true;
	}
	
	public void runSystem() {
		jr.start();
		ps.start();
		ii.start();
		dri.start();
		lpi.start();
		
		
	}

}
