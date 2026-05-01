/**
 * Representa una carta del juego UNO.
 * <p>
 * Puede ser de tipo {@link Tipo#NUMERO}, {@link Tipo#ACCION} o {@link Tipo#COMODIN}.
 * Las cartas de acción y los comodines tienen efectos especiales definidos en las reglas.
 * </p>
 */
public class Card {

    /** Tipos de carta posibles. */
    public enum Tipo { NUMERO, ACCION, COMODIN }

    /** Colores permitidos para cartas numéricas y de acción. */
    public enum Color { ROJO, AMARILLO, VERDE, AZUL }

    /** Acciones de las cartas de acción. */
    public enum Accion { REVERSA, SALTO, ROBA2 }

    /** Tipos de comodines. */
    public enum Comodin { CAMBIO_COLOR, ROBA4 }

    private final Tipo tipo;
    private final Color color;
    private final Integer numero;
    private final Accion accion;
    private final Comodin comodin;

    /**
     * Construye una carta numérica.
     *
     * @param color  color de la carta.
     * @param numero número de la carta (0-9).
     */
    public Card(Color color, int numero) {
        this.tipo = Tipo.NUMERO;
        this.color = color;
        this.numero = numero;
        this.accion = null;
        this.comodin = null;
    }

    /**
     * Construye una carta de acción.
     *
     * @param color  color de la carta.
     * @param accion tipo de acción (REVERSA, SALTO, ROBA2).
     */
    public Card(Color color, Accion accion) {
        this.tipo = Tipo.ACCION;
        this.color = color;
        this.accion = accion;
        this.numero = null;
        this.comodin = null;
    }

    /**
     * Construye un comodín.
     *
     * @param comodin tipo de comodín (CAMBIO_COLOR o ROBA4).
     */
    public Card(Comodin comodin) {
        this.tipo = Tipo.COMODIN;
        this.comodin = comodin;
        this.color = null;
        this.numero = null;
        this.accion = null;
    }

    /** @return tipo de carta. */
    public Tipo getTipo() { return tipo; }

    /** @return color de la carta (puede ser {@code null} en comodines). */
    public Color getColor() { return color; }

    /** @return número de la carta (solo válido si {@code tipo == NUMERO}). */
    public Integer getNumero() { return numero; }

    /** @return acción de la carta (solo válido si {@code tipo == ACCION}). */
    public Accion getAccion() { return accion; }

    /** @return tipo de comodín (solo válido si {@code tipo == COMODIN}). */
    public Comodin getComodin() { return comodin; }

    /**
     * Determina si esta carta se puede jugar dada la carta de la mesa y el color actual.
     * <p>
     * Reglas:
     * <ul>
     *   <li>Los comodines siempre son jugables.</li>
     *   <li>Coincidencia de color.</li>
     *   <li>Coincidencia de número (ambas numéricas).</li>
     *   <li>Coincidencia de acción (ambas de acción).</li>
     * </ul>
     *
     * @param cartaMesa   carta que está en la cima de la pila de descarte.
     * @param colorActual color efectivo cuando la última carta es un comodín.
     * @return {@code true} si la carta se puede jugar.
     */
    public boolean esJugable(Card cartaMesa, Color colorActual) {
        if (tipo == Tipo.COMODIN) return true;
        Color colorComparar = (cartaMesa.getTipo() == Tipo.COMODIN) ? colorActual : cartaMesa.getColor();
        if (this.color == colorComparar) return true;
        if (cartaMesa.getTipo() == Tipo.NUMERO && this.tipo == Tipo.NUMERO) {
            return this.numero.equals(cartaMesa.getNumero());
        } else if (cartaMesa.getTipo() == Tipo.ACCION && this.tipo == Tipo.ACCION) {
            return this.accion == cartaMesa.getAccion();
        }
        return false;
    }

    /**
     * Representación textual de la carta.
     *
     * @return cadena con el color y número, color y acción, o nombre del comodín.
     */
    @Override
    public String toString() {
        switch (tipo) {
            case NUMERO:  return color + " " + numero;
            case ACCION:  return color + " " + accion;
            case COMODIN: return comodin.toString();
            default:      return "?";
        }
    }
}