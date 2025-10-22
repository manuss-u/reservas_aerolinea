
package reservasaerolinea;


public class Reserva {
    private String asiento;
    private String nombre;
    private boolean ocupado;

    public Reserva(String asiento) {
        this.asiento = asiento;
        this.nombre = "";
        this.ocupado = false;
    }
    
    public String getAsiento() { 
        return asiento; 
    }
    
    public String getNombre() { 
        return nombre; 
    }
    
    public boolean isOcupado() { 
        return ocupado; 
    }

    public void reservar(String nombre) {
        this.nombre = nombre;
        this.ocupado = true;
    }
}

