package kernel;

import hardware.Clock;

/**
 * ȱҳ�жϴ���
 *
 */

public class LackPageInterrupt extends Thread {
	private static Clock clock;
	
	
	public LackPageInterrupt() {
		clock = new Clock();
	}
	
	//����Ƿ�ȱҳ,����ֵΪfalse��Ϊ����ȱҳ����֮ȱҳ
	public static boolean checkLackPage(Process p) {
		//�õ���ǰ����ִ�е�ָ��
		int logicAddr = p.getPcb().getInstruc().getL_Address();
		PageFrameLRU[] pfl = p.getPcb().getPage_frame_nums();
		int i;
		for(i=0;i<ProcessSchedule.needPageNum;i++) {
			if(logicAddr == pfl[i].getLogic_no()) {
				pfl[i].addInMemTimes();
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

	
	//ʵ��LRU�㷨
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
		//���滻��ҳ���ҳ������Ϣ
		int tempLogic = page_frames[minIndex].getLogic_no();
		int tempTimes = page_frames[minIndex].getInMemTimes();
		
		
		//����ҳ����
		page_frames[minIndex].setLogic_no(logicAddr);
		page_frames[minIndex].setInMemTimes(p.getPcb().getPage_items()[logicAddr].getInMemTimes());

		//����ҳ��
		if(tempLogic != -1) {
			p.getPcb().getPage_items()[tempLogic].setInMemTimes(tempTimes);
		}
	}
	
	//����ȱҳ�жϣ������ڴ�
	public void readPageFromDisk() {
		if(ProcessSchedule.getBlockQ3().size()>0) {
			Process p = ProcessSchedule.getBlockQ3().get(0);
			if(p.isLackPage()) {
				System.out.println("�Ӵ��̵����ڴ�");
				
				//Ҫ���߼���ַ��ҳ���ͳһ����
				LRU(p);
				//����ȱҳ
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
