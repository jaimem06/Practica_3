package Controlador.TDA_Lista;

import Controlador.Exceptions.PosicionException;

public class ListaEnlazadaServices<E> {

    private ListaEnlazada<E> lista;

    public ListaEnlazada<E> getLista() {
        return lista;
    }

    public void setLista(ListaEnlazada<E> lista) {
        this.lista = lista;
    }

    public ListaEnlazadaServices() {
        this.lista = new ListaEnlazada<>();
    }

    public Boolean insertarAlInicio(E dato) {
        lista.insertarCabecera(dato);
        return true;

    }

    public Boolean insertarAlFinal(E dato) {
        try
        {
            lista.insertar(dato, lista.getSize() - 1);
            return true;
        } catch (PosicionException e)
        {
            System.out.println(e);
        }
        return false;
    }

    public Boolean insertar(E dato, Integer pos) {
        try
        {
            lista.insertar(dato, pos);
            return true;
        } catch (PosicionException e)
        {
            System.out.println(e);
        }
        return false;
    }

    public Integer getSize() {
        return lista.getSize();
    }

    public E obtenerDato(Integer pos) {
        try
        {
            return lista.obtenerDato(pos);
        } catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    public Boolean eliminarCabecera() {
        try
        {
            lista.eliminarDato(0);
            return true;
        } catch (PosicionException e)
        {
            System.out.println(e);
        }
        return false;
    }

    public Boolean eliminarUltimo() {
        try
        {
            lista.eliminarDato(lista.getSize() - 1);
            return true;
        } catch (PosicionException e)
        {
            System.out.println(e);
        }
        return false;
    }

    public Boolean eliminarPosicion(Integer pos) {
        try
        {
            lista.eliminarDato(pos);
            return true;
        } catch (PosicionException e)
        {
            System.out.println(e);
        }
        return false;
    }

    public Boolean modificarDatoPosicion(Integer pos, E dato) {
        try
        {
            lista.modificarDato(pos, dato);
            return true;
        } catch (PosicionException e)
        {
            System.out.println(e);
        }
        return false;
    }

    public void limpiarLista() {
        lista.vaciar();
    }

}
