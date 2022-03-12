package hardware;

import kernel.PCB;

/**
 * 内存管理单元，为每个进程分配页框，以及缺页处理等
 *
 */


public class MMU {
	
	private RAM ram;
	
	public MMU() {
		ram = new RAM();
	}
	
	//为进程分配页框
	public synchronized static void givePageFrame(PCB pcb) {
		int num = 0;
		for(int i=RAM.USER_AREA_START_NO;i<RAM.USER_AREA_NUM;i++) {
			if(num>2) {
				//分配完成
				break;
			}
			if(!RAM.getAllBlocks()[i]) {
				pcb.getPage_frame_nums()[num].setPage_frame_no(i);
				RAM.getAllBlocks()[i] = true;
				num++;
				System.out.println("分配页框："+i);
			}
		}
	}
	
	//将占用的内存释放回收掉
	public synchronized static void recyclePageFrame(PCB pcb) {
		for(int i=0;i<pcb.getPage_frame_nums().length;i++) {
			
			//释放内存物理块空间
			RAM.getAllBlocks()[pcb.getPage_frame_nums()[i].getPage_frame_no()] = false;
			
			//修改快表
			pcb.getPage_frame_nums()[i].setPage_frame_no(-1);
			//修改页表项
			pcb.getPage_items()[i].setPageFrameNo(-1);
		}
	}
	
}
