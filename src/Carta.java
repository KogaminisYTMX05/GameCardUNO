public class Carta {
    private String color;
    private int numero;
    
    public Carta(String color, int numero) {
        this.color = color;
        this.numero = numero;
    }
    
    public String getColor() {
        return color;
    }
    
    public int getNumero() {
        return numero;
    }
    
    public boolean esJugable(Carta cartaSuperior) {
        return this.color.equals(cartaSuperior.getColor()) || 
               this.numero == cartaSuperior.getNumero();
    }
    
    @Override
    public String toString() {
        String colorTexto;
        switch(color.toLowerCase()) {
            case "rojo": colorTexto = "\u001B[31m"; break;
            case "azul": colorTexto = "\u001B[34m"; break;
            case "verde": colorTexto = "\u001B[32m"; break;
            case "amarillo": colorTexto = "\u001B[33m"; break;
            default: colorTexto = "\u001B[0m";
        }
        return colorTexto + color + " " + numero + "\u001B[0m";
    }
}