package kernel;

import hardware.Clock;

/**
 * 缺页中断处理
 *
 */

public class LackPageInterrupt extends Thread {
	private static Clock clock;
	
	
	public LackPageInterrupt() {
		clock = new Clock();
	}
	
	//检查是否缺页,返回值为false则为并不缺页，反之缺页
	public static boolean checkLackPage(Process p) {
		//得到当前进程执行的指令
		int logicAddr = p.getPcb().getInstruc().getL_Address();
		PageFrameLRU[] pfl = p.getPcb().getPage_frame_nums();
		int i;
		for(i=0;i<ProcessSchedule.needPageNum;i++) {
			if(logicAddr == pfl[i].getLogic_no()) {
				pfl[i].addInMemTimes();
				p.setLackPage(false);
				System.out.println("页表命中，逻辑页号为："+pfl[i].getLogic_no()+"页框号为："+pfl[i].getPage_frame_no()+"使用次数："+pfl[i].getInMemTimes());
				return false;
			}
		}
		//说明页表没有命中，那么就需要从磁盘读入相应的文件，因此加入到阻塞队列3
		if(i>=ProcessSchedule.needPageNum) {
			p.setLackPage(true);
			InterruptOperator.inInterrupt();
		}
		return true;
	}

	
	//实现LRU算法
	public void LRU(Process p) {
		PageFrameLRU[] page_frames = p.getPcb().getPage_frame_nums();
		int logicAddr = p.getPcb().getInstruc().getL_Address();
		int minVal=page_frames[0].getInMemTimes(),minIndex=0;
		for(int i=1;i<page_frames.length;i++) {
			if(minVal > page_frames[i].getInMemTimes()) {
				minVal = page_frames[i].getInMemTimes();
				minIndex = i;
			}
		}
		//保存换出页框的页表项信息
		int tempLogic = page_frames[minIndex].getLogic_no();
		int tempTimes = page_frames[minIndex].getInMemTimes();
		
		
		//换入页表项
		page_frames[minIndex].setLogic_no(logicAddr);
		page_frames[minIndex].setInMemTimes(p.getPcb().getPage_items()[logicAddr].getInMemTimes());

		//换出页表
		if(tempLogic != -1) {
			p.getPcb().getPage_items()[tempLogic].setInMemTimes(tempTimes);
		}
	}
	
	//处理缺页中断，调入内存
	public void readPageFromDisk() {
		if(ProcessSchedule.getBlockQ3().size()>0) {
			Process p = ProcessSchedule.getBlockQ3().get(0);
			if(p.isLackPage()) {
				System.out.println("从磁盘调入内存");
				
				//要把逻辑地址和页框号统一起来
				LRU(p);
				//不再缺页
				//p.setLackPage(false);
			}
		}
	}
	
	
	@Override
	public void run() {
		
		while(true) {
			readPageFromDisk();
			
			clock.passOneSec();
		}
	}
	
}
