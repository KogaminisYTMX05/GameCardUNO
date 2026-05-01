package juegouno;

public class Card {
    public enum Tipo { NUMERO, ACCION, COMODIN }
    public enum Color { ROJO, AMARILLO, VERDE, AZUL }
    public enum Accion { REVERSA, SALTO, ROBA2 }
    public enum Comodin { CAMBIO_COLOR, ROBA4 }

    private final Tipo tipo;
    private final Color color;
    private final Integer numero;
    private final Accion accion;
    private final Comodin comodin;

    public Card(Color color, int numero) {
        this.tipo = Tipo.NUMERO;
        this.color = color;
        this.numero = numero;
        this.accion = null;
        this.comodin = null;
    }

    public Card(Color color, Accion accion) {
        this.tipo = Tipo.ACCION;
        this.color = color;
        this.accion = accion;
        this.numero = null;
        this.comodin = null;
    }

    public Card(Comodin comodin) {
        this.tipo = Tipo.COMODIN;
        this.comodin = comodin;
        this.color = null;
        this.numero = null;
        this.accion = null;
    }

    public Tipo getTipo() { return tipo; }
    public Color getColor() { return color; }
    public Integer getNumero() { return numero; }
    public Accion getAccion() { return accion; }
    public Comodin getComodin() { return comodin; }

    public boolean esJugable(Card cartaMesa, Color colorActual) {
        if (tipo == Tipo.COMODIN) return true;
        Color colorComparar = (cartaMesa.getTipo() == Tipo.COMODIN) ? colorActual : cartaMesa.getColor();
        if (this.color == colorComparar) return true;
        if (cartaMesa.getTipo() == Tipo.NUMERO && this.tipo == Tipo.NUMERO)
            return this.numero.equals(cartaMesa.getNumero());
        else if (cartaMesa.getTipo() == Tipo.ACCION && this.tipo == Tipo.ACCION)
            return this.accion == cartaMesa.getAccion();
        return false;
    }

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