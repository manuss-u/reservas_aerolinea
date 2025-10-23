
package reservasaerolinea;


public class Reserva {
    private String asiento;
    private String nombre;
    private boolean ocupado;

    public Reserva(String asiento) {
        this.asiento = asiento;
        this.nombre = null;
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

    public boolean reservar(String nombre) {
        if (nombre == null){
            return false;
        }
        nombre = nombre.trim();
        if (nombre.isEmpty()){
            return false;
        }
        this.nombre = nombre;
        this.ocupado = true;
        return true;
    }
    
    public void cancelar () {
        this.nombre = null;
        this.ocupado = false;
    }
}

