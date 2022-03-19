package kernel;

import other.OSManage;
import ui.ProcessUI;
import hardware.RAM;

/**
 * 
 * 用于缓冲区读取的生产者消费者问题：生产者
 */

public class Producer {

	
	PV empty;   //可用空闲缓冲区个数
	PV full;   //缓冲区满的个数
	PV mutex;   //临界区互斥信号量
	
	public Producer(PV e,PV f,PV m) {
		empty = e;
		full = f;
		mutex = m;
	}
	
	public void appendData(Process p) {
		empty.P();   //消耗一个空闲缓冲区
		mutex.P();   //进入临界区
		//生产数据到内存缓冲区上
		//System.out.println("生产一个数据到内存缓冲区上----");
		String s = ProcessUI.getClock().getTime()+":[拷贝入缓冲区]";
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
