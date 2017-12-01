package sf.alphaBear.agent.db;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import sf.alphaBear.Point;
import sf.alphaBear.agent.algo.AstarAlgoByNeo4j;
import sf.alphaBear.agent.algo.SchedulePath;

public class LookUpTable {
	GridStateDb db;
	AstarAlgoByNeo4j algo;
	ConcurrentHashMap<String, SchedulePath> pathTbl = new ConcurrentHashMap<>(200 * 200);
	public final static String lookUpKey(int fx, int fy, int tx, int ty) {
		return "f_x"+fx + "_y" + fy + "_t_x" + tx + "_y" + ty; 
	}
	public SchedulePath findPath(int fx, int fy, int tx, int ty) {
		String key = lookUpKey(fx, fy, tx, ty);
		SchedulePath path = pathTbl.get(key);
		if (path==null) {
			path = algo.findPath(fx, fy, tx, ty);
			pathTbl.put(key, path);
		}
		return path;
	}
	public LookUpTable(GridStateDb db) {
		this.db = db;
		this.algo = new AstarAlgoByNeo4j(db);
		final Set<Point> points = db.getAllValidPoints();
		points.stream().map(f->{
			for(Point p: points) {
				SchedulePath path = algo.findPath(f.getX(), f.getY(), p.getX(), p.getY());
				String key = lookUpKey(f.getX(), f.getY(), p.getX(), p.getY());
				pathTbl.put(key, path);
			}
			return 1;
		}).count();
	}
}
