package service;

import entity.Data;
import entity.Task;

import java.util.List;

/**
 * Data的业务逻辑接口
 */
public interface DataDAO {

    //查询Workspace的资料
    public List<Data> queryAllData();

    //根据workspace id查询信息
    public Data queryDataByWid(int id);

    //添加workspace信息
    public boolean addData(Data data);

    //修改Workspace信息
    public boolean updateData(Data data);

    //删除workspace信息
    public boolean deleteData(int id);
}
