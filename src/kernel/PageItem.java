package kernel;

/**
 * 该类最主要记录一个页表项，包括逻辑页号、页框号（物理块号）、磁盘物理块号
 * 
 */


public class PageItem {
	
	//页号所占位的个数
	public static final int PAGE_BIT_NUM = 5;
	
	//页框号（物理块）所占位的个数
	public static final int PAGE_FRAME_NUM = 5;
	
	
	//逻辑页号
	private int logicPageNo;
	
	//内存物理页框号
	private int pageFrameNo;
	
	//磁盘物理块号
	private int diskBlockNo;
	
	//是否调入内存标志
	private boolean isInMemory;
	
	//被调入内存的次数(用来实现LFU算法，最不经常使用算法)
	private int inMemTimes;

	//上次被调入内存的时间(用来实现LRU算法，最近最少使用算法)
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
