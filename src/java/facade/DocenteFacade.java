/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package facade;

import javax.ejb.Stateless;
import modelo.Docente;
import controller.HibernateUtil;
import org.hibernate.SessionFactory;

/**
 *
 * @author vinivcp
 * 
 */
@Stateless
public class DocenteFacade extends AbstractFacade<Docente> {
    
    @Override
    protected SessionFactory getSessionFactory(){
        return HibernateUtil.getSessionFactory();
    }
    
    public DocenteFacade(){
        super(Docente.class);
    }
}
