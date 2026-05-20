package uno;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Representa el mazo de cartas del juego UNO.
 * Genera todas las cartas estándar, las mezcla y permite robar cartas.
 */
public class Deck {

    /** Lista de cartas que componen el mazo. */
    private ArrayList<Card> cards;

    /**
     * Construye un mazo nuevo con todas las cartas de UNO y las mezcla.
     * Incluye números, reversa, salto, +2, comodines y +4.
     */
    public Deck() {

        cards = new ArrayList<>();

        String[] colors = {
                "rojo",
                "azul",
                "verde",
                "amarillo"
        };

        // =====================================
        // CARTAS NUMÉRICAS
        // =====================================

        for(String color : colors) {

            // 1 CERO

            cards.add(
                    new Card(
                            color,
                            "0",
                            "/sprites/"
                                    + color
                                    + "_0.png"
                    )
            );

            // 2 DE CADA 1-9

            for(int i = 1; i <= 9; i++) {

                cards.add(
                        new Card(
                                color,
                                String.valueOf(i),
                                "/sprites/"
                                        + color
                                        + "_"
                                        + i
                                        + ".png"
                        )
                );

                cards.add(
                        new Card(
                                color,
                                String.valueOf(i),
                                "/sprites/"
                                        + color
                                        + "_"
                                        + i
                                        + ".png"
                        )
                );
            }

            // REVERSE

            for(int i = 0; i < 2; i++) {

                cards.add(
                        new Card(
                                color,
                                "reverse",
                                "/sprites/"
                                        + color
                                        + "_reverse.png"
                        )
                );
            }

            // SKIP

            for(int i = 0; i < 2; i++) {

                cards.add(
                        new Card(
                                color,
                                "skip",
                                "/sprites/"
                                        + color
                                        + "_skip.png"
                        )
                );
            }

            // DRAW 2

            for(int i = 0; i < 2; i++) {

                cards.add(
                        new Card(
                                color,
                                "+2",
                                "/sprites/"
                                        + color
                                        + "_draw2.png"
                        )
                );
            }
        }

        // WILD

        for(int i = 0; i < 4; i++) {

            cards.add(
                    new Card(
                            "wild",
                            "wild",
                            "/sprites/wild.png"
                    )
            );
        }

        // DRAW 4

        for(int i = 0; i < 4; i++) {

            cards.add(
                    new Card(
                            "wild",
                            "+4",
                            "/sprites/wild_draw4.png"
                    )
            );
        }

        Collections.shuffle(cards);
    }

    /**
     * Roba la primera carta del mazo (la elimina y la devuelve).
     *
     * @return la carta robada, o {@code null} si el mazo está vacío
     */
    public Card drawCard() {

        if(cards.isEmpty()) {

            return null;
        }

        return cards.remove(0);
    }

    /**
     * Devuelve la cantidad de cartas restantes en el mazo.
     *
     * @return número de cartas que aún no han sido robadas
     */
    public int remainingCards() {

        return cards.size();
    }
}