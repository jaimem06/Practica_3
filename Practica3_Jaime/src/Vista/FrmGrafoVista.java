package Vista;

import Controlador.TDA_Grafos.Adyacencia;
import Controlador.TDA_Grafos.GrafoDE;
import Controlador.TDA_Lista.ListaEnlazada;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;

/**
 * @author Jaime Mendoza
 */
public class FrmGrafoVista extends JDialog {

    private GrafoDE grafo;

    public FrmGrafoVista() {
        this.setModal(true);
    }

    public FrmGrafoVista(GrafoDE grafo) {
        this.setModal(true);
        this.grafo = grafo;
        iniciar();
    }

    private void iniciar() {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        mxStylesheet stylesheet = graph.getStylesheet();

        Map<String, Object> style = new HashMap<>();
        style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RHOMBUS);
        style.put(mxConstants.STYLE_OPACITY, 100);
        style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style.put(mxConstants.STYLE_FILLCOLOR, "#1FFFF7");
        stylesheet.putCellStyle("STAR", style);
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setSize(new Dimension(400, 400));
        graph.getModel().beginUpdate();
        try
        {
            ListaEnlazada<Object[]> listaPintadosObject = new ListaEnlazada<>();
            for (int i = 1; i <= this.grafo.numVertices(); i++)
            {
                Object a = graph.insertVertex(parent, (String) grafo.obtenerEtiqueta(i), (String) grafo.obtenerEtiqueta(i), 100, 100, 55, 70, "STAR");
                Object[] aux =
                {
                    i, a
                };
                listaPintadosObject.insertarCabecera(aux);
            }
            for (int i = 1; i <= this.grafo.numVertices(); i++)
            {
                ListaEnlazada<Adyacencia> lista = grafo.adyacente(i);
                Object a = buscar(i, listaPintadosObject);
                for (int j = 0; j < lista.getSize(); j++)
                {
                    Adyacencia aux = lista.obtenerDato(j);
                    Object b = buscar(aux.getDestino(), listaPintadosObject);
                    graph.insertEdge(parent, null, aux.getPeso().toString(), a, b);
                }
            }

        } catch (Exception e)
        {
        } finally
        {
            graph.getModel().endUpdate();
        }
        animacionGrafos(graph, graphComponent);
        new mxHierarchicalLayout(graph).execute(graph.getDefaultParent());
        getContentPane().add(graphComponent);
    }

    private void animacionGrafos(mxGraph graph, mxGraphComponent graphComponent) {
        mxIGraphLayout layout = new mxFastOrganicLayout(graph);
        graph.getModel().beginUpdate();
        try
        {
            layout.execute(graph.getDefaultParent());
        } finally
        {
            mxMorphing morphing = new mxMorphing(graphComponent, 20, 1.5, 20);
            morphing.addListener(mxEvent.DONE, new mxEventSource.mxIEventListener() {
                @Override
                public void invoke(Object arg0, mxEventObject arg1) {
                    graph.getModel().endUpdate();
                }
            });
            morphing.startAnimation();
        }
    }

    private Object buscar(Integer vertice, ListaEnlazada<Object[]> lista) {
        try
        {
            Object resp = null;
            for (int i = 0; i < lista.getSize(); i++)
            {
                Object[] aux = lista.obtenerDato(i);
                if (((Integer) aux[0]).intValue() == vertice.intValue())
                {
                    resp = aux[1];
                    break;
                }
            }
            return resp;
        } catch (Exception e)
        {
            System.out.println("Error no se encontro " + e);
            return null;
        }
    }
}
