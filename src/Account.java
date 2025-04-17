import java.util.ArrayList;

public class Account {

    public Account(String numeris, String vardas) throws Exception {
        if(!numeris.startsWith("LT")) {
            throw new Exception("Account number must start with LT");
        }

        saskaitosNumeris = numeris;
        pilnasVardas = vardas;

        // sukuriam random skaiciu nuo 1000 - 9999
        pin = String.valueOf((int) (Math.random() * 9000 + 1000));
        System.out.println("Your new PIN is " + pin);
    }

    private String saskaitosNumeris;
    private String pilnasVardas;

    private double likutis = 0;
    private final String pin;
    private ArrayList<Transaction> mokejimuIstorija = new ArrayList<>();


    public double pridetiPinigus(double kiekis) {
        // patikrinti ar kiekis teigiamas
        if(kiekis > 0) {
            likutis = likutis + kiekis;
        }
        return likutis;
    }


    public boolean pervestiPinigus(double kiekis, Account gavejas) {
        boolean pavyko = nuimtiPinigus(kiekis);

        if(pavyko) {
            gavejas.pridetiPinigus(kiekis);

            Transaction pervedimas = new Transaction(kiekis, this, gavejas);
            mokejimuIstorija.add(pervedimas);

            return true;

        } else {
            return false;
        }
    }


    public boolean nuimtiPinigus(double kiekis) {
        if(kiekis < 0) return false;
        if(kiekis > likutis) return false;

        likutis -= kiekis;
        return true;
    }


    public void spausdinkInfo() {
        System.out.println("---------------------------");
        System.out.println(saskaitosNumeris + ": " + pilnasVardas);
        System.out.println("Balance: $" + likutis);
    }


    public void spausdinkMokejimus() {
        for(Transaction tx : mokejimuIstorija) {
            tx.printTransaction();
        }
    }


    public String getSaskaitosNumeris() {
        return saskaitosNumeris;
    }

    public String getPin() {
        return pin;
    }
}
