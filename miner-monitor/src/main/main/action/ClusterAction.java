package action;

import entity.ClusterTask;
import entity.Task;
import service.ClusterDAO;
import service.TaskDAO;
import service.impl.ClusterDAOImpl;
import service.impl.TaskDAOImpl;

import java.util.List;

/**
 * Task Action类
 */
public class ClusterAction extends SuperAction{
    //实现查询所有workspace的动作
    public String query(){
        ClusterDAO cdao = new ClusterDAOImpl();
        List<ClusterTask> list = cdao.queryAllTask();
        //放进session中
        if(list != null && list.size()>0){
            session.setAttribute("clustertask_list", list);
        }
        return "clustertask_query_success";
    }

    //删除工作空间动作
    public String delete(){
        TaskDAO tdao = new TaskDAOImpl();
        String id = request.getParameter("id");
        tdao.deleteTask(Integer.valueOf(id));  //调用删除方法
        //没有考虑删除失败的情况
        return "delete_success";
    }

    //增加工作空间
    public String add(){
        TaskDAO tdao = new TaskDAOImpl();
        Task t = new Task();
        t.setWid(request.getParameter("wid"));
        t.setPid(request.getParameter("pid"));
        t.setTid(request.getParameter("tid"));
        t.setName(request.getParameter("tname"));
        t.setDescription(request.getParameter("description"));
        t.setUrlpattern(request.getParameter("urlpattern"));
        t.setUrlgenerate(request.getParameter("urlgenerate"));
        t.setIsloop(request.getParameter("isloop"));
        t.setProxy_open(request.getParameter("proxy_open"));
        tdao.addTask(t);

        return "add_success";
    }

    //修改工作空间动作
    public  String modify(){
        //获得传递过来的工作空间id
        int id = Integer.valueOf(request.getParameter("id"));
        TaskDAO tdao = new TaskDAOImpl();
        Task t = tdao.queryTaskByWid(id);
        //保存在会话中
        session.setAttribute("modify_task", t);

        return "modify_success";
    }

    //保存修改的工作空间
    public String save(){
        TaskDAO tdao = new TaskDAOImpl();
        Task t = new Task();
        t.setId(Integer.valueOf(request.getParameter("id")));
        t.setWid(request.getParameter("wid"));
        t.setPid(request.getParameter("pid"));
        t.setTid(request.getParameter("tid"));
        t.setName(request.getParameter("tname"));
        t.setDescription(request.getParameter("description"));
        t.setUrlpattern(request.getParameter("urlpattern"));
        t.setUrlgenerate(request.getParameter("urlgenerate"));
        t.setIsloop(request.getParameter("isloop"));
        t.setProxy_open(request.getParameter("proxy_open"));

        tdao.updateTask(t);

        return "update_success";
    }
}
