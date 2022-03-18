package hardware;

import java.io.*;

/**
 * 外存磁盘类
 *
 */

public class ROM {
	
	/**
	 * cylinder是表示磁盘的柱面数量
	 */
	private static final int CYLINDER_NUM = 10;
	
	/**
	 * track是表示磁盘中一个柱面上的磁道的数量
	 */
	private static final int TRACK_NUM = 32;
	
	/**
	 * sector是表示磁盘的中一个磁道的扇区数
	 */
	private static final int SECTOR_NUM = 64;
	
	
	//交换区数量
	private static final int CHANGE_NUM = 256;
	
	
	//物理块大小为1024B
	private static final int BLOCK_SIZE = 1024;

	//基本数据单元
	private static byte[] data_unit;
	
	//磁盘物理块的使用情况,true为已经使用，false为还未被使用
	private static boolean[] block_used;
	
	//磁盘交换区使用情况，true为已经使用，false为还未被使用
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



	//从保存的磁盘文件里面读取存入到当前的磁盘上，来初始化外存
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
						//第一次启动系统读取到磁盘当中
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
