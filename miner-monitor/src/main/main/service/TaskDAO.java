package service;

import entity.Project;
import entity.Task;

import java.util.List;

/**
 * Task的业务逻辑接口
 */
public interface TaskDAO {

    //查询Workspace的资料
    public List<Task> queryAllTask();

    //根据workspace id查询信息
    public Task queryTaskByWid(int id);

    //添加workspace信息
    public boolean addTask(Task task);

    //修改Workspace信息
    public boolean updateTask(Task task);

    //删除workspace信息
    public boolean deleteTask(int id);
}
