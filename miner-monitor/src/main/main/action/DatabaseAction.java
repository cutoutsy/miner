package action;

import com.opensymphony.xwork2.ModelDriven;
import entity.Servers;
import service.DatabaseDAO;
import service.impl.DatabaseDAOImple;
import utils.IOUtil;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class DatabaseAction extends SuperAction implements ModelDriven<Servers> {

    private Servers servers = new Servers();

    //数据库连接操作
    public String connect(){
        DatabaseDAO datadao = new DatabaseDAOImple();
        if(datadao.connSqlServer(servers)){


            session.setAttribute("severconn", datadao.getSqlServerConnection(servers));

            session.setAttribute("serverconncted", servers);
            //session.setAttribute("validateCode", user.getGcode());
            //返回一个视图
            return "connect_success";
        }else{
            return "connect_failure";
        }
    }

    //选择数据库连接
    public String choconnect(){
        DatabaseDAO datadao = new DatabaseDAOImple();
        String choStr = request.getParameter("hostip")+":"+request.getParameter("hostport")+":"+request.getParameter("servername");
        String base = System.getProperty("user.dir");
        Set<String> serversSet = IOUtil.readFileToSet(base + "/servers.txt", "utf-8");
        Iterator it = serversSet.iterator();
        while (it.hasNext()){
            String tempStr = it.next().toString();
            if(tempStr.contains(choStr)) {
                Servers oneServer = new Servers();
                oneServer.setHostip(tempStr.split(":")[0]);
                oneServer.setHostport(tempStr.split(":")[1]);
                oneServer.setServername(tempStr.split(":")[2]);
                oneServer.setServerpasswd(tempStr.split(":")[3]);
                if(datadao.connSqlServer(oneServer)){
                    session.setAttribute("severconn", datadao.getSqlServerConnection(oneServer));
                    session.setAttribute("serverconncted", oneServer);
                }
                return "connect_success";
            }
        }
        return "connect_failure";
    }

    //断开连接
    public String unconnect(){
        //request.getParameter("hostip");
        Connection conn = (Connection) session.getAttribute("severconn");
        try {
            conn.close();
            session.removeAttribute("severconn");
            session.removeAttribute("serverconncted");
            return "connected";
        }catch (Exception ex){
            ex.printStackTrace();
            return "connect_failure";
        }

    }


    //数据库连接并保存操作
    public String connectSave(){
        DatabaseDAO datadao = new DatabaseDAOImple();
        if(datadao.connSqlServer(servers)){
            session.setAttribute("severconn", datadao.getSqlServerConnection(servers));
            session.setAttribute("serverconncted", servers);

            String base = System.getProperty("user.dir");

            String writeStr = servers.getHostip()+":"+servers.getHostport()+":"+servers.getServername()+":"+servers.getServerpasswd();
            IOUtil.createFile(base + "/servers.txt", false);

            if(!IOUtil.readFileToSet(base + "/servers.txt", "utf-8").contains(writeStr)){
                IOUtil.writeFile(base + "/servers.txt", writeStr, true, "utf-8");
            }

            //新建server连接时,更新session里面保存的server
            Set<String> serversSet = IOUtil.readFileToSet(base + "/servers.txt", "utf-8");
            Iterator it = serversSet.iterator();
            List<Servers> serverlist = new ArrayList<Servers>();
            while (it.hasNext()){
                String tempStr = it.next().toString();
                Servers oneServer = new Servers();
                oneServer.setHostip(tempStr.split(":")[0]);
                oneServer.setHostport(tempStr.split(":")[1]);
                oneServer.setServername(tempStr.split(":")[2]);
                oneServer.setServerpasswd(tempStr.split(":")[3]);
                serverlist.add(oneServer);
            }
            session.setAttribute("serversave", serverlist);

            return "connect_success";
        }else{
            return "connect_failure";
        }
    }


    //删除已保存的连接信息
    public String delsave(){
        String choStr = request.getParameter("hostip")+":"+request.getParameter("hostport")+":"+request.getParameter("servername");
        String base = System.getProperty("user.dir");
        Set<String> serversSet = IOUtil.readFileToSet(base + "/servers.txt", "utf-8");
        Iterator it = serversSet.iterator();
        while (it.hasNext()){
            String tempStr = it.next().toString();
            if(tempStr.contains(choStr)) {
                serversSet.remove(tempStr);
            }
        }

        //删除文件
        IOUtil.createFile(base + "/servers.txt", true);

        //重新写入
        Iterator ita = serversSet.iterator();
        while (ita.hasNext()){
            IOUtil.writeFile(base + "/servers.txt", ita.next().toString(), true, "utf-8");
        }

        List<Servers> serverlist = new ArrayList<Servers>();
        while (ita.hasNext()){
            String tempStr = ita.next().toString();
            Servers oneServer = new Servers();
            oneServer.setHostip(tempStr.split(":")[0]);
            oneServer.setHostport(tempStr.split(":")[1]);
            oneServer.setServername(tempStr.split(":")[2]);
            oneServer.setServerpasswd(tempStr.split(":")[3]);
            serverlist.add(oneServer);
        }
        session.setAttribute("serversave", serverlist);

        return "connect_save";

    }

    public static void updataSessionSaveServer(HttpSession session){
        String base = System.getProperty("user.dir");
        Set<String> serversSet = IOUtil.readFileToSet(base + "/servers.txt", "utf-8");
        Iterator it = serversSet.iterator();
        List<Servers> serverlist = new ArrayList<Servers>();
        while (it.hasNext()){
            String tempStr = it.next().toString();
            Servers oneServer = new Servers();
            oneServer.setHostip(tempStr.split(":")[0]);
            oneServer.setHostport(tempStr.split(":")[1]);
            oneServer.setServername(tempStr.split(":")[2]);
            oneServer.setServerpasswd(tempStr.split(":")[3]);
            serverlist.add(oneServer);
        }
        session.setAttribute("serversave", serverlist);
    }


    public Servers getModel() {
        return this.servers;
    }
}
