package org.example;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Transaction {

    private double mokejimoSuma;
    private Account siuntejas;
    private Account gavejas;

    public void printTransaction() {
        System.out.printf("### %s$ : %s -> %s\n", mokejimoSuma,
                siuntejas.getSaskaitosNumeris(), gavejas.getSaskaitosNumeris());
    }
}
