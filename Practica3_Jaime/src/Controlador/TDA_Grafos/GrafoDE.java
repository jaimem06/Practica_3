package Controlador.TDA_Grafos;

import Controlador.Exceptions.PosicionException;
import Controlador.Exceptions.VerticeException;
import Controlador.TDA_Lista.ListaEnlazada;
import java.util.HashMap;

/**
 * @author Jaime Mendoza
 */
public class GrafoDE<E> extends GrafoD {

    protected Class<E> clazz;
    protected E etiquetas[];
    protected HashMap<E, Integer> dicVertices;

    public GrafoDE(Integer numV, Class clazz) {
        super(numV);
        this.clazz = clazz;
        etiquetas = (E[]) java.lang.reflect.Array.newInstance(this.clazz, numV + 1);
        dicVertices = new HashMap<>(numV);
    }

    public Object[] existeAristaE(E i, E j) throws Exception {
        return this.existeArista(obtenerCodigo(i), obtenerCodigo(j));
    }

    public void insertarAristaE(E i, E j, Double peso) throws Exception {
        this.insertarArista(obtenerCodigo(i), obtenerCodigo(j), peso);
    }

    public void insertarAristaE(E i, E j) throws Exception {
        this.insertarArista(obtenerCodigo(i), obtenerCodigo(j), Double.NaN);
    }

    public Integer obtenerCodigo(E etiqueta) throws Exception {
        Integer key = dicVertices.get(etiqueta);
        if (key != null)
        {
            return key;
        } else
        {
            return -1;
        }
    }

    public E obtenerEtiqueta(Integer codigo) throws Exception {

        return etiquetas[codigo];
    }

    public ListaEnlazada<Adyacencia> adyacentesDEE(E i) throws Exception {
        return adyacente(obtenerCodigo(i));
    }

    public void etiquetarVertice(Integer codigo, E etiqueta) {
        etiquetas[codigo] = etiqueta;
        dicVertices.put(etiqueta, codigo);
    }

    public Boolean modificar(E viejo, E nuevo) throws Exception {
        Integer pos = obtenerCodigo(viejo);
        etiquetas[pos] = nuevo;
        dicVertices.remove(viejo);
        dicVertices.put(nuevo, pos);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder grafo = new StringBuilder();
        try
        {
            for (int i = 1; i <= numVertices(); i++)
            {
                grafo.append("VERTICE " + i + " --E-- " + obtenerEtiqueta(i).toString());
                try
                {
                    ListaEnlazada<Adyacencia> lista = adyacente(i);

                    for (int j = 0; j < lista.getSize(); j++)
                    {
                        Adyacencia aux = lista.obtenerDato(j);
                        if (aux.getPeso().toString().equalsIgnoreCase(String.valueOf(Double.NaN)))
                        {
                            grafo.append(" --- VERTICE DESTINO " + aux.getDestino() + " --E-- " + obtenerEtiqueta(aux.getDestino()));
                        } else
                        {
                            grafo.append(" --- VERTICE DESTINO " + aux.getDestino() + " --E-- " + obtenerEtiqueta(aux.getDestino()) + " --peso-- " + aux.getPeso());
                        }
                    }
                    grafo.append("\n");
                } catch (Exception e)
                {
                    System.out.println("Error en toString " + e);

                }
            }
        } catch (Exception e)
        {
            System.out.println("Error en toString" + e);
        }
        return grafo.toString();
    }

    public ListaEnlazada[] dijkStra(String inicio, String fin, boolean todosDatos) throws Exception {

        int codigoInicio = obtenerCodigo((E) inicio);
        int finx = obtenerCodigo((E) fin);
        boolean[] isVisited = new boolean[this.numVertices() + 1];
        double[] distance = new double[this.numVertices()];
        String[] path = new String[numVertices()];
        for (int i = 0; i < numVertices(); i++)
        {
            distance[i] = Double.POSITIVE_INFINITY;
            path[i] = "";
        }

        distance[codigoInicio - 1] = 0.0;

        int CO;
        int headIndex = codigoInicio;

        while (!isVisited[headIndex - 1])
        {

            CO = getFirstCO(headIndex);

            while (isVisited[CO - 1])
            {
                CO = getNextCO(headIndex, CO);
            }

            if (CO == numVertices() + 1)
            {
                isVisited[headIndex - 1] = true;

            } else
            {

                while (!isVisited[CO - 1] && CO < this.numVertices() + 1)
                {
                    isVisited[headIndex - 1] = true;
                    Object[] resultado = existeAristaE(obtenerEtiqueta(headIndex), obtenerEtiqueta(CO));
                    double currentDis = distance[headIndex - 1] + (double) resultado[1];

                    if (currentDis < distance[CO - 1])
                    {
                        distance[CO - 1] = currentDis;

                        path[CO - 1] = path[headIndex - 1] + " " + this.obtenerEtiqueta(headIndex);
                    }

                    CO = getNextCO(headIndex, CO);

                }
            }

            headIndex = indexGet(distance, isVisited);

        }
        for (int i = 0; i < this.numVertices(); i++)
        {
            path[i] = path[i] + " " + this.obtenerEtiqueta(i + 1);
        }

        if (todosDatos)
        {
            ListaEnlazada[] listaDAtos = new ListaEnlazada[numVertices()];
            for (int i = 0; i < this.numV; i++)
            {
                listaDAtos[i] = new ListaEnlazada<>();
            }
            for (int i = 0; i < numVertices(); i++)
            {
                listaDAtos[i].insertarCabecera(path[i]);
                listaDAtos[i].insertarCabecera(distance[i]);
                listaDAtos[i].insertarCabecera(this.obtenerEtiqueta(i + 1));
            }
            return listaDAtos;
        } else
        {
            ListaEnlazada[] listaDAtos = new ListaEnlazada[1];
            listaDAtos[0] = new ListaEnlazada<>();
            listaDAtos[0].insertarCabecera(path[finx - 1]);
            listaDAtos[0].insertarCabecera(distance[finx - 1]);
            listaDAtos[0].insertarCabecera(this.obtenerEtiqueta(finx));
            return listaDAtos;
        }

    }

