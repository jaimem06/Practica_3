package Vista.Tablas;

import Controlador.Exceptions.VerticeException;
import Controlador.TDA_Grafos.GrafoD;
import Controlador.TDA_Grafos.GrafoDE;
import javax.swing.table.AbstractTableModel;

/**
 * @author Jaime Mendoza
 */
public class ModeloTablaGrafo extends AbstractTableModel{
    
    private GrafoDE grafoDE;
    private String[] columnas;

    private String [] generarColumna() throws Exception{
        columnas = new String [grafoDE.numVertices()+1];
        columnas[0] = "/";
        for (int i = 0; i < columnas.length; i++) {
            columnas[i] = String.valueOf(grafoDE.obtenerEtiqueta(i));
        }
        return columnas;
    }

    public GrafoD getGrafoD() {
        return grafoDE;
    }

    public void setGrafoED(GrafoDE grafoD) throws Exception {
        this.grafoDE = grafoD;
        generarColumna();
    }
    
    @Override
    public int getRowCount() {
        return grafoDE.numVertices();
    }

    @Override
    public int getColumnCount() {
        return grafoDE.numVertices()+1;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int arg0, int arg1) {
        if (arg1==0) {
            return columnas[arg0+1];
        }else{
            try {
                Object [] aux = grafoDE.existeArista((arg0+1), arg1);
                if ((Boolean) aux[0]) {
                    return aux[1];
                }else{
                    return "----------";
                }
            } catch (VerticeException ex) {
                System.out.println("ERROR EN LA TABLA " + ex);
                return "";
            }
        }
    }
    
}
