package hardware;

public class AddressLine {
	//��ַ��16λʹ��short���͵ı�������
	private short addr;
	
	public AddressLine() {
		this.addr = 0;
	}

	//synchronized�ؼ������ڱ�֤��ִ����һ����ʱִ�����Σ���ֹ���̲߳�������
	public synchronized short getAddr() {
		return addr;
	}

	public synchronized void setAddr(short addr) {
		this.addr = addr;
	}
	
}
