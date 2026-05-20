package uno;

/**
 * Motor de reglas del juego UNO.
 * Determina si un movimiento es válido según el color o el valor de la carta.
 */
public class RuleEngine {

    /**
     * Verifica si una carta puede ser jugada sobre la carta actual de la pila.
     * Un movimiento es válido si:
     * <ul>
     *   <li>La carta jugada es comodín (wild o +4)</li>
     *   <li>Coincide el color con la carta superior</li>
     *   <li>Coincide el valor con la carta superior</li>
     * </ul>
     *
     * @param playedCard  la carta que el jugador intenta jugar
     * @param currentCard la carta que está actualmente en la cima de la pila de descartes
     * @return {@code true} si el movimiento es permitido, {@code false} en caso contrario
     */
    public boolean isValidMove(
            Card playedCard,
            Card currentCard
    ) {

        if(playedCard == null
                || currentCard == null) {

            return false;
        }

        // COMODÍN

        if(playedCard.getColor()
                .equals("wild")) {

            return true;
        }

        // MISMO COLOR

        if(playedCard.getColor()
                .equals(currentCard.getColor())) {

            return true;
        }

        // MISMO VALOR

        return playedCard.getValue()
                .equals(currentCard.getValue());
    }
}