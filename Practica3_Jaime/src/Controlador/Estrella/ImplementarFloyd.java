package Controlador.Estrella;

/**
 * @author Jaime Mendoza
 */
public class ImplementarFloyd {

    Estrella_Controller ec;

    public ImplementarFloyd(Estrella_Controller ec) {
        this.ec = ec;
    }

    public String implementar() throws Exception {
        MetodoFloyd cm = new MetodoFloyd(ec);
        return cm.algoritmoFloyd(ec.getGrafoEND().Matrizpesos());
    }

}
