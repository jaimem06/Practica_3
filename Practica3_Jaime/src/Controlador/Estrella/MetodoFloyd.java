package Controlador.Estrella;

/**
 * @author Jaime Mendoza
 */
public class MetodoFloyd {

    Estrella_Controller ec;

    public MetodoFloyd(Estrella_Controller ec) {
        this.ec = ec;
    }

    public String algoritmoFloyd(Double[][] MatrizP) throws Exception {
        int vertices = MatrizP.length;
        Double matrizAdyacencia[][] = MatrizP;
        String caminos[][] = new String[vertices][vertices];
        String caminosAuxiliares[][] = new String[vertices][vertices];
        String caminoRecorrido = "", cadena = "", camino = "";
        int i, j, k;
        double temporal1, temporal2, temporal3, temporal4, minimo;
        for (i = 0; i < vertices; i++)
        {
            for (j = 0; j < vertices; j++)
            {
                caminos[i][j] = "";
                caminosAuxiliares[i][j] = "";
            }
        }
        for (k = 0; k < vertices; k++)
        {
            for (i = 0; i < vertices; i++)
            {
                for (j = 0; j < vertices; j++)
                {
                    temporal1 = matrizAdyacencia[i][j];
                    temporal2 = matrizAdyacencia[i][k];
                    temporal3 = matrizAdyacencia[k][j];
                    temporal4 = temporal2 + temporal3;
                    minimo = Math.min(temporal1, temporal4);
                    if (temporal1 != temporal4)
                    {
                        if (minimo == temporal4)
                        {
                            caminoRecorrido = "";
                            caminosAuxiliares[i][j] = k + "";
                            caminos[i][j] = caminosR(i, k, caminosAuxiliares, caminoRecorrido) + (k + 1);
                        }
                    }
                    matrizAdyacencia[i][j] = minimo;
                }
            }
        }
        for (i = 0; i < vertices; i++)
        {
            for (j = 0; j < vertices; j++)
            {
                cadena = cadena + "[" + matrizAdyacencia[i][j] + "]";
            }
            cadena = cadena + "\n";
        }

        for (i = 0; i < vertices; i++)
        {
            for (j = 0; j < vertices; j++)
            {
                if (matrizAdyacencia[i][j] != -1)
                { //-1 representa el infinito
                    if (i != j)
                    {
                        if (caminos[i][j].equals(""))
                        {
                            camino += ec.getGrafoEND().obtenerEtiqueta((i + 1)).getNombres()+ " --> " + ec.getGrafoEND().obtenerEtiqueta((j + 1)).getNombres()+ "\n";
                        } else
                        {
                            camino += ec.getGrafoEND().obtenerEtiqueta((i + 1)).getNombres()+ " --> " + ec.getGrafoEND().obtenerEtiqueta(Integer.parseInt(caminos[i][j])).getNombres()+ " --> " + ec.getGrafoEND().obtenerEtiqueta((j + 1)).getNombres()+ "\n";
                        }
                    }
                }
            }
        }
        return cadena + "\n" + camino;
    }

    public String caminosR(int i, int k, String[][] caminosAuxiliares, String caminoRecorrido) {
        if (caminosAuxiliares[i][k] == "")
        {
            return "";
        } else
        {
            caminoRecorrido += caminosR(i, Integer.parseInt(caminosAuxiliares[i][k].toString()), caminosAuxiliares, caminoRecorrido) + (Integer.parseInt(caminosAuxiliares[i][k].toString())) + ", ";
            return caminoRecorrido;
        }
    }

}
