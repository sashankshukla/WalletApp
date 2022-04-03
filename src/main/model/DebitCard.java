package model;


import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a debit card having a bank,owner,
// card type, cvv, card balance and a list of its transactions
public class DebitCard implements Card, Writable {
    private final String bankName;  // bank name
    private final String ownerName; // owner name
    private final String cardType;  // type of card (debit)
    private final String cvv;       // 3 digit cvv
    private double currentBalance;  // balance remaining on card
    private List<Transaction> transactions;  // list of all transactions in this card

    // MODIFIES: this
    // EFFECTS: initializes bankName, ownerName, cvv, and currentBalance
    //          to given parameters, cardType to "DEBIT CARD", and
    //          initializes transactions to an ArrayList
    public DebitCard(String bankName, String ownerName, String cvv, double currentBalance) {
        this.bankName = bankName;
        this.ownerName = ownerName;
        this.cardType = "DEBIT CARD";
        this.cvv = cvv;
        this.currentBalance = currentBalance;
        this.transactions = new ArrayList<>();
    }

    // MODIFIES : this
    // EFFECTS : creates a new instance of a debit card based on loaded json data
    public DebitCard(String bankName, String ownerName, String cvv,
                     double currentBalance, List<Transaction> transactions) {
        this.bankName = bankName;
        this.ownerName = ownerName;
        this.cvv = cvv;
        this.cardType = "DEBIT CARD";
        this.currentBalance = currentBalance;
        this.transactions = transactions;
    }


    // MODIFIES: this
    // EFFECTS:  deducts val from currentBalance, creates a new
    //           transaction and adds it to the list of transactions
    public void pay(double val, String name) {
        currentBalance -= val;
        Transaction transaction = new Transaction(name, val);
        transactions.add(transaction);
    }


    //GETTERS

    // EFFECTS : produces the current balance of this debit card
    public double getCardStatus() {
        return getBalance();
    }

    // EFFECTS : returns the current balance of this debit card
    public double getBalance() {
        return currentBalance;
    }

    // EFFECTS : returns the name of the bank this debit card is from
    public String getBankName() {
        return bankName;
    }

    // EFFECTS : returns the name of the owner of this debit card
    public String getOwnerName() {
        return ownerName;
    }

    // EFFECTS : returns the type of this card (DEBIT)
    public String getCardType() {
        return cardType;
    }

    // EFFECTS : returns the CVV of this debit card
    public String getCvv() {
        return cvv;
    }

    // EFFECTS : returns the transactions of this credit card
    public List<Transaction> getTransactions() {
        return transactions;
    }

    // EFFECTS : creates a JSON object of the current debit card to store
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("bankName", bankName);
        json.put("ownerName", ownerName);
        json.put("cardType", cardType);
        json.put("cvv", cvv);
        json.put("currentBalance", currentBalance);
        json.put("transactions", transactions);
        return json;
    }
}
