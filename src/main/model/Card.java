package model;

import org.json.JSONObject;

import java.util.List;

// an interface representing the similarities between
// credit or debit cards
public interface Card {
    void pay(double val, String name);

    double getCardStatus();

    String getBankName();

    String getOwnerName();

    String getCardType();

    String getCvv();

    List<Transaction> getTransactions();

    JSONObject toJson();
}
