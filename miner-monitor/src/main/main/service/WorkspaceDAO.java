package service;

import entity.Workspace;

import java.util.List;

/**
 * Workspace的业务逻辑接口
 */
public interface WorkspaceDAO {

    //查询Workspace的资料
    public List<Workspace> queryAllWorkspace();

    //根据workspace id查询信息
    public Workspace queryWorkspaceByWid(String wid);

    //添加workspace信息
    public boolean addWorkspace(Workspace wspace);

    //修改Workspace信息
    public boolean updateWorkspace(Workspace wspace);

    //删除workspace信息
    public boolean deleteWorkspace(String wid);
}
