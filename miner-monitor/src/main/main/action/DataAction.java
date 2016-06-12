package action;

import entity.Data;
import entity.Task;
import service.DataDAO;
import service.TaskDAO;
import service.impl.DataDAOImpl;
import service.impl.TaskDAOImpl;

import java.util.List;

/**
 * Data Action类
 */
public class DataAction extends SuperAction{
    //实现查询所有workspace的动作
    public String query(){
        DataDAO ddao = new DataDAOImpl();
        List<Data> list = ddao.queryAllData();
        //放进session中
        if(list != null && list.size()>0){
            session.setAttribute("data_list", list);
        }
        return "data_query_success";
    }

    //删除工作空间动作
    public String delete(){
        DataDAO ddao = new DataDAOImpl();
        String id = request.getParameter("id");
        ddao.deleteData(Integer.valueOf(id));  //调用删除方法
        //没有考虑删除失败的情况
        return "delete_success";
    }

    //增加工作空间
    public String add(){
        DataDAO ddao = new DataDAOImpl();
        Data d = new Data();
        d.setWid(request.getParameter("wid"));
        d.setPid(request.getParameter("pid"));
        d.setTid(request.getParameter("tid"));
        d.setDataid(request.getParameter("dataid"));
        d.setDescription(request.getParameter("description"));
        d.setProperty(request.getParameter("property"));
        d.setRowKey(request.getParameter("rowKey"));
        d.setForeignKey(request.getParameter("foreignKey"));
        d.setForeignValue(request.getParameter("foreignValue"));
        d.setLink(request.getParameter("link"));
        d.setProcessWay(request.getParameter("processWay"));
        d.setLcondition(request.getParameter("lcondition"));
        ddao.addData(d);

        return "add_success";
    }

    //修改工作空间动作
    public  String modify(){
        //获得传递过来的工作空间id
        int id = Integer.valueOf(request.getParameter("id"));
        DataDAO ddao = new DataDAOImpl();
        Data data = ddao.queryDataByWid(id);
        //保存在会话中
        session.setAttribute("modify_data", data);

        return "modify_success";
    }

    //保存修改的工作空间
    public String save(){
        DataDAO ddao = new DataDAOImpl();
        Data d = new Data();
        d.setId(Integer.valueOf(request.getParameter("id")));
        d.setWid(request.getParameter("wid"));
        d.setPid(request.getParameter("pid"));
        d.setTid(request.getParameter("tid"));
        d.setDataid(request.getParameter("dataid"));
        d.setDescription(request.getParameter("description"));
        d.setProperty(request.getParameter("property"));
        d.setRowKey(request.getParameter("rowKey"));
        d.setForeignKey(request.getParameter("foreignKey"));
        d.setForeignValue(request.getParameter("foreignValue"));
        d.setLink(request.getParameter("link"));
        d.setProcessWay(request.getParameter("processWay"));
        d.setLcondition(request.getParameter("lcondition"));

        ddao.updateData(d);

        return "update_success";
    }
}
