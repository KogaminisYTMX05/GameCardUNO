package uno;

/**
 * Representa una carta del juego UNO.
 * Cada carta tiene un color, un valor y la ruta de su imagen (sprite).
 */
public class Card {

    /** Color de la carta (rojo, azul, verde, amarillo o wild). */
    private String color;

    /** Valor de la carta (0-9, reverse, skip, +2, wild, +4). */
    private String value;

    /** Ruta del archivo de imagen del sprite. */
    private String spritePath;

    /**
     * Construye una nueva carta.
     *
     * @param color      el color de la carta
     * @param value      el valor de la carta
     * @param spritePath la ruta del sprite asociado
     */
    public Card(
            String color,
            String value,
            String spritePath
    ) {

        this.color = color;

        this.value = value;

        this.spritePath = spritePath;
    }

    /**
     * Devuelve el color de la carta.
     *
     * @return el color
     */
    public String getColor() {
        return color;
    }

    /**
     * Devuelve el valor de la carta.
     *
     * @return el valor
     */
    public String getValue() {
        return value;
    }

    /**
     * Devuelve la ruta del sprite de la carta.
     *
     * @return la ruta del sprite
     */
    public String getSpritePath() {
        return spritePath;
    }

    /**
     * Representación textual de la carta (color + valor).
     *
     * @return cadena con el color y el valor
     */
    @Override
    public String toString() {

        return color + " " + value;
    }
}