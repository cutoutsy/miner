package service.impl;

import entity.Project;
import entity.Task;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import service.ProjectDAO;
import service.TaskDAO;

import java.util.List;

/**
 * Project业务逻辑接口的实现类
 */
public class TaskDAOImpl implements TaskDAO{

    //查询所有workspace信息
    public List<Task> queryAllTask() {
        Transaction tx = null;
        List<Task> list = null;
        String hql = "";
        Configuration config = new Configuration().configure();

        config.addClass(Task.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            hql = "from Task";
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

    public Task queryTaskByWid(int id) {
        Transaction tx = null;
        Task t = null;
        String hql = "";
        Configuration config = new Configuration().configure();

        config.addClass(Task.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            t = session.get(Task.class, id);
            tx.commit();
            return t;
        }catch (Exception ex){
            ex.printStackTrace();
            tx.commit();
            return t;
        }finally {
            if(tx != null){
                tx = null;
            }
            sessionFactory.close();
        }
    }

    //增加工作空间
    public boolean addTask(Task task) {
        Transaction tx = null;
        Configuration config = new Configuration().configure();

        config.addClass(Task.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            session.save(task);
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
    public boolean updateTask(Task task) {
        Transaction tx = null;
        Configuration config = new Configuration().configure();

        config.addClass(Task.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            session.update(task);
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
    public boolean deleteTask(int id) {
        Transaction tx = null;
        Configuration config = new Configuration().configure();

        config.addClass(Task.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try {
            tx = session.beginTransaction();
            Task task = session.get(Task.class, id);
            session.delete(task);
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
