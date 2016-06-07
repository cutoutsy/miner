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
}
