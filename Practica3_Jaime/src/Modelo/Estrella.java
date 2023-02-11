package Modelo;

/**
 * @author Jaime Mendoza
 */
public class Estrella {

    private Integer id;
    private String nombres;
    private TipoEstrella tipoEstrella;
    private Ubicacion ubicacion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public TipoEstrella getTipoEstrella() {
        return tipoEstrella;
    }

    public void setTipoEstrella(TipoEstrella tipoEstrella) {
        this.tipoEstrella = tipoEstrella;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public String toString() {
        return nombres;
    }

}
