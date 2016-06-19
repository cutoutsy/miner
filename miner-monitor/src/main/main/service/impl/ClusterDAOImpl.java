package service.impl;

import entity.ClusterTask;
import entity.Task;
import miner.utils.RedisUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import redis.clients.jedis.Jedis;
import service.ClusterDAO;
import service.TaskDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Project业务逻辑接口的实现类
 */
public class ClusterDAOImpl implements ClusterDAO{

    //查询所有workspace信息
    public List<ClusterTask> queryAllTask() {
        List<ClusterTask> list = new ArrayList<ClusterTask>();

        RedisUtil ru = new RedisUtil();
        Jedis redis = ru.getJedisInstance();

        List<String> keys = new ArrayList<String>(redis.hkeys("project_executenum"));

        for (int i = 0; i < keys.size(); i++) {
            System.out.println(keys.get(i));
            ClusterTask ct = new ClusterTask();
            ct.setWid(keys.get(i).split("-")[0]);
            ct.setPid(keys.get(i).split("-")[1]);
            ct.setExecuNum(redis.hget("project_executenum", keys.get(i)));
            ct.setStatus(redis.hget("project_state", keys.get(i)));
            System.out.println(ct.toString());
            list.add(ct);
        }

        return list;
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
