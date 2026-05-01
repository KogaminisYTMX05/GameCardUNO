package juegouno;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingresa tu nombre: ");
        String nombre = sc.nextLine().trim();
        if (nombre.isEmpty()) nombre = "P1";

        Game juego = new Game(nombre);
        juego.iniciar();
        sc.close();
    }
}