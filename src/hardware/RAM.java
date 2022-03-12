package hardware;

public class RAM {
	
	//定义页框（物理块）数量常量
	public static final int PAGE_NUM = 32;
	
	//定义页框（物理块）大小常量
	static final int PAGE_SIZE = 1024;
	
	//页表存放开始的块位置
	public static final int PAGE_LIST_START_NO = 0;
	//页表占用块数
	public static final int PAGE_LIST_NUM = 1;
	
	//用户区存放开始的块位置
	public static final int USER_AREA_START_NO = 1;
	//用户区占用的块数
	public static final int USER_AREA_NUM = 24;
	
	
	//存放的数据单元
	private static byte[] data_unit;
	
	//内存中物理块所有的占用情况
	public static boolean[] all_blocks;
	
	
	
	
	public RAM() {
		//初始化数据单元
		data_unit = new byte[PAGE_NUM * PAGE_SIZE];
		for(int k=0;k<PAGE_NUM * PAGE_SIZE;k++) {
			//全部初始化为FF
			data_unit[k] = -1;
		}
		//所有块的初始化
		all_blocks = new boolean[PAGE_NUM];
		for(int j=0;j<PAGE_NUM;j++) {
			all_blocks[j] = false;
		}
		
	}
	
	
	//获取用户物理块分配情况表
	public static boolean[] getAllBlocks() {
		return all_blocks;
	}



	//这里再次增加synchronized关键字是为了在多线程下对内存的操作不同时做，防止出错
	public synchronized void writeRAM(AddressLine addr,DataLine data) {
		//addr为内存的实际物理地址，在这里规定正数并且是偶数(由于数据存储单元为双字节),则取值范围为0-16383,用二进制表示则需要14位
		//data是需要写入内存的数据
		if(addr.getAddr()<0 || addr.getAddr()>16383) {
			System.out.println("内存写入失败，内存地址不正确");
			return;
		}
		//小端模式存储，内存中的高地址存数据的高字节内容
		data_unit[addr.getAddr()] = (byte)data.getData();
		data_unit[addr.getAddr()] = (byte)(data.getData()>>8);
	}
	
	public synchronized short readRAM(AddressLine addr) {
		if(addr.getAddr()<0 || addr.getAddr()>16383) {
			System.out.println("内存写入失败，内存地址不正确");
			return -1;
		}
		short low_data = (short) (data_unit[addr.getAddr()] & 0x00FF);
		short high_data = (short) (data_unit[addr.getAddr()+1] & 0xFF00);
		return (short) (high_data | low_data);
	}
}
