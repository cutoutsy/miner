package miner.parse.data;

public class DataItem {
	/* 四元组 */
	final private int data_id;
	final private int project_id;
	final private int task_id;
	final private int workstation_id;
	/* connection */
	final private String row_key;
	final private String foreign_key;
	final private String foreign_value;
	final private String link;

	/* 实际存放的数据 */

	public DataItem(int data_id, int project_id, int task_id,
			int workstation_id, String row_key, String foreign_key,
			String foreign_value, String link) {
		this.data_id = data_id;
		this.project_id = project_id;
		this.task_id = task_id;
		this.workstation_id = workstation_id;
		this.row_key = row_key;
		this.foreign_key = foreign_key;
		this.foreign_value = foreign_value;
		this.link = link;
	}

	public int get_data_id() {
		return this.data_id;
	}

	public int get_project_id() {
		return this.project_id;
	}

	public int get_task_id() {
		return this.task_id;
	}

	public int get_workstation_id() {
		return this.workstation_id;
	}

	public String get_row_key() {
		return this.row_key;
	}

	public String get_foreign_key() {
		return this.foreign_key;
	}

	public String get_foreign_value() {
		return this.foreign_value;
	}

	public String get_link() {
		return this.link;
	}

}
