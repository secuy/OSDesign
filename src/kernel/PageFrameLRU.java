package kernel;

/**
 * 专门用来存放进程中分配的页框号和对应的逻辑页号和调入内存次数，方便LRU算法的实现
 *
 */

public class PageFrameLRU {
	
	//页框号
	private int page_frame_no;
	
	//逻辑地址号
	private int logic_no;
	
	//调入内存次数
	private int inMemTimes;
	
	public PageFrameLRU() {
		this.page_frame_no = -1;
		this.logic_no = -1;
		this.inMemTimes = 0;
	}

	public int getPage_frame_no() {
		return page_frame_no;
	}

	public void setPage_frame_no(int pageFrameNo) {
		page_frame_no = pageFrameNo;
	}

	public int getLogic_no() {
		return logic_no;
	}

	public void setLogic_no(int logicNo) {
		logic_no = logicNo;
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
}
