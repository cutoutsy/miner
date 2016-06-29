package service.impl;

import entity.ClusterTask;
import entity.Task;
import miner.spider.utils.IOUtil;
import miner.topo.platform.Project;
import miner.utils.RedisUtil;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import redis.clients.jedis.Jedis;
import service.ClusterDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Project业务逻辑接口的实现类
 */
public class ClusterDAOImpl implements ClusterDAO{

    //query cluster tasks info
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
    public boolean addTask(ClusterTask cTask) {
        RedisUtil ru = new RedisUtil();
        Jedis redis = ru.getJedisInstance();

        String key = cTask.getWid()+"-"+cTask.getPid();
        redis.hset("project_executenum", key, cTask.getExecuNum());
        redis.hset("project_cronstate", key, "3");
        redis.hset("project_state", key, cTask.getStatus());

        return true;
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

    //删除集群上的任务信息
    public boolean deleteTask(ClusterTask cTask) {
        RedisUtil ru = new RedisUtil();
        Jedis redis = ru.getJedisInstance();

        String key = cTask.getWid()+"-"+cTask.getPid();

        redis.hdel("project_executenum", key);
        redis.hdel("project_cronstate", key);
        redis.hdel("project_state", key);

        return true;
    }

    //启动任务
    public boolean startTask(ClusterTask cTask){
        RedisUtil ru = new RedisUtil();
        Jedis redis = ru.getJedisInstance();
        String key = cTask.getWid()+"-"+cTask.getPid();
        Project po = new Project(key);
        String path = ServletActionContext.getServletContext().getRealPath("/files");
        System.out.println(path+"=====");
        Set<String> dataSet = IOUtil.readFileToSet(path+"/"+key+".txt", "UTF-8");
        Iterator it = dataSet.iterator();
        while (it.hasNext()){
            String temp = it.next().toString();
            redis.sadd(po.getDatasource(), temp);
            redis.lpush(po.getDatasource()+"1", temp);
        }

        redis.lpush("project_execute", key);

        return true;
    }
}
