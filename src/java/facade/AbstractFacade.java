/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package facade;


import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

/**
 *
 * @author vinivcp
 * @param <T>
 */
public abstract class AbstractFacade<T> {
    
    private final Class<T> entityClass;
    
    public AbstractFacade (Class<T> entityClass){
        this.entityClass = entityClass;
    }
    
    protected abstract SessionFactory getSessionFactory();
    
    
    /**
    *     
    * 
    * @param entity
    */
    public void save(T entity){
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        
    
    }
    
    /**
     *     
     * 
     * @param entity
    */
    public void edit(T entity){
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(entity);
        transaction.commit();
        session.close();
    }
    
    /**
     *     
     * 
     * @param entity
    */
    public void remove(T entity){
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();
        session.close();
    }
    
    /**
     *     
     * 
     * @param id
     * @return 
    */
    public T find(Long id){
        Session session = getSessionFactory().openSession();
        T entity = (T) session.get(entityClass, id);
        session.close();
        return entity;
    }
    
    /**
     *     
     * 
     * @return 
    */
    public List<T> findAll(){
        Session session = getSessionFactory().openSession();
        Criteria crit = session.createCriteria(entityClass);
        crit.setMaxResults(50);
        List results = crit.list();
        session.close();
        return results;
    }
    
    /**
     *     
     * 
     * @param range
     * @return 
    */
    public List<T> findRange(int[] range){
        Session session = getSessionFactory().openSession();
        Criteria crit = session.createCriteria(entityClass);
        crit.setMaxResults(range[1] - range[0]);
        crit.setFirstResult(range[0]);
        List results = crit.list();
        session.close();
        return results;
    }
    
    /**
     *     
     * 
     * @return 
    */
    public int count(){
        Session session = getSessionFactory().openSession();
        Criteria crit = session.createCriteria(entityClass);
        int count = ((Number)crit.setProjection(Projections.rowCount())
                .uniqueResult()).intValue();
        session.close();
        return count;
    }
}
