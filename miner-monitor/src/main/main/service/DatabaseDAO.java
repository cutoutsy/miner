package service;

import entity.Database;
import entity.Pager;
import entity.Regex;

import java.util.List;

/**
 * 集群数据库的业务逻辑接口
 */
public interface DatabaseDAO {
    //查询数据库的资料
    public List<Database> queryAllDatabase();

    //根据查询条件,查询任务正则信息
    public Pager<Database> findDatabase(Database searchModel, int pageNum, int pageSize);

}