    public int indexGet(double[] distance, boolean[] isVisited) {
        int j = 0;
        double mindis = Double.POSITIVE_INFINITY;
        for (int i = 0; i < distance.length; i++)
        {
            if (!isVisited[i])
            {
                if (distance[i] < mindis)
                {
                    mindis = distance[i];
                    j = i;
                }
            }
        }
        return j + 1;
    }

    public int getFirstCO(int index) throws VerticeException, Exception {
        for (int i = 1; i <= this.numVertices(); i++)
        {

            Object[] resultado = existeAristaE(obtenerEtiqueta(index), obtenerEtiqueta(i));

            if ((Double) resultado[1] > 0)
            {
                return i;
            }
        }
        return this.numVertices() + 1;
    }

    public int getNextCO(int index, int firstCO) throws VerticeException, Exception {
        for (int i = firstCO + 1; i <= this.numVertices(); i++)
        {

            Object[] resultado = existeAristaE(obtenerEtiqueta(index), obtenerEtiqueta(i));

            if ((Double) resultado[1] > 0)
            {

                return i;
            }
        }
        return this.numVertices() + 1;
    }

    public ListaEnlazada<Integer> caminosFloyd(Integer origen, Integer destino) throws VerticeException {
        ListaEnlazada<Integer> camino = new ListaEnlazada<>();
        Double[][] recorrido = new Double[numV + 1][numV + 1];
        Integer[][] traza = new Integer[numV + 1][numV + 1];
        Integer[][] traza2 = new Integer[numV + 1][numV + 1];

        for (int i = 1; i < numV; i++)
        {
            for (int j = 1; j < numV; j++)
            {
                traza2[i][j] = j;
                traza[i][j] = i;
                if (i == j)
                {
                    recorrido[i][j] = 0.0;
                    traza2[i][j] = 0;
                    traza[i][j] = 0;
                } else
                {
                    if (!(Boolean) existeArista(i, j)[0])
                    {
                        recorrido[i][j] = Double.POSITIVE_INFINITY;
                    } else
                    {
                        recorrido[i][j] = pesoArista(i, j);
                    }
                }

            }
        }

        for (int k = 1; k < numV; k++)
        {
            for (int i = 1; i < numV; i++)
            {
                for (int j = 1; j < numV; j++)
                {
                    if (recorrido[i][k] + recorrido[k][j] < recorrido[i][j])
                    {
                        recorrido[i][j] = recorrido[i][k] + recorrido[k][j];
                        traza2[i][j] = traza[k][j];
                    }
                }
            }
        }

        Integer i = origen;
        Integer j = destino;

        while (i != j)
        {
            camino.insertarCabecera(i);
            i = traza2[i][j];
        }

        camino.insertarCabecera(i);
        return camino;

    }

    public Integer[] busquedaProfundidad(Integer origen) throws PosicionException {
        Integer[] visitados = new Integer[numV];
        visitados[0] = origen;
        Integer contador = 1;
        int i = 0;
        boolean band = false;
        while (contador < numV)
        {
            i++;
            for (int j = 0; j < listaAdyacente[origen].getSize(); j++)
            {
                for (int k = 0; k < visitados.length; k++)
                {
                    if (visitados[k] == listaAdyacente[origen].obtenerDato(j).getDestino())
                    {
                        band = true;
                        break;
                    } else
                    {
                        band = false;
                    }
                }
                if (!band)
                {
                    visitados[contador] = listaAdyacente[origen].obtenerDato(j).getDestino();
                    contador++;
                }
            }

            if (origen == numV)
            {
                origen = 0;
            }
            if (contador == numV)
            {
                break;
            }
            origen++;
            if (i > numV)
            {
                break;
            }
        }
        return visitados;
    }
}
