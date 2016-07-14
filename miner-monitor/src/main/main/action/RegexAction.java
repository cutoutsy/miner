package action;

import entity.Pager;
import entity.Regex;
import entity.Workspace;
import service.RegexDAO;
import service.WorkspaceDAO;
import service.impl.RegexDAOImpl;
import service.impl.WorkspaceDAOImpl;

import java.util.List;

/**
 * Regex Action类
 */
public class RegexAction extends SuperAction{
    //实现查询所有workspace的动作
    public String query(){
        RegexDAO rdao = new RegexDAOImpl();
        List<Regex> list = rdao.queryAllRegex();
        //放进session中
        if(list != null && list.size()>0){
            session.setAttribute("regex_list", list);
        }
        return "regex_query_success";
    }

    //删除工作空间动作
    public String delete(){
        RegexDAO rdao = new RegexDAOImpl();
        String id = request.getParameter("id");
        rdao.deleteRegex(Integer.valueOf(id));  //调用删除方法
        //没有考虑删除失败的情况
        return "delete_success";
    }

    //增加工作空间
    public String add(){
        RegexDAO rdao = new RegexDAOImpl();
        Regex regex = new Regex();
        regex.setWid(request.getParameter("wid"));
        regex.setPid(request.getParameter("pid"));
        regex.setTid(request.getParameter("tid"));
        regex.setTagname(request.getParameter("tagname"));
        regex.setPath(request.getParameter("path"));

        rdao.addRegex(regex);

        return "add_success";
    }

    //修改工作空间动作
    public  String modify(){
        //获得传递过来的工作空间id
        int id = Integer.valueOf(request.getParameter("id"));
        RegexDAO rdao = new RegexDAOImpl();
        Regex regex = rdao.queryRegexByWid(id);
        //保存在会话中
        session.setAttribute("modify_regex", regex);

        return "modify_success";
    }

    //保存修改的工作空间
    public String save(){
        RegexDAO rdao = new RegexDAOImpl();
        Regex regex = new Regex();
        regex.setId(Integer.valueOf(request.getParameter("id")));
        regex.setWid(request.getParameter("wid"));
        regex.setPid(request.getParameter("pid"));
        regex.setTid(request.getParameter("tid"));
        regex.setTagname(request.getParameter("tagname"));
        regex.setPath(request.getParameter("path"));

        rdao.updateRegex(regex);

        return "update_success";
    }

    //分页
    public String page(){
        String pageNumStr = request.getParameter("pageNum");
        int pageNum = 1;
        if(pageNumStr != null && !"".equals(pageNumStr.trim())){
            pageNum = Integer.parseInt(pageNumStr);
        }

        int pageSize = 10;

        Regex searchModel = new Regex();
        RegexDAO rdao = new RegexDAOImpl();
        //调用service获取查询结果

        Pager<Regex> result = rdao.findRegex(searchModel, pageNum, pageSize);

        request.setAttribute("result", result);
        return "page_success";
    }
}
