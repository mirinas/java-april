package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Getter
public class Account {

    /**
     * Sukuria sąskaitą, patikrina ar sąskaitos numeris prasideda iš LT, automatiškai sugeneruoja PIN kodą.
     * @param numeris Naujos sąskaitos numeris
     * @param vardas Sąskaitos savininko vardas
     * @throws Exception Meta klaidą, jei sąskaita neprasideda LT
     */
    public Account(String numeris, String vardas) throws Exception {
        if (!numeris.startsWith("LT")) {
            throw new Exception("Account number must start with LT");
        }

        saskaitosNumeris = numeris;
        pilnasVardas = vardas;

        // sukuriam random skaiciu nuo 1000 - 9999
        pin = String.valueOf((int) (Math.random() * 9000 + 1000));
        System.out.println("Your new PIN is " + pin);
    }

    public Account(String saskaitosNumeris, String pilnasVardas, double likutis, String pin) {
        this.saskaitosNumeris = saskaitosNumeris;
        this.pilnasVardas = pilnasVardas;
        this.likutis = likutis;
        this.pin = pin;
    }

    private String saskaitosNumeris;
    private String pilnasVardas;
    private double likutis = 0;
    private final String pin;

    private ArrayList<Transaction> mokejimuIstorija = new ArrayList<>();


    public double pridetiPinigus(double kiekis) {
        // patikrinti ar kiekis teigiamas
        if (kiekis > 0) {
            likutis = likutis + kiekis;
        }
        return likutis;
    }


    /**
     * Naudoja funkcijas pridetiPinigus ir nuimtiPinigus tam, kad pervesti pingus iš šios sąskaitos į nurodytą.
     * @param kiekis Pervedamų pinigų kiekis
     * @param gavejas Sąskaita kuriai bus pridėta pinigų
     * @return true jei pervedimas sėkmingas, false jei neužtenka pinigų
     */
    public boolean pervestiPinigus(double kiekis, Account gavejas) {
        boolean pavyko = nuimtiPinigus(kiekis);

        if (pavyko) {
            gavejas.pridetiPinigus(kiekis);

            Transaction pervedimas = new Transaction(kiekis, this, gavejas);
            mokejimuIstorija.add(pervedimas);

            return true;

        } else {
            return false;
        }
    }


    public boolean nuimtiPinigus(double kiekis) {
        if (kiekis < 0) return false;
        if (kiekis > likutis) return false;

        likutis -= kiekis;
        return true;
    }


    public void spausdinkInfo() {
        System.out.println("---------------------------");
        System.out.println(saskaitosNumeris + ": " + pilnasVardas);
        System.out.println("Balance: $" + likutis);
    }


    public void spausdinkMokejimus() {
        for (Transaction tx : mokejimuIstorija) {
            tx.printTransaction();
        }
    }
}
