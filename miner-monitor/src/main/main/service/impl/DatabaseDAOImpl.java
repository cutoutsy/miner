package service.impl;

import entity.*;
import miner.spider.utils.DateUtil;
import miner.store.CreateTable;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import service.DatabaseDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * 集群数据库实现类
 */
public class DatabaseDAOImpl implements DatabaseDAO{

    public Pager<Database> findDatabase(Database searchModel, int pageNum, int pageSize){
        List<Database> allDatabase = queryAllDatabase();

        Pager<Database> pager = new Pager<Database>(pageNum, pageSize, allDatabase);

        return pager;
    }

    public Pager<DataSet> findDataSet(DataSet searchModel, int pageNum, int pageSize) {
        List<DataSet> allDataSet = queryAllDataSet(searchModel);

        Pager<DataSet> pager = new Pager<DataSet>(pageNum, pageSize, allDataSet);
        return pager;
    }

    public List<DataSet> queryAllDataSet(DataSet searchModel){
        List<DataSet> relist = new ArrayList<DataSet>();
        String tableNme = searchModel.getTableName();
        List<String> result = CreateTable.getDataSetByTableName(tableNme);
        for (int i = 0; i < result.size(); i++){
            String temp = result.get(i);
            DataSet tempDs = new DataSet(tableNme, temp.split("\\$\\$")[0], DateUtil.TimeStamp2DateNormal(temp.split("\\$\\$")[1], "yyyy-MM-dd HH:mm:ss"), temp.split("\\$\\$")[2], temp.split("\\$\\$")[3]);
            relist.add(tempDs);
        }

        return relist;
    }

    //得到集群所有的数据库信息
    public List<Database> queryAllDatabase() {
        Transaction tx = null;
        List<Data> list = null;
        List<Database> relist = new ArrayList<Database>();
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
        }catch (Exception ex){
            ex.printStackTrace();
            tx.commit();
        }finally {
            if(tx != null){
                tx = null;
            }
            sessionFactory.close();
        }

        for (int i = 0; i < list.size(); i++){
            String tempTableName = list.get(i).getWid()+list.get(i).getPid()+list.get(i).getTid()+list.get(i).getDataid();
            int tempCountRow = CreateTable.rowCount(tempTableName);
            Database tempDb = new Database(tempTableName, tempCountRow);
            relist.add(tempDb);
        }

        return relist;
    }
}
