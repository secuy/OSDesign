package hardware;

import java.util.List;

import kernel.Instruction;
import kernel.PageItem;
import kernel.Process;

/**
 * �ڴ����Ԫ��Ϊÿ�����̷���ҳ���Լ�ȱҳ�����
 *
 */


public class MMU {
	
	private RAM ram;
	
	private ROM rom;
	
	public MMU() {
		ram = new RAM();
		rom = new ROM();
	}
	
	//Ϊ���̷���ҳ��
	public synchronized static void givePageFrame(Process p) {
		int num = 0;
		for(int i=RAM.USER_AREA_START_NO;i<RAM.USER_AREA_NUM+RAM.USER_AREA_START_NO;i++) {
			if(num>2) {
				//�������
				break;
			}
			if(!RAM.getAllBlocks()[i]) {
				p.getPcb().getPage_frame_nums()[num].setPage_frame_no(i);
				RAM.getAllBlocks()[i] = true;
				num++;
				System.out.println("����ҳ��"+i);
			}
		}
	}
	
	public synchronized static void giveDiskAddr(Process p) {
		//�����������߼�ҳ��ӳ�䵽���������̿����
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
	
	
	//��ռ�õ��ڴ��ͷŻ��յ�
	public synchronized static void recyclePageFrame(Process p) {
		for(int i=0;i<p.getPcb().getPage_frame_nums().length;i++) {
			
			//�ͷ��ڴ������ռ�
			RAM.getAllBlocks()[p.getPcb().getPage_frame_nums()[i].getPage_frame_no()] = false;
			
			//�޸Ŀ��
			p.getPcb().getPage_frame_nums()[i].setPage_frame_no(-1);
			//�޸�ҳ����
			p.getPcb().getPage_items()[i].setPageFrameNo(-1);
		}
	}
	
	//��ռ�õĴ��̽������ͷŻ��յ�
	public synchronized static void recycleDiskAddr(Process p) {
		for(int i=0;i<p.getPcb().getPage_items().length;i++) {
			if(p.getPcb().getPage_items()[i].getDiskBlockNo()!=-1) {
				ROM.getChange_used()[p.getPcb().getPage_items()[i].getDiskBlockNo()] = false;
				p.getPcb().getPage_items()[i].setDiskBlockNo(-1);
			}
		}
	}
	
}
