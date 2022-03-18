package kernel;

import hardware.RAM;

/**
 * ����������
 */

public class BufferManager {
	
	private PV empty;
	private PV full;
	private PV mutex;
	
	private Producer p;
	private Consumer c;
	
	public BufferManager() {
		empty = new PV(RAM.BUFFER_AREA_NUM);    //�ջ������ʼ�и�
		full = new PV(0);   //���������ʼû��
		mutex = new PV(1);
		
		
		p = new Producer(empty,full,mutex);
		c = new Consumer(empty,full,mutex);
	}

	public Producer getP() {
		return p;
	}

	public Consumer getC() {
		return c;
	}
	
}
