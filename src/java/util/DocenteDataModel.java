/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;
import javax.faces.model.ListDataModel;
import modelo.Docente;

import org.primefaces.model.SelectableDataModel;

public class DocenteDataModel extends ListDataModel implements SelectableDataModel<Docente> {

    public DocenteDataModel() {
    }

    public DocenteDataModel(List<Docente> data) {
        super(data);
    }

    @Override
    public Docente getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  

        List<Docente> docentes = (List<Docente>) getWrappedData();

        for (Docente docente : docentes) {
            if (docente.getNome().equals(rowKey)) {
                return docente;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Docente docente) {
        return docente.getNome();
    }

}
