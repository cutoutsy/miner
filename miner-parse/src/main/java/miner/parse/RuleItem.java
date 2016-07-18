package miner.parse;

/*
 * @ name:      RuleItem.java
 * @ author:    white write and mafu expand
 * @ info:      表示一个字段的抽取规则，这里的字段最终可能是一个字符串，也可能是一个数组
 * */
public class RuleItem {
	final private String path;
    final private DataType type;
    final private String name;
    /*规则的类型，根据类型判断使用哪种方法*/
    final private PathType path_type;
    /* 只有对于html这个tag变量是有效的,对于Xpath方法，这个变量是无效的 */
    final private String tag;
    /* extra operation */
    private  String extra_operation;
    

    /* 在构造器中间接的派生了tag和type两个变量 */
	public RuleItem(String name, String path) {
        String[] paths_type = path.split("\\$\\$");
        String the_path = "";
        //判断是不是xpath
        if(paths_type[0].equals("xpath")){
            this.path_type = PathType.Xpath;
            for (int i = 1;i<paths_type.length;i++){
                 the_path = paths_type[i];
            }
            this.path = the_path;
            this.type = DataType.STR;
            this.name = name;
            this.tag  = "none";
        }else {
//        System.out.println(extra_paths);
            this.path_type = PathType.Htmlpath;
            int type_flag = 0;
            String[] paths = path.split("\\.");

            for (int i = 0; i < paths.length; i++) {
                if (paths[i].endsWith("_")) {
                    type_flag = 1;
                    break;
                }
            }
            if (type_flag == 0) {
                this.type = DataType.STR;
            } else {
                this.type = DataType.ARRAY;
            }
            String tmp_path = "";
            if (paths[0].startsWith("html")) {
                this.tag = paths[paths.length - 1];//tag原来封装在path的尾部，这里抽出来
                for (int i = 0; i < paths.length - 1; i++) {
                    tmp_path += paths[i] + ".";
                }
                tmp_path = tmp_path.substring(0, tmp_path.length() - 1);
            } else {
                this.tag = "text";
                tmp_path = path;
            }
            this.path = tmp_path;
            this.name = name;
        }
//        System.out.println(this.path+" "+this.name+" "+this.tag+" "+this.type.equals(DataType.ARRAY));
	}

    public RuleItem(String name, String path,String extra_operation){
        this(name,path);
        this.extra_operation=extra_operation;
    }



	public String get_path() {
		return this.path;
	}

	public String get_tag() {
        if (this.tag == "none"){
            System.out.print("正在使用Xpath,注意绕行\n");
        }
		return this.tag;
	}

	public DataType get_type() {
		return this.type;
	}

    public PathType get_path_type(){
        return this.path_type;
    }

	public String get_name() {
		return this.name;
	}

    public String get_extra_operation(){
        return this.extra_operation;
    }
}

