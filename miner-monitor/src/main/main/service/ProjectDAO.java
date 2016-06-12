package service;

import entity.Project;
import entity.Workspace;

import java.util.List;

/**
 * Project的业务逻辑接口
 */
public interface ProjectDAO {

    //查询Workspace的资料
    public List<Project> queryAllProject();

    //根据workspace id查询信息
    public Project queryProjectByWid(int id);

    //添加workspace信息
    public boolean addProject(Project p);

    //修改Workspace信息
    public boolean updateProject(Project p);

    //删除workspace信息
    public boolean deleteProject(int id);
}
