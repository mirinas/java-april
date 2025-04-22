package org.example;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Banko sąskaitos programa, galima prisijungti, prisiregistruoti, pervesti pinigus
 */
public class App {
    public static void main(String[] args) throws SQLException {

        DBConnection db;
        try {
            db = new DBConnection();

        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            return;
        }

        var s = new Scanner(System.in);

        // 1. paprasyti ivesti komanda (nuimti, prideti, quit)
        // 2. perskaityti naudotojo pasirinkima
        // 3. pagal pasirinkima, ivykdyti veiksma

        // while ciklas - ivykdzius veiksma, skaitom nauja pasirinkima

        Account prijungtaSaskaita = null;

        // prisijungimo metu
        // paklausti koks pin
        // perskaityti ivesta pin
        // patikrinti ar teisinga
        //      jei taip ileist i programa
        //      jei ne - neprijungt

        while (true) {

            if (prijungtaSaskaita == null) {
                System.out.println("Enter your account number:");

                // naudotojas iveda savo saskaitos numeri
                String ivestasNumeris = s.next();


                var surastaSaskaita = db.findAccount(ivestasNumeris);
                if (surastaSaskaita != null) {
                    // JEIGU randam saskaita (pagal ivesta numeri) is "duomenu bazes" (HashMap)
                    // prijungiam saskaita prie savo sistemos

                    String surastosSaskaitosPin = surastaSaskaita.getPin();

                    System.out.println("Enter your PIN:");
                    String spejamasSlaptazodis = s.next();

                    if (spejamasSlaptazodis.equals(surastosSaskaitosPin)) {
                        prijungtaSaskaita = surastaSaskaita;
                    } else {
                        System.err.println("Incorrect PIN!");
                        continue;
                    }

                } else {
                    // JEIGU saskaitos nerandam
                    // priregistruojam saskaita
                    // prijungiam prie sistemos

                    // paprasyti zmogaus irasyti varda
                    System.out.println("Please enter your name to create a new account:");
                    // gauti ivesta varda ir ji panaudoti
                    String vardas = s.next();

                    try {
                        var naujaSaskaita = new Account(ivestasNumeris, vardas);
                        db.upsertAccount(naujaSaskaita);

                        // prijungiam naujai sukurta account
                        prijungtaSaskaita = naujaSaskaita;
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                    }


                }

            }

            System.out.println("Choose action: add - remove - transfer - history - print - logout - quit");
            String command = s.next();

            switch (command) {
                case "add" -> {
                    // 1. perskaityti kieki (nextDouble)
                    // 2. panaudoti funkcija pridetiPinigus

                    double kiekis = scanDouble(s);
                    double newBalance = prijungtaSaskaita.pridetiPinigus(kiekis);
                    db.upsertAccount(prijungtaSaskaita);
                    System.out.println("Your new balance: " + newBalance + "e");

                }
                case "remove" -> {
                    // 1. perskaityti kieki (nextDouble)
                    // 2. panaudoti funkcija nuimtiPinigus

                    double kiekis = scanDouble(s);
                    boolean success = prijungtaSaskaita.nuimtiPinigus(kiekis);
                    db.upsertAccount(prijungtaSaskaita);
                    System.out.println(success ? "Money withdrawn" : "Insufficient funds");
                }
                case "print" -> prijungtaSaskaita.spausdinkInfo();
                case "logout" -> prijungtaSaskaita = null;
                case "quit" -> {
                    System.out.println("Good bye");
                    try {
                        db.closeConnection();
                    } catch (SQLException e) {
                        System.err.println("Error closing the connection: " + e.getMessage());
                    }
                    return;
                }
                case "transfer" -> {
                    double kiekis = scanDouble(s);
                    String gavejoSaskaitosNumeris = s.next();

                    // ar egzistuoja gavejo saskaita
                    Account rastaSaskaita = db.findAccount(gavejoSaskaitosNumeris);

                    if (rastaSaskaita != null) {
                        prijungtaSaskaita.pervestiPinigus(kiekis, rastaSaskaita);
                        db.upsertAccount(prijungtaSaskaita);
                        db.upsertAccount(rastaSaskaita);
                    } else {
                        System.err.println("Account with this number does not exist");
                    }
                }
                case "history" -> {
                    prijungtaSaskaita.spausdinkMokejimus();
                }
                default -> System.err.println("Command invalid");
            }

        }
    }


    private static double scanDouble(Scanner s) {
        try {
            return s.nextDouble();
        } catch (InputMismatchException e) {
//            e.printStackTrace();

            System.err.println("Invalid number entered");
            return 0;
        }
    }
}
