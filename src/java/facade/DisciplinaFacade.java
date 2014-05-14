/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package facade;

import controller.HibernateUtil;
import javax.ejb.Stateless;
import modelo.Disciplina;
import org.hibernate.SessionFactory;

/**
 *
 * @author vinivcp
 */

@Stateless
public class DisciplinaFacade extends AbstractFacade <Disciplina>{
    
    public DisciplinaFacade(){
        super(Disciplina.class);
    }
    
    @Override
    protected SessionFactory getSessionFactory(){
        return HibernateUtil.getSessionFactory();
    }
    
}
