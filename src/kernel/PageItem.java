package kernel;

/**
 * ��������Ҫ��¼һ��ҳ��������߼�ҳ�š�ҳ��ţ������ţ�������������
 * 
 */


public class PageItem {
	
	//ҳ����ռλ�ĸ���
	public static final int PAGE_BIT_NUM = 5;
	
	//ҳ��ţ�����飩��ռλ�ĸ���
	public static final int PAGE_FRAME_NUM = 5;
	
	
	//�߼�ҳ��
	private int logicPageNo;
	
	//�ڴ�����ҳ���
	private int pageFrameNo;
	
	//����������
	private int diskBlockNo;
	
	//�Ƿ�����ڴ��־
	private boolean isInMemory;
	
	//�������ڴ�Ĵ���(����ʵ��LFU�㷨�������ʹ���㷨)
	private int inMemTimes;

	//�ϴα������ڴ��ʱ��(����ʵ��LRU�㷨���������ʹ���㷨)
	private int lastInMemTime;
	
	
	public PageItem() {
		this.logicPageNo = -1;
		this.pageFrameNo = -1;
		this.diskBlockNo = -1;
		this.isInMemory = false;
		this.inMemTimes = 0;
		this.lastInMemTime = -1;
	}
	
	public PageItem(int logicPageNo, int pageFrameNo, int diskBlockNo, boolean isInMemory) {
		this.logicPageNo = logicPageNo;
		this.pageFrameNo = pageFrameNo;
		this.diskBlockNo = diskBlockNo;
		this.isInMemory = isInMemory;
		this.inMemTimes = 0;
		this.lastInMemTime = -1;
	}


	public int getLogicPageNo() {
		return logicPageNo;
	}

	public void setLogicPageNo(int logicPageNo) {
		this.logicPageNo = logicPageNo;
	}

	public int getPageFrameNo() {
		return pageFrameNo;
	}

	public void setPageFrameNo(int pageFrameNo) {
		this.pageFrameNo = pageFrameNo;
	}

	public int getDiskBlockNo() {
		return diskBlockNo;
	}

	public void setDiskBlockNo(int diskBlockNo) {
		this.diskBlockNo = diskBlockNo;
	}

	public boolean isInMemory() {
		return isInMemory;
	}

	public void setInMemory(boolean isInMemory) {
		this.isInMemory = isInMemory;
	}

	public int getInMemTimes() {
		return inMemTimes;
	}

	public void setInMemTimes(int inMemTimes) {
		this.inMemTimes = inMemTimes;
	}
	
	public void addInMemTimes() {
		this.inMemTimes++;
	}

	public int getLastInMemTime() {
		return lastInMemTime;
	}

	public void setLastInMemTime(int lastInMemTime) {
		this.lastInMemTime = lastInMemTime;
	}
	
}
