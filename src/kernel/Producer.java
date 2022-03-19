package kernel;

import other.OSManage;
import ui.ProcessUI;
import hardware.RAM;

/**
 * 
 * ���ڻ�������ȡ�����������������⣺������
 */

public class Producer {

	
	PV empty;   //���ÿ��л���������
	PV full;   //���������ĸ���
	PV mutex;   //�ٽ��������ź���
	
	public Producer(PV e,PV f,PV m) {
		empty = e;
		full = f;
		mutex = m;
	}
	
	public void appendData(Process p) {
		empty.P();   //����һ�����л�����
		mutex.P();   //�����ٽ���
		//�������ݵ��ڴ滺������
		//System.out.println("����һ�����ݵ��ڴ滺������----");
		String s = ProcessUI.getClock().getTime()+":[�����뻺����]";
		OSManage.messageOutputSystem(s);
		for(int i=RAM.BUFFER_AREA_START_NO;i<RAM.BUFFER_AREA_START_NO+RAM.BUFFER_AREA_NUM;i++) {
			if(RAM.getAllBlocks()[i] == false) {
				RAM.getAllBlocks()[i] = true;
				p.setBufferNo(i);
				break;
			}
		}
		
		mutex.V();
		full.V();
	}
}
