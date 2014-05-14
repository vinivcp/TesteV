/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import modelo.Turma;

/**
 *
 * @author vinivcp
 */
@Named(value = "turmaController")
@SessionScoped
public class TurmaController implements Serializable {
    
    Turma turma;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    @EJB
    facade.TurmaFacade ejbfacade;
    
    public TurmaController() {
        turma = new Turma();
    }
    
    public String prepareView() {
        turma = (Turma) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    
    public String prepareEdit(){
        turma = (Turma) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String destroy(){
        turma = (Turma) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    private void recreateModel(){
        items = null;
    }
    
    private void performDestroy(){
        try{
            ejbfacade.remove(turma);
            JsfUtil.addSuccessMessage("Turma deletada");
        } catch(Exception e){
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia");
        }
    }
    
    public Turma getSelected(){
        if (turma == null){
            turma = new Turma();
            selectedItemIndex = -1;
        }
        return turma;
    }
    
    public String prepareCreate(){
        turma = new Turma();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public String create() {
        try{
            ejbfacade.save(turma);
            JsfUtil.addSuccessMessage("Turma criada");
            return prepareCreate();
        }catch(Exception e){
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia");
            return null;
        }
    }
    
    public String prepareList(){
        recreateModel();
        return "List";
    }
    
    public String update(){
        try{
            ejbfacade.edit(turma);
            JsfUtil.addSuccessMessage("Turma atualizada");
            return "View";
        } catch(Exception e){
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia");
            return null;
        }
    }
    
    public Turma getTurma() {
        return this.turma;
    }
    
    public String destroyAndView(){
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0){
            return "View";
        } else {
            recreateModel();
            return "List";
        }
    }
    
    private void updateCurrentItem() {
        int count = ejbfacade.count();
        if (selectedItemIndex >= count){
            selectedItemIndex = count - 1;
            
            if(pagination.getPageFirstItem() >= count){
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0){
            turma = ejbfacade.findRange(new int[]{selectedItemIndex,
                selectedItemIndex + 1}).get(0);
        }
    }
    
    public PaginationHelper getPagination(){
        if (pagination == null){
            pagination = new PaginationHelper(10){
                
                @Override
                public int getItemsCount(){
                    return ejbfacade.count();
                }
                
                @Override
                public DataModel createPageDateModel(){
                    return new ListDataModel(ejbfacade.findRange(new 
                            int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }
    
    public DataModel getItems(){
        if(items == null){
            items = getPagination().createPageDateModel();
        }
        return items;
    }
    
    public void recreatePagination(){
        pagination = null;
    }
    
    public String next(){
        getPagination().nextPage();
        recreateModel();
        return "List";
    }
    
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }
    
}
