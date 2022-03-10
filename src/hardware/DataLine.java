package hardware;

public class DataLine {
	//数据线也是16位，用short类型变量容纳
	private short data;
	
	public DataLine() {
		this.data = 0;
	}
	
	//synchronized关键字用于保证在执行这一方法时执行两次，防止多线程产生错误
	public synchronized short getData() {
		return data;
	}

	public synchronized void setData(short data) {
		this.data = data;
	}
	
}
