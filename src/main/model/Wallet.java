package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a wallet with a collection of
// cards and the current/active card
public class Wallet implements Writable {
    private List<Card> wallet; // list of all cards in the wallet
    private Card currentCard;  // the card that is currently active

    // MODIFIES: this
    // EFFECTS: initializes wallet to a new arraylist
    //          makes a default current card and adds it to the wallet
    public Wallet() {
        wallet = new ArrayList<>();
    }


    // MODIFIES: this
    // EFFECTS: adds a new credit card to the wallet
    public void addCreditCard(String bankName, String ownerName, String cvv) {
        Card card = new CreditCard(bankName, ownerName, cvv);
        if (wallet.size() == 0) {
            currentCard = card;
        }
        wallet.add(card);
        EventLog.getInstance().logEvent(new Event("Credit card from bank "
                + bankName + " belonging to " + ownerName + " was added successfully"));
    }

    // MODIFIES : this
    // EFFECTS : adds credit card to wallet from loaded json data
    public void addCreditCard(String bankName, String ownerName, String cvv,
                              double creditLimit, List<Transaction> transactions) {
        Card card = new CreditCard(bankName, ownerName, cvv, creditLimit, transactions);
        wallet.add(card);
        currentCard = wallet.get(0);
    }

    // MODIFIES : this
    // EFFECTS : adds debit card to wallet from loaded json data
    public void addDebitCard(String bankName, String ownerName, String cvv,
                             double currentBalance, List<Transaction> transactions) {
        Card card = new DebitCard(bankName, ownerName, cvv, currentBalance, transactions);
        if (wallet.size() == 0) {
            currentCard = card;
        }
        wallet.add(card);
    }


    // MODIFIES: this
    // EFFECTS: adds a new debit card to the wallet
    public void addDebitCard(String bankName, String ownerName, String cvv, double currentBalance) {
        Card card = new DebitCard(bankName, ownerName, cvv, currentBalance);
        wallet.add(card);
        EventLog.getInstance().logEvent(new Event("Debit card from bank " + bankName
                + " belonging to " + ownerName + " was added successfully"));
        currentCard = wallet.get(0);
    }

    // MODIFIES: this
    // EFFECTS:  removes card from wallet at the specified index
    public boolean removeCard(int index) {
        --index;
        if (getWalletSize() != 0 && wallet.get(index) != currentCard) {
            wallet.remove(index);
            EventLog.getInstance().logEvent(new Event("Card at index " + (index + 1) + " was removed successfully"));
            return true;
        }
        EventLog.getInstance().logEvent(new Event("Unsuccessful : card could not be removed"));
        return false;
    }


    // MODIFIES: this
    // EFFECTS: switches currentCard to the card at index-1 in the wallet
    public boolean switchCurrentCard(int index) {
        if (getWalletSize() != 0) {
            --index;
            currentCard = wallet.get(index);
            EventLog.getInstance().logEvent(new Event("Current card was switched successfully"));
            return true;
        }
        EventLog.getInstance().logEvent(new Event("Unsuccessful : Current card was not changed"));
        return false;
    }

    // GETTERS

    // EFFECTS : returns the current card of the wallet
    public Card getCurrentCard() {
        return currentCard;
    }

    // EFFECTS : returns the number of cards in the wallet
    public int getWalletSize() {
        return wallet.size();
    }

    //EFFECTS : returns the wallet
    public List<Card> getWallet() {
        return wallet;
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS : returns a json object of this wallet to be stored
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("wallet", walletToJson());
        json.put("currentCard", currentCard);
        return json;
    }

    // EFFECTS: creates a JSONArray of all cards in this wallet
    private JSONArray walletToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Card card : wallet) {
            jsonArray.put(card.toJson());
        }

        return jsonArray;
    }
}
