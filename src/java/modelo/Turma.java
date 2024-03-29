/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author vinivcp
 */

@Entity
public class Turma implements Serializable{
    
    @ManyToOne
    private Disciplina disciplina;
    @ManyToOne
    private Docente docente;

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public int hashCode(){
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object){
        if(!(object instanceof Turma)){
            return false;
        }
        Turma other = (Turma) object;
        if((this.id == null && other.id != null) || 
                (this.id != null && !this.id.equals(other.id))){
            return false;
        }
      return true;
    }
    
    @Override
    public String toString(){
        return "model.Turma[ id=" + id + "]";
    }
    
}
