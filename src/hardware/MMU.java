package hardware;

import kernel.PCB;

/**
 * �ڴ����Ԫ��Ϊÿ�����̷���ҳ���Լ�ȱҳ�����
 *
 */


public class MMU {
	
	private RAM ram;
	
	public MMU() {
		ram = new RAM();
	}
	
	//Ϊ���̷���ҳ��
	public synchronized static void givePageFrame(PCB pcb) {
		int num = 0;
		for(int i=RAM.USER_AREA_START_NO;i<RAM.USER_AREA_NUM;i++) {
			if(num>2) {
				//�������
				break;
			}
			if(!RAM.getAllBlocks()[i]) {
				pcb.getPage_frame_nums()[num].setPage_frame_no(i);
				RAM.getAllBlocks()[i] = true;
				num++;
				System.out.println("����ҳ��"+i);
			}
		}
	}
	
	//��ռ�õ��ڴ��ͷŻ��յ�
	public synchronized static void recyclePageFrame(PCB pcb) {
		for(int i=0;i<pcb.getPage_frame_nums().length;i++) {
			
			//�ͷ��ڴ������ռ�
			RAM.getAllBlocks()[pcb.getPage_frame_nums()[i].getPage_frame_no()] = false;
			
			//�޸Ŀ��
			pcb.getPage_frame_nums()[i].setPage_frame_no(-1);
			//�޸�ҳ����
			pcb.getPage_items()[i].setPageFrameNo(-1);
		}
	}
	
}
