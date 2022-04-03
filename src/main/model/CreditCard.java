package model;


import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a credit card having a bank,owner,
// card type, cvv, credit limit and a list of its transactions
public class CreditCard implements Card, Writable {
    private final String bankName;  // bank name
    private final String ownerName; // owner name
    private final String cardType;  // type of card (credit)
    private final String cvv;       // 3 digit cvv
    private double creditLimit;     // credit limit on credit card
    private List<Transaction> transactions;  // list of all transactions on this card

    // MODIFIES: this
    // EFFECTS: initializes bankName, ownerName and cvv to parameters,
    //          creditLimit to 5000, cardType to "CREDIT CARD"
    //          and initializes transactions to an ArrayList
    public CreditCard(String bankName, String ownerName, String cvv) {
        this.bankName = bankName;
        this.ownerName = ownerName;
        this.cardType = "CREDIT CARD";
        this.cvv = cvv;
        this.creditLimit = 5000;
        this.transactions = new ArrayList<>();
    }

    // MODIFIES : this
    // EFFECTS : creates an instance of a credit card from loaded json data
    public CreditCard(String bankName, String ownerName, String cvv,
                      double creditLimit, List<Transaction> transactions) {
        this.bankName = bankName;
        this.ownerName = ownerName;
        this.cvv = cvv;
        this.cardType = "CREDIT CARD";
        this.creditLimit = creditLimit;
        this.transactions = transactions;
    }

    // MODIFIES: this
    // EFFECTS:  deducts val from creditLimit, creates a new transaction and
    //           adds it to the list of transactions
    public void pay(double val, String name) {
        creditLimit -= val;
        Transaction transaction = new Transaction(name, val);
        transactions.add(transaction);
    }


    // GETTERS

    // EFFECTS : produces how much credit this person has on this credit card
    public double getCardStatus() {
        return getCreditLimit();
    }

    // EFFECTS : returns the remaining credit limit of this credit card
    public double getCreditLimit() {
        return creditLimit;
    }

    // EFFECTS : returns the name of the bank this debit card is from
    public String getBankName() {
        return bankName;
    }

    // EFFECTS : returns the name of the owner of this debit card
    public String getOwnerName() {
        return ownerName;
    }

    // EFFECTS : returns the type of this card (CREDIT )
    public String getCardType() {
        return cardType;
    }

    // EFFECTS : returns the CVV of this credit card
    public String getCvv() {
        return cvv;
    }

    // EFFECTS : returns the transactions of this credit card
    public List<Transaction> getTransactions() {
        return transactions;
    }

    // EFFECTS : returns a json object of the given credit card
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("bankName", bankName);
        json.put("ownerName", ownerName);
        json.put("cardType", cardType);
        json.put("cvv", cvv);
        json.put("creditLimit", creditLimit);
        json.put("transactions", transactions);
        return json;
    }

}
