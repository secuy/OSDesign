package hardware;

public class AddressLine {
	//地址线16位使用short类型的变量容纳
	private short addr;
	
	public AddressLine() {
		this.addr = 0;
	}

	//synchronized关键字用于保证在执行这一方法时执行两次，防止多线程产生错误
	public synchronized short getAddr() {
		return addr;
	}

	public synchronized void setAddr(short addr) {
		this.addr = addr;
	}
	
}
