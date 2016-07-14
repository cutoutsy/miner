package action;

import entity.*;
import service.ClusterDAO;
import service.DatabaseDAO;
import service.impl.ClusterDAOImpl;
import service.impl.DatabaseDAOImpl;

import java.util.List;

/**
 * Database Action类
 */
public class DatabaseAction extends SuperAction{

    //实现查询所有数据库的动作
    public String query(){
        DatabaseDAO ddao = new DatabaseDAOImpl();
        List<Database> list = ddao.queryAllDatabase();
        //放进session中
        if(list != null && list.size()>0){
            session.setAttribute("database_list", list);
        }
        return "database_query_success";
    }

    //分页
    public String page(){
        String pageNumStr = request.getParameter("pageNum");
        int pageNum = 1;
        if(pageNumStr != null && !"".equals(pageNumStr.trim())){
            pageNum = Integer.parseInt(pageNumStr);
        }

        int pageSize = 10;

        Database searchModel = new Database();
        DatabaseDAO ddao = new DatabaseDAOImpl();
        //调用service获取查询结果
        Pager<Database> result = ddao.findDatabase(searchModel, pageNum, pageSize);

        request.setAttribute("result", result);
        return "page_database_success";
    }

    //数据分页
    public String datapage(){
        String pageNumStr = request.getParameter("pageNum");
        String tableName = request.getParameter("tableName");
        int pageNum = 1;
        if(pageNumStr != null && !"".equals(pageNumStr.trim())){
            pageNum = Integer.parseInt(pageNumStr);
        }

        int pageSize = 10;

        DataSet searchModel = new DataSet();
        searchModel.setTableName(tableName);
        DatabaseDAO ddao = new DatabaseDAOImpl();
        //调用service获取查询结果
        Pager<DataSet> result = ddao.findDataSet(searchModel, pageNum, pageSize);

        request.setAttribute("result", result);
        return "page_data_success";
    }
}
