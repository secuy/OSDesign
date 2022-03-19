package kernel;

import other.OSManage;
import ui.ProcessUI;

/**
 * 
 * PV操作
 */


public class PV {
	
	private int value=0;
	
	public PV() {
		
	}
	
	public PV(int val) {
		this.value = val;
	}
	
	public void setValue(int val) {
		this.value = val;
	}
	
	public int getValue() {
		return value;
	}
	
	public void addValue() {
		value++;
	}
	public void subValue() {
		value--;
	}
	
	public synchronized void P() {
		while(value<=0);
		//信息输出
		String s = ProcessUI.getClock().getTime()+":[P操作]";
		OSManage.messageOutputSystem(s);
		subValue();
	}
	
	public synchronized void V() {
		addValue();
		String s = ProcessUI.getClock().getTime()+":[V操作]";
		OSManage.messageOutputSystem(s);
	}
	
}
