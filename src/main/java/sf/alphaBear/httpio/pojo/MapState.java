package sf.alphaBear.httpio.pojo;

import java.util.List;

public class MapState {
	AI ai;
	List<Wall> walls;
	List<Job> jobs;
	
	public AI getAi() {
		return ai;
	}
	public void setAi(AI ai) {
		this.ai = ai;
	}
	public List<Wall> getWalls() {
		return walls;
	}
	public void setWalls(List<Wall> walls) {
		this.walls = walls;
	}
	public List<Job> getJobs() {
		return jobs;
	}
	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}
	
	
}
