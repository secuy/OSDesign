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
	public static void givePageFrame(PCB pcb) {
		int num = 0;
		for(int i=0;i<RAM.USER_AREA_NUM;i++) {
			if(num>2) {
				//�������
				break;
			}
			if(!RAM.getUser_distrib()[i]) {
				pcb.getPage_frame_nums()[num].setPage_frame_no(i);
				RAM.getUser_distrib()[i] = true;
				num++;
				System.out.println("����ҳ��"+i);
			}
		}
	}
}
