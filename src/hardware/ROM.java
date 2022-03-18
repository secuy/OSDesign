package hardware;

import java.io.*;

/**
 * ��������
 *
 */

public class ROM {
	
	/**
	 * cylinder�Ǳ�ʾ���̵���������
	 */
	private static final int CYLINDER_NUM = 10;
	
	/**
	 * track�Ǳ�ʾ������һ�������ϵĴŵ�������
	 */
	private static final int TRACK_NUM = 32;
	
	/**
	 * sector�Ǳ�ʾ���̵���һ���ŵ���������
	 */
	private static final int SECTOR_NUM = 64;
	
	
	//����������
	private static final int CHANGE_NUM = 256;
	
	
	//������СΪ1024B
	private static final int BLOCK_SIZE = 1024;

	//�������ݵ�Ԫ
	private static byte[] data_unit;
	
	//����������ʹ�����,trueΪ�Ѿ�ʹ�ã�falseΪ��δ��ʹ��
	private static boolean[] block_used;
	
	//���̽�����ʹ�������trueΪ�Ѿ�ʹ�ã�falseΪ��δ��ʹ��
	private static boolean[] change_used;
	

	public ROM() {
		
		data_unit = new byte[CYLINDER_NUM * TRACK_NUM * SECTOR_NUM * BLOCK_SIZE];
		
		block_used = new boolean[CYLINDER_NUM * TRACK_NUM * SECTOR_NUM];
		
		change_used = new boolean[CHANGE_NUM];
		
		try {
			readDataFromDiskFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i=0;i<block_used.length;i++) {
			block_used[i] = false;
		}
		
		for(int i=0;i<change_used.length;i++) {
			change_used[i] = false;
		}
	}
	
	
	
	public static boolean[] getBlock_used() {
		return block_used;
	}

	

	public static boolean[] getChange_used() {
		return change_used;
	}



	//�ӱ���Ĵ����ļ������ȡ���뵽��ǰ�Ĵ����ϣ�����ʼ�����
	public void readDataFromDiskFile() throws IOException {
		File f = null;
		f = new File("Rom\\");
		if(!f.exists()) {
			f.mkdir();
		}
		for(int i=0;i<CYLINDER_NUM;i++) {
			f = new File("Rom\\"+"cylinder_"+i);
			if(!f.exists()) {
				f.mkdir();
			}
			for(int j=0;j<TRACK_NUM;j++) {
				f = new File("Rom\\"+"cylinder_"+i+"\\track_"+j);
				if(!f.exists()) {
					f.mkdir();
				}
				
				for(int k=0;k<SECTOR_NUM;k++) {
					f = new File("Rom\\"+"cylinder_"+i+"\\track_"+j+"\\sector_"+k+".txt");
					if(!f.exists()) {
						//��һ������ϵͳ��ȡ�����̵���
						f.createNewFile();
						FileOutputStream fos = new FileOutputStream(f);
						for(int m=0;m<BLOCK_SIZE;m++) {
							fos.write("FF".getBytes());
							data_unit[i*j*1024 + k] = (byte) 0xFF;
						}
					} else {

					}
				}
			}
			
		}
	}
	
	
}
