package action;

import entity.Workspace;
import service.WorkspaceDAO;
import service.impl.WorkspaceDAOImpl;

import java.util.List;

/**
 * Workspace Action类
 */
public class WorkspaceAction extends SuperAction{
    //实现查询所有workspace的动作
    public String query(){
        WorkspaceDAO wdao = new WorkspaceDAOImpl();
        List<Workspace> list = wdao.queryAllWorkspace();
        //放进session中
        if(list != null && list.size()>0){
            session.setAttribute("workspace_list", list);
        }
        return "workspace_query_success";
    }

    //删除工作空间动作
    public String delete(){
        WorkspaceDAO wdao = new WorkspaceDAOImpl();
        String id = request.getParameter("id");
        wdao.deleteWorkspace(Integer.valueOf(id));  //调用删除方法
        //没有考虑删除失败的情况
        return "delete_success";
    }

    //增加工作空间
    public String add(){
        WorkspaceDAO wdao = new WorkspaceDAOImpl();
        Workspace workspace = new Workspace();
        workspace.setWid(request.getParameter("wid"));
        workspace.setName(request.getParameter("wname"));
        workspace.setDescription(request.getParameter("description"));

        wdao.addWorkspace(workspace);

        return "add_success";
    }

    //修改工作空间动作
    public  String modify(){
        //获得传递过来的工作空间id
        int id = Integer.valueOf(request.getParameter("id"));
        WorkspaceDAO wdao = new WorkspaceDAOImpl();
        Workspace workspace = wdao.queryWorkspaceByWid(id);
        //保存在会话中
        session.setAttribute("modify_workspace", workspace);

        return "modify_success";
    }

    //保存修改的工作空间
    public String save(){
        WorkspaceDAO wdao = new WorkspaceDAOImpl();
        Workspace workspace = new Workspace();
        workspace.setId(Integer.valueOf(request.getParameter("id")));
        workspace.setWid(request.getParameter("wid"));
        workspace.setName(request.getParameter("wname"));
        workspace.setDescription(request.getParameter("description"));

        wdao.updateWorkspace(workspace);

        return "update_success";
    }
}
