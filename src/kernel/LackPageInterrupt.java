package kernel;

import other.OSManage;
import hardware.Clock;

/**
 * 缺页中断处理
 *
 */

public class LackPageInterrupt extends Thread {
	private static Clock clock;
	
	OSManage om;
	
	private static int times = 0;
	
	public LackPageInterrupt(OSManage om) {
		clock = new Clock();
		this.om = om;
	}
	
	//检查是否缺页,返回值为false则为并不缺页，反之缺页
	public synchronized static boolean checkLackPage(Process p) {
		//得到当前进程执行的指令
		//正在运行的指令不进行缺页检查
		if(p.getPcb().getInstruc().getFinishRunTimes()==0) {
			int logicAddr = p.getPcb().getInstruc().getL_Address();
			PageFrameLRU[] pfl = p.getPcb().getPage_frame_nums();
			int i;
			for(i=0;i<ProcessSchedule.needPageNum;i++) {
				if(logicAddr == pfl[i].getLogic_no()) {
					pfl[i].addInMemTimes();
					p.getPcb().getPage_items()[logicAddr].addInMemTimes();
					
					pfl[i].setLastInMemTime(clock.getTime());
					p.getPcb().getPage_items()[logicAddr].setLastInMemTime(clock.getTime());
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
		return false;
	}

	
	//实现LFU算法（最不经常使用），根据进入内存次数，内存换入次数最少的换出
	public synchronized void LFU(Process p) {
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
		
		//更改页表内容
		p.getPcb().getPage_items()[logicAddr].setPageFrameNo(page_frames[minIndex].getPage_frame_no());

		//换出页表
		if(tempLogic != -1) {
			//更改页表内容
			p.getPcb().getPage_items()[tempLogic].setInMemTimes(tempTimes);
			
			p.getPcb().getPage_items()[tempLogic].setPageFrameNo(-1);
		}
		
	}
	
	//实现LRU算法（最近最少使用），根据上次进入内存时间，最早的页面换出
	public synchronized void LRU(Process p) {
		PageFrameLRU[] page_frames = p.getPcb().getPage_frame_nums();
		int logicAddr = p.getPcb().getInstruc().getL_Address();
		int minVal=page_frames[0].getLastInMemTime(),minIndex=0;
		for(int i=1;i<page_frames.length;i++) {
			if(minVal > page_frames[i].getLastInMemTime()) {
				minVal = page_frames[i].getLastInMemTime();
				minIndex = i;
			}
		}
		//保存换出页框的页表项信息
		int tempLogic = page_frames[minIndex].getLogic_no();
		int tempTimes = page_frames[minIndex].getInMemTimes();
		
		
		
		//换入页表项
		page_frames[minIndex].setLogic_no(logicAddr);
		page_frames[minIndex].setInMemTimes(p.getPcb().getPage_items()[logicAddr].getInMemTimes());
		//page_frames[minIndex].setLastInMemTime(clock.getTime());
		
		//更改页表内容
		p.getPcb().getPage_items()[logicAddr].setPageFrameNo(page_frames[minIndex].getPage_frame_no());
		//p.getPcb().getPage_items()[logicAddr].setLastInMemTime(clock.getTime());
		
		//换出页表
		if(tempLogic != -1) {
			//更改页表内容
			p.getPcb().getPage_items()[tempLogic].setInMemTimes(tempTimes);
			
			p.getPcb().getPage_items()[tempLogic].setPageFrameNo(-1);
			
			p.getPcb().getPage_items()[tempLogic].setLastInMemTime(-1);
		}
		
	}
	
	
	//处理缺页中断，调入内存
	public synchronized void readPageFromDisk() {
		if(ProcessSchedule.getBlockQ3().size()>0) {
			Process p = ProcessSchedule.getBlockQ3().get(0);
			if(p.isLackPage()) {
				//System.out.println("从磁盘调入内存");
				
				if(!p.isFinishLRUTag()) {
					//要把逻辑地址和页框号统一起来
					LRU(p);
					System.out.println("从磁盘调入内存-----"+(++times));
					p.setFinishLRUTag(true);
				}
				//不再缺页
				//p.setLackPage(false);
			}
		}
	}
	
	
	@Override
	public void run() {
		
		while(!om.isShutdown()) {
			if(!om.isPause()) {
				readPageFromDisk();
				
				clock.passOneSec();
			}
		}
	}
	
}
