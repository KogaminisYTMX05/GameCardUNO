public class Carta {
    public enum Color {
        ROJO, AMARILLO, VERDE, AZUL
    }

    private Color color;
    private int numero;

    public Carta(Color color, int numero) {
        this.color = color;
        this.numero = numero;
    }

    public Color getColor() {
        return color;
    }

    public int getNumero() {
        return numero;
    }

    public boolean esJugable(Carta otra) {
        return this.color == otra.color || this.numero == otra.numero;
    }

    @Override
    public String toString() {
        return color.toString() + " " + numero;
    }
}