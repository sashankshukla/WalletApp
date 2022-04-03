package model;



public class Transaction {
    private final String transactionName;
    private final double amount;

    // MODIFIES: this
    // EFFECTS: initializes transactionName and amount to parameters
    public Transaction(String transactionName, double amount) {
        this.transactionName = transactionName;
        this.amount = amount;
    }

    // GETTERS

    // EFFECTS : returns the transaction name of this transaction
    public String getTransactionName() {
        return transactionName;
    }

    // EFFECTS : returns the amount of this transaction
    public double getAmount() {
        return amount;
    }



}
