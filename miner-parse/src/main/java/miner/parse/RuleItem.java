package miner.parse;

public class RuleItem {
	final private String path;
	final private String tag;
	final private DataType type;
	final private String name;

	// 有了name,id能不能去掉？
	public RuleItem(String name, String path, String tag, DataType type) {
		this.path = modify_path(path);
		this.tag = tag;
		this.type = type;
		this.name = name;
	}

	public String get_path() {
		return this.path;
	}

	public String get_tag() {
		return this.tag;
	}

	public DataType get_type() {
		return this.type;
	}

	public String get_name() {
		return this.name;
	}

	public String modify_path(String path) {
		String result = "";
		String[] paths = path.split("\\.");
		for (int i = 0; i < paths.length; i++) {
			if (!paths[i].endsWith("_array")) {
				result += (paths[i] + "0.");
			} else {
				result += (paths[i] + ".");
			}
		}
		// System.out.println("\n"+result.substring(0, result.length() - 1));
		return result.substring(0, result.length() - 1);
	}

}
