package Controlador.TDA_Grafos;

import Controlador.Exceptions.GrafoConexionException;
import Controlador.Exceptions.PosicionException;
import Controlador.Exceptions.VerticeException;
import Controlador.TDA_Lista.ListaEnlazada;
import Controlador.Utilidades.TipoOrdenacion;

/**
 * @author Jaime Mendoza
 */
public abstract class Grafo {

    public abstract Integer numVertices();

    public abstract Integer numAristas();

    public abstract Object[] existeArista(Integer i, Integer f) throws VerticeException;

    public abstract Double pesoArista(Integer i, Integer f) throws VerticeException;

    public abstract void insertarArista(Integer i, Integer j) throws VerticeException;

    public abstract void insertarArista(Integer i, Integer j, Double peso) throws VerticeException;

    public abstract ListaEnlazada<Adyacencia> adyacente(Integer i) throws VerticeException;

    @Override
    public String toString() {
        StringBuilder grafo = new StringBuilder();
        for (int i = 1; i <= numVertices(); i++)
        {
            try
            {
                grafo.append("VERTICE " + i);
                ListaEnlazada<Adyacencia> lista = adyacente(i);
                for (int j = 0; j < lista.getSize(); j++)
                {
                    try
                    {
                        Adyacencia aux = lista.obtenerDato(j);
                        if (aux.getPeso().toString().equalsIgnoreCase(String.valueOf(Double.NaN)))
                        {
                            grafo.append("------- VERTICE DESTINO " + aux.getDestino());
                        } else
                        {
                            grafo.append("------- VERTICE DESTINO " + aux.getDestino() + "------------ peso---" + aux.getPeso());
                        }

                    } catch (PosicionException ex)
                    {
                    }
                }
                grafo.append("\n");
            } catch (VerticeException ex)
            {
                System.out.println("ERROR: " + ex);
            }
        }
        return grafo.toString();
    }

    public Boolean estaConectado() {
        Boolean bad = true;
        for (int i = 1; i <= numVertices(); i++)
        {
            try
            {
                ListaEnlazada<Adyacencia> lista = adyacente(i);
                for (int j = 0; j < lista.getSize(); j++)
                {
                    try
                    {
                        Adyacencia aux = lista.obtenerDato(j);
                        if (lista.getSize() == 0)
                        {
                            bad = false;
                            break;
                        }
                    } catch (PosicionException ex)
                    {
                        System.out.println("ERROR: " + ex);
                    }
                }
            } catch (VerticeException ex)
            {
                System.out.println("ERROR: " + ex);
            }
        }
        return bad;
    }

    private Boolean estaPintado(ListaEnlazada<Integer> lista, Integer vertice) throws PosicionException {
        Boolean band = false;

        for (int i = 0; i < lista.getSize(); i++)
        {
            if (lista.obtenerDato(i).intValue() == vertice.intValue())
            {
                band = true;
                break;
            }
        }

        return band;
    }

    public ListaEnlazada caminoMinimo(Integer verticeInicial, Integer verticeFinal) throws Exception {
        ListaEnlazada<Integer> camino = new ListaEnlazada();
        if (estaConectado())
        {
            ListaEnlazada pesos = new ListaEnlazada();
            Boolean finalizar = false;
            Integer inicial = verticeInicial;
            camino.insertarCabecera(inicial);
            while (!finalizar)
            {
                ListaEnlazada<Adyacencia> adycencias = adyacente(inicial);
                Integer T = -1;
                Double peso = 100000000.0;
                for (int i = 0; i < adycencias.getSize(); i++)
                {
                    Adyacencia ad = adycencias.obtenerDato(i);
                    if (!estaPintado(camino, ad.getDestino()))
                    {
                        Double pesoArista = ad.getPeso();
                        if (verticeFinal.intValue() == ad.getDestino())
                        {
                            T = ad.getDestino();
                            peso = ad.getPeso();
                            break;
                        } else if (pesoArista < peso)
                        {
                            T = ad.getDestino();
                            peso = pesoArista;
                        }
                    }
                }
                if (T > -1)
                {
                    pesos.insertarCabecera(peso);
                    camino.insertarCabecera(T);
                    inicial = T;
                } else
                {
                    throw new GrafoConexionException("NO SE ENCUENTRA EL CAMINO");
                }

                if (verticeFinal.intValue() == inicial.intValue())
                {
                    finalizar = true;
                }
            }
        } else
        {
            throw new GrafoConexionException("EL GRAFO NO ESTA CONECTADO");
        }
        return camino;
    }

    public Double[][] Matrizpesos() throws PosicionException, Exception {

        System.out.println("Matriz Pesos");
        Integer n = numVertices();
        Double[][] matrizPesos = new Double[n][n];
        for (int i = 0; i < n; i++)
        {
            ListaEnlazada<Adyacencia> adycencias = new ListaEnlazada<>();
            adycencias = obtenerAdyacencias(adycencias, i + 1);
            for (int j = 0; j < n; j++)
            {
                matrizPesos[i][j] = adycencias.obtenerDato(j).getPeso();
            }
        }

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                System.out.print("[" + matrizPesos[i][j] + "]" + "\t");
            }
            System.out.println();
        }
        return matrizPesos;
    }

    private ListaEnlazada<Adyacencia> obtenerAdyacencias(ListaEnlazada<Adyacencia> adycencias, Integer inicial) throws Exception {
        ListaEnlazada<Adyacencia> aux = adyacente(inicial);

        for (int i = 0; i < aux.getSize(); i++)
        {
            adycencias.insertarCabecera(aux.obtenerDato(i));
        }

        if (adycencias.getSize() < numVertices())
        {
            for (int i = 0; i < numVertices(); i++)
            {
                if (comprobar(aux, i + 1) != true)
                {
                    adycencias.insertarCabecera(new Adyacencia(i + 1, Double.POSITIVE_INFINITY));
                }
            }
        }

        adycencias.ordenar_seleccion("destino", TipoOrdenacion.ASCENDENTE);

        return adycencias;
    }

    private Boolean comprobar(ListaEnlazada<Adyacencia> list, Integer destino) throws VerticeException, PosicionException {
        Boolean estaDestino = false;
        for (int i = 0; i < list.getSize(); i++)
        {
            if (list.obtenerDato(i).getDestino() == destino)
            {
                estaDestino = true;
            }
        }
        return estaDestino;
    }

}
