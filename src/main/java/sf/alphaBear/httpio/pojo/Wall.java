package sf.alphaBear.httpio.pojo;

public class Wall {
	int x;
	int y;
	
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
	public String myWallKey() {
		return wallKey(x, y);
	}
	public static String wallKey(int x, int y) {
		return "x=" + x + ",y=" + y;
	}
	public static String wallKey(String x, String y) {
		return "x=" + x + ",y=" + y;
	}
	
	@Override
	public String toString() {
		return "wall=[x=" + x + ",y=" + y + "]";
	}
}
