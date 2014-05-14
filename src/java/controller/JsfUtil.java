/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author vinivcp
 */
public class JsfUtil {
    
    public static SelectItem[] getSelectItems(List<?> entities, 
            boolean selectOne){
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne){
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities){
            items[i++] = new SelectItem(x, x.toString());
        }
             
        return items;
    }
 
    public static void addErrorMessage (Exception ex, String defaultMsg){
        String msg = ex.getLocalizedMessage();
        if(msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }
    
    public static void addErrorMessages(List<String> messages) {
        for(String message : messages){
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);      
    }
    
    public static void addSuccessMessage(String msg){
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }
    
    
}
