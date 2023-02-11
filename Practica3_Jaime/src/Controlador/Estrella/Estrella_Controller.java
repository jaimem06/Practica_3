package Controlador.Estrella;

import Controlador.TDA_Grafos.GrafoEND;
import Controlador.TDA_Lista.ListaEnlazada;
import Modelo.Estrella;
import Modelo.TipoEstrella;
import Modelo.Ubicacion;

/**
 * @author Jaime Mendoza
 */
public class Estrella_Controller {

    private GrafoEND<Estrella> grafoEND;
    private Estrella estrella;
    private ListaEnlazada<Estrella> estrellas = new ListaEnlazada<>();

    public Estrella_Controller(Integer nro_vertices) {
        grafoEND = new GrafoEND<>(nro_vertices, Estrella.class);
        for (int i = 1; i <= nro_vertices; i++)
        {
            Estrella p = new Estrella();
            p.setId(i);
            p.setNombres("Persona " + i);
            p.setTipoEstrella(TipoEstrella.NORTE);
            Ubicacion u = new Ubicacion();
            u.setId(i);
            u.setLatitud(0.0);
            u.setLongitud(0.0);
            p.setUbicacion(u);
            grafoEND.etiquetarVertice(i, p);
        }
    }

    public ListaEnlazada<Estrella> getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(ListaEnlazada<Estrella> estrellas) {
        this.estrellas = estrellas;
    }

    public Estrella_Controller() {
    }

    public GrafoEND<Estrella> getGrafoEND() {
        if (grafoEND == null)
        {
            grafoEND = new GrafoEND<>(0, Estrella.class);
        }
        return grafoEND;
    }

    public void setGrafoEND(GrafoEND<Estrella> grafoEND) {
        this.grafoEND = grafoEND;
    }

    public Estrella getEstrella() {
        if (estrella == null)
        {
            estrella = new Estrella();
        }
        return estrella;
    }

    public void setEstrella(Estrella estrella) {
        this.estrella = estrella;
    }

    public Double calcularDistancia(Estrella po, Estrella pd) {
        Double dis = 0.0;
        Double x = po.getUbicacion().getLongitud() - pd.getUbicacion().getLongitud();
        Double y = po.getUbicacion().getLatitud() - pd.getUbicacion().getLatitud();
        dis = Math.sqrt((x * x) + (y * y));
        return dis;
    }

}
