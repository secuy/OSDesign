package kernel;

/**
 * 
 * PV²Ù×÷
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
	
	public void P() {
		while(value<=0);
		subValue();
	}
	
	public void V() {
		addValue();
	}
	
}
