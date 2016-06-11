package service.impl;

import entity.Regex;
import entity.Workspace;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import service.RegexDAO;
import service.WorkspaceDAO;

import java.util.List;

/**
 * Workspace业务逻辑接口的实现类
 */
public class RegexDAOImpl implements RegexDAO{

    //查询所有workspace信息
    public List<Regex> queryAllRegex() {
        Transaction tx = null;
        List<Regex> list = null;
        String hql = "";
        Configuration config = new Configuration().configure();

        config.addClass(Regex.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            hql = "from Regex";
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

    public Regex queryRegexByWid(int id) {
        Transaction tx = null;
        Regex regex = null;
        String hql = "";
        Configuration config = new Configuration().configure();

        config.addClass(Regex.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            regex = session.get(Regex.class, id);
            tx.commit();
            return regex;
        }catch (Exception ex){
            ex.printStackTrace();
            tx.commit();
            return regex;
        }finally {
            if(tx != null){
                tx = null;
            }
            sessionFactory.close();
        }
    }

    //增加工作空间
    public boolean addRegex(Regex regex) {
        Transaction tx = null;
        Configuration config = new Configuration().configure();

        config.addClass(Regex.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            session.save(regex);
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
    public boolean updateRegex(Regex regex) {
        Transaction tx = null;
        Configuration config = new Configuration().configure();

        config.addClass(Regex.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try{
            tx = session.beginTransaction();
            session.update(regex);
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
    public boolean deleteRegex(int id) {
        Transaction tx = null;
        Configuration config = new Configuration().configure();

        config.addClass(Regex.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try {
            tx = session.beginTransaction();
            Regex regex = session.get(Regex.class, id);
            session.delete(regex);
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
