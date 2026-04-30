import java.util.List;

/**
 * Gestiona el orden de turnos, la dirección de juego y los saltos pendientes.
 */
public class TurnManager {
    private final List<Player> jugadores;
    private int indiceJugadorActual;
    private int direccion;      // 1 = horario, -1 = antihorario
    private int saltosPendientes;

    /**
     * Crea el gestor de turnos.
     *
     * @param jugadores lista de jugadores en el orden inicial.
     */
    public TurnManager(List<Player> jugadores) {
        this.jugadores = jugadores;
        this.indiceJugadorActual = 0;
        this.direccion = 1;
        this.saltosPendientes = 0;
    }

    /**
     * @return jugador que debe jugar en este momento.
     */
    public Player getJugadorActual() {
        return jugadores.get(indiceJugadorActual);
    }

    /**
     * Avanza al siguiente turno considerando saltos y dirección.
     * Una vez avanzado, los saltos pendientes se reinician.
     */
    public void siguienteTurno() {
        int pasos = direccion * (1 + saltosPendientes);
        indiceJugadorActual = (indiceJugadorActual + pasos) % jugadores.size();
        if (indiceJugadorActual < 0) indiceJugadorActual += jugadores.size();
        saltosPendientes = 0;
    }

    /**
     * @return el jugador que recibiría el turno sin aplicar saltos.
     */
    public Player getSiguienteJugador() {
        int siguienteIndice = (indiceJugadorActual + direccion) % jugadores.size();
        if (siguienteIndice < 0) siguienteIndice += jugadores.size();
        return jugadores.get(siguienteIndice);
    }

    /** Invierte la dirección de juego (horario ↔ antihorario). */
    public void invertirDireccion() {
        direccion *= -1;
    }

    /** Acumula un salto de turno pendiente. */
    public void agregarSalto() {
        saltosPendientes++;
    }

    /**
     * Acumula varios saltos de turno pendientes.
     *
     * @param cantidad número de saltos a agregar.
     */
    public void agregarSaltos(int cantidad) {
        saltosPendientes += cantidad;
    }

    /** @return dirección actual (1 horario, -1 antihorario). */
    public int getDireccion() { return direccion; }
}