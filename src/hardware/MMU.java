package hardware;

import java.util.List;

import kernel.Instruction;
import kernel.PageItem;
import kernel.Process;

/**
 * 内存管理单元，为每个进程分配页框，以及缺页处理等
 *
 */


public class MMU {
	
	private RAM ram;
	
	private ROM rom;
	
	public MMU() {
		ram = new RAM();
		rom = new ROM();
	}
	
	//为进程分配页框
	public synchronized static void givePageFrame(Process p) {
		int num = 0;
		for(int i=RAM.USER_AREA_START_NO;i<RAM.USER_AREA_NUM+RAM.USER_AREA_START_NO;i++) {
			if(num>2) {
				//分配完成
				break;
			}
			if(!RAM.getAllBlocks()[i]) {
				p.getPcb().getPage_frame_nums()[num].setPage_frame_no(i);
				RAM.getAllBlocks()[i] = true;
				num++;
				System.out.println("分配页框："+i);
			}
		}
	}
	
	public synchronized static void giveDiskAddr(Process p) {
		//将进程所需逻辑页号映射到交换区磁盘块号上
		PageItem[] pi = p.getPcb().getPage_items();
		List<Instruction> is = p.getInstructions();
		
		for(int i=0;i<is.size();i++) {
			if(pi[is.get(i).getL_Address()].getDiskBlockNo() == -1) {
				for(int k=0;k<ROM.getChange_used().length;k++) {
					if(!ROM.getChange_used()[k]) {
						pi[is.get(i).getL_Address()].setDiskBlockNo(k);
						ROM.getChange_used()[k] = true;
						break;
					}
				}
			}
		}
	}
	
	
	//将占用的内存释放回收掉
	public synchronized static void recyclePageFrame(Process p) {
		for(int i=0;i<p.getPcb().getPage_frame_nums().length;i++) {
			
			//释放内存物理块空间
			RAM.getAllBlocks()[p.getPcb().getPage_frame_nums()[i].getPage_frame_no()] = false;
			
			//修改快表
			p.getPcb().getPage_frame_nums()[i].setPage_frame_no(-1);
			//修改页表项
			p.getPcb().getPage_items()[i].setPageFrameNo(-1);
		}
	}
	
	//将占用的磁盘交换区释放回收掉
	public synchronized static void recycleDiskAddr(Process p) {
		for(int i=0;i<p.getPcb().getPage_items().length;i++) {
			if(p.getPcb().getPage_items()[i].getDiskBlockNo()!=-1) {
				ROM.getChange_used()[p.getPcb().getPage_items()[i].getDiskBlockNo()] = false;
				p.getPcb().getPage_items()[i].setDiskBlockNo(-1);
			}
		}
	}
	
}
