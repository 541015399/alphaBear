package sf.alphaBear.httpio.pojo;

public class Job {
	int x;
	int y;
	int value;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "ai=[x=" + x + ",y=" + y + ",v="  + value + "]";
	}
}
