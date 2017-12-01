package sf.alphaBear;

public enum Env {
	TEST("test"), REAL("competition");
	final String label;
	private Env(String label) {
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
	
}