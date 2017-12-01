package sf.alphaBear.agent.algo;

import java.util.ArrayList;
import java.util.List;

import sf.alphaBear.Point;

public class SchedulePath {
	private List<Point> path;
	
	public SchedulePath() {
		path = new ArrayList<>();
	}
	public void addPoint(Point p) {
		path.add(p);
	}
	public List<Point> getPath() {
		return path;
	}
	public void setPath(List<Point> path) {
		this.path = path;
	}
	public Point get(int idx) {
		return path.size()>idx? path.get(idx): null;
	}
	public int maxSteps() {
		return path.size() - 1;
	}
	public void print() {
		StringBuffer sb = new StringBuffer();
		path.forEach(p->sb.append("(" + p.getX() + "," + p.getY()  + ") -> "));
		System.out.println(sb.toString());
	}
}