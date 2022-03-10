package kernel;

/**
 * ר��������Ž����з����ҳ��źͶ�Ӧ���߼�ҳ�ź͵����ڴ����������LRU�㷨��ʵ��
 *
 */

public class PageFrameLRU {
	
	//ҳ���
	private int page_frame_no;
	
	//�߼���ַ��
	private int logic_no;
	
	//�����ڴ����
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
