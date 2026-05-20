package uno;

import java.util.ArrayList;

/**
 * Gestor de turnos del juego UNO.
 * Mantiene la lista de jugadores (aunque el control de turnos se realiza en {@link Game} y {@link GUI}).
 */
public class TurnManager {

    /** Lista de jugadores en el orden de la mesa. */
    private ArrayList<Player> players;

    /**
     * Crea un gestor de turnos con la lista de jugadores.
     *
     * @param players los jugadores (normalmente en orden: humano, CPU izquierda, CPU arriba, CPU derecha)
     */
    public TurnManager(
            ArrayList<Player> players
    ) {

        this.players = players;
    }

    /**
     * Devuelve la lista de jugadores.
     *
     * @return los jugadores de la partida
     */
    public ArrayList<Player> getPlayers() {

        return players;
    }
}