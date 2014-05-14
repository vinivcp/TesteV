/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.io.Serializable;
import java.util.List;
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
import modelo.Docente;
import util.DocenteDataModel;

/**
 *
 * @author vinivcp
 */

@Named(value = "docenteController")
@SessionScoped
public class DocenteController implements Serializable {
    
    Docente docente;
    
    private DataModel items = null; 
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private DocenteDataModel docenteDataModel;
    
    @EJB
    private facade.DocenteFacade ejbFacade;
    
    public DocenteController(){
        docente = new Docente();
    }
    
     public DocenteDataModel getDocenteDataModel() {
        if (docenteDataModel == null) {
            List<Docente> docentes = ejbFacade.findAll();
            docenteDataModel = new DocenteDataModel (docentes);
        }
        return docenteDataModel;
    }

    
    public String prepareView() {
        docente = (Docente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        
        return "View";
    }
    
    public String prepareEdit() {
        docente = (Docente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String destroy() {
        
        docente = (Docente) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    private void recreateModel(){
        items = null;
    }
    
    private void performDestroy() {
        try{
            ejbFacade.remove(docente);
            JsfUtil.addSuccessMessage("Docente deletado");
        } catch (Exception e){
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia");
        }   
    }
    
    public Docente getSelected() {
        if (docente == null) {
            docente = new Docente();
            selectedItemIndex = -1;
        }
        return docente;
    }
    
    public String prepareCreate(){
       docente = new Docente();
       selectedItemIndex = -1;
       return "Create";
    }
    
    public String create() {
        
        try{
            docente = new Docente();
            docente.setNome("Charles");
            ejbFacade.save(docente);
            JsfUtil.addSuccessMessage("Docente Criado");
            return prepareCreate();
        } catch (Exception e){
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia");
            return null;
        }
    }
    
    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    public String update() {
        
        try {
            ejbFacade.edit(docente);
            JsfUtil.addSuccessMessage("Docente Atualizado");
            return "View";
        } catch (Exception e){
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistencia");
            return null;
        }
    }
   
    
    public Docente getDocente(){
        return this.docente;
    }
    
    public String destroyAndView() {
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
        int count = ejbFacade.count();
        if (selectedItemIndex >= count){
            selectedItemIndex = count - 1;
            if(pagination.getPageFirstItem() >= count){
                pagination.previousPage();
            }
            
        }
        if (selectedItemIndex >= 0){
            docente = ejbFacade.findRange(new int[]{selectedItemIndex,
                selectedItemIndex +1 }).get(0);
        }        
    }
    
    public PaginationHelper getPagination() {
        if  (pagination == null){
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount(){
                    return ejbFacade.count();
                }
                
                @Override
                public DataModel createPageDateModel() {
                    return new ListDataModel(ejbFacade.findRange(new int[]{getPageFirstItem(), 
                        getPageFirstItem() + getPageSize()}));
                    
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
    
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }
    
    public String previous(){
        getPagination().previousPage();
        recreateModel();
        return"List";
    }
    
    public SelectItem[] getItemsAvaiableSelectOne(){
      return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }
    
    public Docente getDocente(java.lang.Long id){
        return ejbFacade.find(id);
    }
    
    @FacesConverter(forClass = Docente.class)
    public static class DocenteControllerConverter implements Converter{
       
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component,
                String value){
            if (value == null || value.length() == 0 ){
                return null;
            }
            DocenteController controller = (DocenteController)
                    facesContext.getApplication().getELResolver().
                            getValue(facesContext.getELContext(), null, "docenteController");
            return controller.getDocente(getKey(value));
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
            if(object == null){
                return null;
            }
            
            if (object instanceof Docente){
                Docente o = (Docente) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object" + object + 
                        "is of type " + object.getClass().getName() + ";"
                + " expected type: " + Docente.class.getName());
                 }
            }
        }
} 
    

