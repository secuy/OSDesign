package kernel;

import hardware.RAM;

/**
 * ���ڻ�������ȡ�����������������⣺������
 */


public class Consumer {
	
	PV empty;   //���ÿ��л���������
	PV full;   //���������ĸ���
	PV mutex;   //�ٽ��������ź���
	

	public Consumer(PV e,PV f,PV m) {
		empty = e;
		full = f;
		mutex = m;
	}
	public void takeData(Process p) {
		full.P();       //��ȡһ����������
		mutex.P();      //�����ٽ���
		
		//���ڴ滺������ȡ������
		//System.out.println("���ڴ滺����ȡ������----");
		RAM.getAllBlocks()[p.getBufferNo()] = false;
		p.setBufferNo(-1);
		
		mutex.V();      //�뿪�ٽ���
		empty.V();      //���������
	}
	
	
}
