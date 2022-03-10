package hardware;

public class RAM {
	
	//����ҳ������飩��������
	static final int PAGE_NUM = 32;
	
	//����ҳ������飩��С����
	static final int PAGE_SIZE = 1024;
	
	//ҳ���ſ�ʼ�Ŀ�λ��
	static final int PAGE_LIST_START_NO = 0;
	//ҳ��ռ�ÿ���
	static final int PAGE_LIST_NUM = 1;
	
	//�û�����ſ�ʼ�Ŀ�λ��
	static final int USER_AREA_START_NO = 1;
	//�û���ռ�õĿ���
	static final int USER_AREA_NUM = 24;
	
	
	//��ŵ����ݵ�Ԫ
	private static byte[] data_unit;
	
	//�ڴ��û������������������Ӧ��ţ�1-24��
	public static boolean[] user_distrib;
	
	
	
	public RAM() {
		//��ʼ�����ݵ�Ԫ
		data_unit = new byte[PAGE_NUM * PAGE_SIZE];
		for(int k=0;k<PAGE_NUM * PAGE_SIZE;k++) {
			//ȫ����ʼ��ΪFF
			data_unit[k] = -1;
		}
		//�û����������������ʼ��
		user_distrib = new boolean[USER_AREA_NUM];
		for(int i=0;i<USER_AREA_NUM;i++) {
			user_distrib[i] = false;
		}
	}
	
	
	//��ȡ�û��������������
	public static boolean[] getUser_distrib() {
		return user_distrib;
	}



	//�����ٴ�����synchronized�ؼ�����Ϊ���ڶ��߳��¶��ڴ�Ĳ�����ͬʱ������ֹ����
	public synchronized void writeRAM(AddressLine addr,DataLine data) {
		//addrΪ�ڴ��ʵ�������ַ��������涨����������ż��(�������ݴ洢��ԪΪ˫�ֽ�),��ȡֵ��ΧΪ0-16383,�ö����Ʊ�ʾ����Ҫ14λ
		//data����Ҫд���ڴ������
		if(addr.getAddr()<0 || addr.getAddr()>16383) {
			System.out.println("�ڴ�д��ʧ�ܣ��ڴ��ַ����ȷ");
			return;
		}
		//С��ģʽ�洢���ڴ��еĸߵ�ַ�����ݵĸ��ֽ�����
		data_unit[addr.getAddr()] = (byte)data.getData();
		data_unit[addr.getAddr()] = (byte)(data.getData()>>8);
	}
	
	public synchronized short readRAM(AddressLine addr) {
		if(addr.getAddr()<0 || addr.getAddr()>16383) {
			System.out.println("�ڴ�д��ʧ�ܣ��ڴ��ַ����ȷ");
			return -1;
		}
		short low_data = (short) (data_unit[addr.getAddr()] & 0x00FF);
		short high_data = (short) (data_unit[addr.getAddr()+1] & 0xFF00);
		return (short) (high_data | low_data);
	}
}
