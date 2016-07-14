package service.impl;

import entity.Pager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //使用Hibernate实现分页
    public Pager<Regex> findRegex(Regex searchModel, int pageNum, int pageSize) {
        Pager<Regex> result = null;
        Transaction tx = null;

        //存放查询参数
        Map<String, Object> paramMap = new HashMap<String, Object>();

        StringBuffer hql = new StringBuffer("from Regex where 1=1");
        StringBuffer countHql = new StringBuffer("select count(id) from Regex where 1=1");

        //起始索引
        int fromIndex = pageSize * (pageNum - 1);

        //存放所有查询出来的正则对象
        List<Regex> regexList = new ArrayList<Regex>();
        Session session = null;
        SessionFactory sessionFactory = null;
        try {
            Configuration config = new Configuration().configure();

            config.addClass(Regex.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                    .build();
            sessionFactory = config.buildSessionFactory(serviceRegistry);
            session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();

            //获取query对象
            Query hqlQuery = session.createQuery(hql.toString());
            Query countHqlQuery = session.createQuery(countHql.toString());

            //从第几条记录开始查询
            hqlQuery.setFirstResult(fromIndex);

            //一共查询多少条记录
            hqlQuery.setMaxResults(pageSize);

            //获取查询的结果
            regexList = hqlQuery.list();
            //获取总计条数
            List<?> countResult = countHqlQuery.list();
            int totalRecord = ((Number) countResult.get(0)).intValue();
            tx.commit();
            //获取总页数
            int totalPage = totalRecord / pageSize;
            if (totalRecord % pageSize != 0) {
                totalPage++;
            }

            //组装paper对象
            result = new Pager<Regex>(pageSize, pageNum, totalRecord, totalPage, regexList);
        }catch (Exception e){
            tx.commit();
            throw  new RuntimeException("查询所有数据异常!", e);
        }finally {
            if(tx != null){
                tx = null;
            }
            sessionFactory.close();
        }
        return result;
    }
}
