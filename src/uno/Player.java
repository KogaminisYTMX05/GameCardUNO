package uno;

import java.util.ArrayList;

/**
 * Representa un jugador de UNO (puede ser humano o CPU).
 * Cada jugador tiene un nombre, una lista de cartas en la mano y un tipo.
 */
public class Player {

    /** Nombre del jugador. */
    private String name;

    /** Indica si el jugador es controlado por la CPU. */
    private boolean cpu;

    /** Lista de cartas que tiene actualmente el jugador. */
    private ArrayList<Card> hand;

    /**
     * Construye un nuevo jugador.
     *
     * @param name nombre del jugador
     * @param cpu  {@code true} si es controlado por la CPU, {@code false} si es humano
     */
    public Player(
            String name,
            boolean cpu
    ) {

        this.name = name;

        this.cpu = cpu;

        hand = new ArrayList<>();
    }

    /**
     * Añade una carta a la mano del jugador.
     *
     * @param card la carta a añadir
     */
    public void addCard(Card card) {

        hand.add(card);
    }

    /**
     * Elimina una carta de la mano del jugador.
     *
     * @param card la carta a eliminar
     */
    public void removeCard(Card card) {

        hand.remove(card);
    }

    /**
     * Devuelve la lista de cartas en la mano del jugador.
     *
     * @return la mano del jugador
     */
    public ArrayList<Card> getHand() {

        return hand;
    }

    /**
     * Devuelve el nombre del jugador.
     *
     * @return el nombre
     */
    public String getName() {

        return name;
    }

    /**
     * Indica si el jugador es controlado por la CPU.
     *
     * @return {@code true} si es CPU, {@code false} si es humano
     */
    public boolean isCpu() {

        return cpu;
    }

    /**
     * Comprueba si el jugador se ha quedado sin cartas (ha ganado la partida).
     *
     * @return {@code true} si la mano está vacía, {@code false} en caso contrario
     */
    public boolean hasWon() {

        return hand.isEmpty();
    }
}