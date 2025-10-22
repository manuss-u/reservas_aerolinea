
package reservasaerolinea;


public class GestorReservas {
    private Reserva[] reservas;

    public GestorReservas(int cantidad) {
        reservas = new Reserva[cantidad];
        for (int i = 0; i < cantidad; i++) {
            reservas[i] = new Reserva("A" + (i + 1));
        }
    }

    public Reserva[] getReservas() {
        return reservas;
    }

    public Reserva getReserva(int index) {
        return reservas[index];
    }

    public boolean reservar(int index, String nombre) {
        if (!reservas[index].isOcupado()) {
            reservas[index].reservar(nombre);
            return true;
        }
        return false;
    }
}
