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
	public static void givePageFrame(PCB pcb) {
		int num = 0;
		for(int i=0;i<RAM.USER_AREA_NUM;i++) {
			if(num>2) {
				//分配完成
				break;
			}
			if(!RAM.getUser_distrib()[i]) {
				pcb.getPage_frame_nums()[num].setPage_frame_no(i);
				RAM.getUser_distrib()[i] = true;
				num++;
				System.out.println("分配页框："+i);
			}
		}
	}
}
