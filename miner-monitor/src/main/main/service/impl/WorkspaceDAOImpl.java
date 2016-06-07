package service.impl;

import entity.Workspace;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import service.WorkspaceDAO;

import java.util.List;

/**
 * Workspace业务逻辑接口的实现类
 */
public class WorkspaceDAOImpl implements WorkspaceDAO{

    //查询所有workspace信息
    public List<Workspace> queryAllWorkspace() {
        Transaction tx = null;
        List<Workspace> list = null;
        String hql = "";
        Configuration config = new Configuration().configure();

        config.addClass(Workspace.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            hql = "from Workspace";
            Query query = session.createQuery(hql);

            list = query.list();
            tx.commit();
            return list;
        }catch (Exception ex){
            ex.printStackTrace();
            tx.commit();
            return list;
        }finally {
            if(tx != null){
                tx = null;
            }
           sessionFactory.close();
        }

    }

    public Workspace queryWorkspaceByWid(String wid) {
        return null;
    }

    public boolean addWorkspace(Workspace wspace) {
        return false;
    }

    public boolean updateWorkspace(Workspace wspace) {
        return false;
    }

    public boolean deleteWorkspace(String wid) {
        return false;
    }
}
