public class Transaction {

    private double mokejimoSuma;
    private Account siuntejas;
    private Account gavejas;

    public Transaction(double mokejimoSuma, Account siuntejas, Account gavejas) {
        this.mokejimoSuma = mokejimoSuma;
        this.siuntejas = siuntejas;
        this.gavejas = gavejas;
    }


    public void printTransaction() {
        System.out.printf("### %s$ : %s -> %s\n", mokejimoSuma,
                siuntejas.getSaskaitosNumeris(), gavejas.getSaskaitosNumeris());
    }
}
