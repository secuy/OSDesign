package kernel;

import other.OSManage;
import hardware.Clock;

/**
 * ȱҳ�жϴ���
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
	
	//����Ƿ�ȱҳ,����ֵΪfalse��Ϊ����ȱҳ����֮ȱҳ
	public synchronized static boolean checkLackPage(Process p) {
		//�õ���ǰ����ִ�е�ָ��
		//�������е�ָ�����ȱҳ���
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

					System.out.println("ҳ�����У��߼�ҳ��Ϊ��"+pfl[i].getLogic_no()+"ҳ���Ϊ��"+pfl[i].getPage_frame_no()+"ʹ�ô�����"+pfl[i].getInMemTimes());
					return false;
				}
			}
			//˵��ҳ��û�����У���ô����Ҫ�Ӵ��̶�����Ӧ���ļ�����˼��뵽��������3
			if(i>=ProcessSchedule.needPageNum) {
				p.setLackPage(true);
				InterruptOperator.inInterrupt();
			}
			return true;
		}
		return false;
	}

	
	//ʵ��LFU�㷨�������ʹ�ã������ݽ����ڴ�������ڴ滻��������ٵĻ���
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
		//���滻��ҳ���ҳ������Ϣ
		int tempLogic = page_frames[minIndex].getLogic_no();
		int tempTimes = page_frames[minIndex].getInMemTimes();
		
		
		
		//����ҳ����
		page_frames[minIndex].setLogic_no(logicAddr);
		page_frames[minIndex].setInMemTimes(p.getPcb().getPage_items()[logicAddr].getInMemTimes());
		
		//����ҳ������
		p.getPcb().getPage_items()[logicAddr].setPageFrameNo(page_frames[minIndex].getPage_frame_no());

		//����ҳ��
		if(tempLogic != -1) {
			//����ҳ������
			p.getPcb().getPage_items()[tempLogic].setInMemTimes(tempTimes);
			
			p.getPcb().getPage_items()[tempLogic].setPageFrameNo(-1);
		}
		
	}
	
	//ʵ��LRU�㷨���������ʹ�ã��������ϴν����ڴ�ʱ�䣬�����ҳ�滻��
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
		//���滻��ҳ���ҳ������Ϣ
		int tempLogic = page_frames[minIndex].getLogic_no();
		int tempTimes = page_frames[minIndex].getInMemTimes();
		
		
		
		//����ҳ����
		page_frames[minIndex].setLogic_no(logicAddr);
		page_frames[minIndex].setInMemTimes(p.getPcb().getPage_items()[logicAddr].getInMemTimes());
		//page_frames[minIndex].setLastInMemTime(clock.getTime());
		
		//����ҳ������
		p.getPcb().getPage_items()[logicAddr].setPageFrameNo(page_frames[minIndex].getPage_frame_no());
		//p.getPcb().getPage_items()[logicAddr].setLastInMemTime(clock.getTime());
		
		//����ҳ��
		if(tempLogic != -1) {
			//����ҳ������
			p.getPcb().getPage_items()[tempLogic].setInMemTimes(tempTimes);
			
			p.getPcb().getPage_items()[tempLogic].setPageFrameNo(-1);
			
			p.getPcb().getPage_items()[tempLogic].setLastInMemTime(-1);
		}
		
	}
	
	
	//����ȱҳ�жϣ������ڴ�
	public synchronized void readPageFromDisk() {
		if(ProcessSchedule.getBlockQ3().size()>0) {
			Process p = ProcessSchedule.getBlockQ3().get(0);
			if(p.isLackPage()) {
				//System.out.println("�Ӵ��̵����ڴ�");
				
				if(!p.isFinishLRUTag()) {
					//Ҫ���߼���ַ��ҳ���ͳһ����
					LRU(p);
					System.out.println("�Ӵ��̵����ڴ�-----"+(++times));
					p.setFinishLRUTag(true);
				}
				//����ȱҳ
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
