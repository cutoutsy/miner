package utils;

import entity.Workplace;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

/**
 * 有关hibernate的工具类
 */
public class HibernateUtil {

    public static boolean saveAndUpdateMemberInfo(List<Workplace> memberseList) {
        Transaction tx = null;

        Configuration config = new Configuration().configure();

        config.addClass(Workplace.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
                .build();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        Session session        = sessionFactory.getCurrentSession();
        try {
            tx = session.beginTransaction();

            for (int i = 0; i < memberseList.size(); i++) {
                Workplace members = session.get(Workplace.class, memberseList.get(i).getId());

                if (members != null) {
                    session.update(members);
                } else {
                    session.save(memberseList.get(i));
                }
            }

            tx.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tx.commit();
            return false;
        }finally {
            sessionFactory.close();
        }
    }
}
