package hardware;

public class DataLine {
	//������Ҳ��16λ����short���ͱ�������
	private short data;
	
	public DataLine() {
		this.data = 0;
	}
	
	//synchronized�ؼ������ڱ�֤��ִ����һ����ʱִ�����Σ���ֹ���̲߳�������
	public synchronized short getData() {
		return data;
	}

	public synchronized void setData(short data) {
		this.data = data;
	}
	
}
