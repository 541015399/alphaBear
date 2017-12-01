package sf.alphaBear;

public class Point implements Comparable<Point> {
	int x;
	int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
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

	@Override
	public String toString() {
		return "(x=" +x +",y=" + y+ ")";
	}
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	@Override
	public int compareTo(Point o) {
		return toString().compareTo(o.toString());
	}
}
