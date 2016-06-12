package service.impl;

import entity.Project;
import entity.Workspace;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import service.ProjectDAO;
import service.WorkspaceDAO;

import java.util.List;

/**
 * Project业务逻辑接口的实现类
 */
public class ProjectDAOImpl implements ProjectDAO{

    //查询所有workspace信息
    public List<Project> queryAllProject() {
        Transaction tx = null;
        List<Project> list = null;
        String hql = "";
        Configuration config = new Configuration().configure();

        config.addClass(Project.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            hql = "from Project";
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

    public Project queryProjectByWid(int id) {
        Transaction tx = null;
        Project p = null;
        String hql = "";
        Configuration config = new Configuration().configure();

        config.addClass(Project.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            p = session.get(Project.class, id);
            tx.commit();
            return p;
        }catch (Exception ex){
            ex.printStackTrace();
            tx.commit();
            return p;
        }finally {
            if(tx != null){
                tx = null;
            }
            sessionFactory.close();
        }
    }

    //增加工作空间
    public boolean addProject(Project p) {
        Transaction tx = null;
        Configuration config = new Configuration().configure();

        config.addClass(Project.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            session.save(p);
            tx.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tx.commit();
            return false;
        }finally {
            if(tx != null){
                tx = null;
            }
            sessionFactory.close();
        }
    }

    //更新工作空间
    public boolean updateProject(Project p) {
        Transaction tx = null;
        Configuration config = new Configuration().configure();

        config.addClass(Project.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            session.update(p);
            tx.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tx.commit();
            return false;
        }finally {
            if(tx != null){
                tx = null;
            }
            sessionFactory.close();
        }
    }

    //删除工作空间
    public boolean deleteProject(int id) {
        Transaction tx = null;
        Configuration config = new Configuration().configure();

        config.addClass(Project.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try {
            tx = session.beginTransaction();
            Project p = session.get(Project.class, id);
            session.delete(p);
            tx.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tx.commit();
            return false;
        }finally {
            if(tx != null){
                tx = null;
            }
            sessionFactory.close();
        }
    }
}
