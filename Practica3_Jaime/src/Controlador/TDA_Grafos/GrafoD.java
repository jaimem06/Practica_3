package Controlador.TDA_Grafos;

import Controlador.Exceptions.PosicionException;
import Controlador.Exceptions.VerticeException;
import Controlador.TDA_Lista.ListaEnlazada;

/**
 * @author Jaime Mendoza
 */
public class GrafoD extends Grafo {

    protected Integer numV;
    protected Integer numA;
    protected ListaEnlazada<Adyacencia> listaAdyacente[];

    public GrafoD(Integer numV) {
        this.numV = numV;
        this.numA = 0;
        listaAdyacente = new ListaEnlazada[numV + 1];
        for (int i = 0; i <= this.numV; i++)
        {
            listaAdyacente[i] = new ListaEnlazada<>();
        }

    }

    public void comprobasF() {
        for (int i = 1; i <= numV; i++)
        {
            System.out.println(listaAdyacente[i].getSize());
        }
        System.out.println("---");
    }

    @Override
    public Integer numVertices() {
        return this.numV;
    }

    @Override
    public Integer numAristas() {
        return this.numA;
    }

    @Override
    public Object[] existeArista(Integer i, Integer f) throws VerticeException {
        Object[] resultado =
        {
            Boolean.FALSE, Double.NaN
        };
        if (i > 0 && f > 0 && i <= numV && f <= numV)
        {
            ListaEnlazada<Adyacencia> lista = listaAdyacente[i];
            for (int j = 0; j < lista.getSize(); j++)
            {
                try
                {
                    Adyacencia aux = lista.obtenerDato(j);
                    if (aux.getDestino().intValue() == f.intValue())
                    {
                        resultado[0] = Boolean.TRUE;
                        resultado[1] = aux.getPeso();
                        break;
                    }
                } catch (PosicionException ex)
                {

                }
            }
        } else
        {

        }
        return resultado;
    }

    @Override
    public Double pesoArista(Integer i, Integer f) throws VerticeException {
        Double peso = Double.NaN;
        Object[] existe = existeArista(i, f);
        if (((Boolean) existe[0]))
        {
            peso = (Double) existe[1];
        }
        return peso;
    }

    @Override
    public void insertarArista(Integer i, Integer j) throws VerticeException {
        insertarArista(i, j, Double.NaN);
    }

    @Override
    public void insertarArista(Integer i, Integer j, Double peso) throws VerticeException {
        if (i > 0 && j > 0 && i <= numV && j <= numV)
        {
            Object[] existe = existeArista(i, j);
            if (!((Boolean) existe[0]))
            {
                numA++;
                listaAdyacente[i].insertarCabecera(new Adyacencia(j, peso));
            }
        } else
        {
            throw new VerticeException("Error en insertar arista: Algun vertice ingresado no existe");
        }

    }

    @Override
    public ListaEnlazada<Adyacencia> adyacente(Integer i) throws VerticeException {
        return listaAdyacente[i];
    }

    private Integer[] visitados;
    private Integer ordenVisita;

    public Integer[] toArrayDFS() throws Exception {
        Integer res[] = new Integer[numVertices() + 1];
        visitados = new Integer[numVertices() + 1];
        ordenVisita = 0;
        for (int i = 0; i <= numVertices(); i++)
        {
            visitados[i] = 0;
        }

        for (int i = 1; i <= numVertices(); i++)
        {
            if (visitados[i] == 0)
            {
                toArrayDFS(i, res);
            }
        }
        Integer aux[] = res;
        for (int j = 0; j < res.length; j++)
        {
            for (int k = 0; k < aux.length; k++)
            {
                if (j != k)
                {
                    if (res[j] == aux[k])
                    {
                        for (int i = k; i < aux.length - 1; i++)
                        {
                            res[i] = aux[i + 1];

                        }
                    }
                }
            }
        }

        return res;
    }

    public void toArrayDFS(Integer origen, Integer res[]) throws Exception {
        res[ordenVisita] = origen;
        visitados[origen] = ordenVisita++;
        ListaEnlazada<Adyacencia> lista = listaAdyacente[origen];
        for (int i = 0; i < lista.getSize(); i++)
        {
            Adyacencia a;
            if (lista.obtenerDatoConNull(i) == null)
            {
                break;
            } else
            {
                a = lista.obtenerDato(i);
            }
            lista.setCabecera(lista.getCabecera().getSiguiente());
            if (visitados[a.getDestino()] == 0)
            {
                toArrayDFS(a.getDestino(), res);
            }
        }
    }

    public String toStringDFS() throws Exception {
        return arrayToString(toArrayDFS());
    }

    public String arrayToString(Integer v[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length; i++)
        {
            sb.append(v[i] + "\n");
        }
        return sb.toString();
    }

    public Integer[] BPA(Integer inicio) throws PosicionException {
        Integer[] recorrido = new Integer[numV];
        recorrido[0] = inicio;
        Integer cont = 1;
        int i = 0;
        boolean esta = false;
        while (cont < numV)
        {
            i++;
            for (int j = 0; j < listaAdyacente[inicio].getSize(); j++)
            {
                for (int k = 0; k < recorrido.length; k++)
                {
                    if (recorrido[k] == listaAdyacente[inicio].obtenerDatoConNull(j).getDestino())
                    {
                        esta = true;
                        break;
                    } else
                    {
                        esta = false;
                    }
                }
                if (!esta)
                {
                    recorrido[cont] = listaAdyacente[inicio].obtenerDatoConNull(j).getDestino();
                    cont++;
                }
            }
            if (inicio == numV)
            {
                inicio = 0;
            }
            if (cont == numV)
            {
                break;
            }
            inicio++;
            if (i > numV)
            {
                break;
            }
        }
        return recorrido;
    }

}
