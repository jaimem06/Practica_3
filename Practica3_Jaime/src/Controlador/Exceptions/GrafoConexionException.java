package Controlador.Exceptions;

public class GrafoConexionException extends Exception{

    /**
     * Creates a new instance of <code>GrafoConexionException</code> without
     * detail message.
     */
    public GrafoConexionException() {
    }

    /**
     * Constructs an instance of <code>GrafoConexionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GrafoConexionException(String msg) {
        super(msg);
    }
}
