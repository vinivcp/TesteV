/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import modelo.Disciplina;

/**
 *
 * @author vinivcp
 */

@Named (value = "disciplinaController")
@SessionScoped
public class DisciplinaController implements Serializable{
    
    Disciplina disciplina;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    @EJB
    private facade.DisciplinaFacade ejbFacade;
    
    public DisciplinaController() {
        disciplina = new Disciplina();
    }
    
    public String prepareView() {
        disciplina = (Disciplina) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    
    public String preapareEdit() {
        disciplina = (Disciplina) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String destroy() {
        
        disciplina = (Disciplina) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    private void recreateModel() {
        items = null;
    }
    
    private void performDestroy() {
        try {
            ejbFacade.remove(disciplina);
            JsfUtil.addSuccessMessage("Disciplina deletada");
        } catch (Exception e){
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia");
        }
    }
    
    public Disciplina getSelected() {
        if (disciplina == null){
            disciplina = new Disciplina();
            selectedItemIndex = -1;
        }
        return disciplina;
    }
    
    public String prepareCreate() {
        disciplina = new Disciplina();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public String create(){
        try{
            ejbFacade.save(disciplina);
            JsfUtil.addSuccessMessage("Disciplina criada");
            return prepareCreate();
        }catch(Exception e){
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia");
            return null;
        }
    }
    
    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    public String update(){
        try{
            ejbFacade.edit(disciplina);
            JsfUtil.addSuccessMessage("Disciplina atualizada");
            return "View";
        }catch(Exception e){
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia");
            return null;}
    }
    
    public Disciplina getDisciplina(){
        return this.disciplina;
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
    
    private void updateCurrentItem(){
        
        int count = ejbFacade.count();
        if (selectedItemIndex >= count){
            selectedItemIndex = count - 1;
           if(pagination.getPageFirstItem() >= count){
               pagination.previousPage();
           } 
        }
        if (selectedItemIndex >= 0){
            disciplina = ejbFacade.findRange(new int[]{
                selectedItemIndex, selectedItemIndex +1}).get(0);
        }
    }
    
    public PaginationHelper getPagination(){
        if(pagination == null){
            pagination = new PaginationHelper(10){
                @Override
                public int getItemsCount() {
                    return ejbFacade.count();
                }
                
                @Override
                public DataModel createPageDateModel(){
                    return new ListDataModel(ejbFacade.findRange(new 
                            int[]{getPageFirstItem(), getPageFirstItem()
                                    + getPageSize()}));
                }
                
            };
        }
        return pagination;
    }
    
    public DataModel getItems(){
        if (items == null){
            items = getPagination().createPageDateModel();
        }
        
        return items;
    }
    
    private void recreatePagination(){
        pagination = null;
    }
    
    public String next(){
        getPagination().nextPage();
        recreateModel();
        return "List";
    }
    
    public String previous(){
        getPagination().previousPage();
        recreateModel();
        return "List";
    }
    
    public SelectItem[] getItemsAvaiableSelectOne(){
     
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }
    
    public Disciplina getDisciplina(java.lang.Long id){
        return ejbFacade.find(id);
    }
    
    @FacesConverter(forClass = Disciplina.class)
    public static class DisciplinaControllerConverter implements Converter {
       
        @Override
        public Object getAsObject (FacesContext facesContext, 
                UIComponent component, String value){
            
            if (value == null || value.length() == 0){
                return null;
            }
            DisciplinaController controller = (DisciplinaController)
                    facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "disciplinaController");
            return controller.getDisciplina(getKey(value));
        }
        
        java.lang.Long getKey(String value){
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }
        
        String getStringKey(java.lang.Long value){
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }
        
        @Override
        public String getAsString(FacesContext facesContext,
                UIComponent component, Object object){
            if (object == null){
                return null;
            }
            if (object instanceof Disciplina){
                Disciplina o = (Disciplina) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object" + object +
                        " is of type " + object.getClass().getName() + ";expected type" +
                        Disciplina.class.getName());
            }
            
        }
        
    }
}
