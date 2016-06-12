package service.impl;

import entity.Data;
import entity.Task;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import service.DataDAO;
import service.TaskDAO;

import java.util.List;

/**
 * Data业务逻辑接口的实现类
 */
public class DataDAOImpl implements DataDAO{

    //查询所有workspace信息
    public List<Data> queryAllData() {
        Transaction tx = null;
        List<Data> list = null;
        String hql = "";
        Configuration config = new Configuration().configure();

        config.addClass(Data.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            hql = "from Data";
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

    public Data queryDataByWid(int id) {
        Transaction tx = null;
        Data data = null;
        String hql = "";
        Configuration config = new Configuration().configure();

        config.addClass(Data.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            data = session.get(Data.class, id);
            tx.commit();
            return data;
        }catch (Exception ex){
            ex.printStackTrace();
            tx.commit();
            return data;
        }finally {
            if(tx != null){
                tx = null;
            }
            sessionFactory.close();
        }
    }

    //增加工作空间
    public boolean addData(Data data) {
        Transaction tx = null;
        Configuration config = new Configuration().configure();

        config.addClass(Data.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            session.save(data);
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
    public boolean updateData(Data data) {
        Transaction tx = null;
        Configuration config = new Configuration().configure();

        config.addClass(Data.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            session.update(data);
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
    public boolean deleteData(int id) {
        Transaction tx = null;
        Configuration config = new Configuration().configure();

        config.addClass(Data.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try {
            tx = session.beginTransaction();
            Data data = session.get(Data.class, id);
            session.delete(data);
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
