package action;

import entity.Project;
import entity.Workspace;
import service.ProjectDAO;
import service.WorkspaceDAO;
import service.impl.ProjectDAOImpl;
import service.impl.WorkspaceDAOImpl;

import java.util.List;

/**
 * Project Action类
 */
public class ProjectAction extends SuperAction{
    //实现查询所有workspace的动作
    public String query(){
        ProjectDAO pdao = new ProjectDAOImpl();
        List<Project> list = pdao.queryAllProject();
        //放进session中
        if(list != null && list.size()>0){
            session.setAttribute("project_list", list);
        }
        return "project_query_success";
    }

    //删除工作空间动作
    public String delete(){
        ProjectDAO pdao = new ProjectDAOImpl();
        String id = request.getParameter("id");
        pdao.deleteProject(Integer.valueOf(id));  //调用删除方法
        //没有考虑删除失败的情况
        return "delete_success";
    }

    //增加工作空间
    public String add(){
        ProjectDAO pdao = new ProjectDAOImpl();
        Project p = new Project();
        p.setWid(request.getParameter("wid"));
        p.setPid(request.getParameter("pid"));
        p.setName(request.getParameter("pname"));
        p.setDescription(request.getParameter("description"));
        p.setDatasource(request.getParameter("datasource"));
        p.setSchedule(request.getParameter("schedule"));
        p.setPrecondition(request.getParameter("precondition"));

        pdao.addProject(p);

        return "add_success";
    }

    //修改工作空间动作
    public  String modify(){
        //获得传递过来的工作空间id
        int id = Integer.valueOf(request.getParameter("id"));
        ProjectDAO pdao = new ProjectDAOImpl();
        Project p = pdao.queryProjectByWid(id);
        //保存在会话中
        session.setAttribute("modify_project", p);

        return "modify_success";
    }

    //保存修改的工作空间
    public String save(){
        ProjectDAO pdao = new ProjectDAOImpl();
        Project p = new Project();
        p.setId(Integer.valueOf(request.getParameter("id")));
        p.setWid(request.getParameter("wid"));
        p.setPid(request.getParameter("pid"));
        p.setName(request.getParameter("pname"));
        p.setDescription(request.getParameter("description"));
        p.setDatasource(request.getParameter("datasource"));
        p.setSchedule(request.getParameter("schedule"));
        p.setPrecondition(request.getParameter("precondition"));

        pdao.updateProject(p);

        return "update_success";
    }
}
