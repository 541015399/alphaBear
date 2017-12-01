package sf.alphaBear;

public enum Env {
	TEST("test"), REAL("competition"), BENCH("benchmark");
	final String label;
	private Env(String label) {
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
	
}