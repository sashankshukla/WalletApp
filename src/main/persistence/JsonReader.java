package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: reads wallet from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Wallet read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWallet(jsonObject);
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // Method taken from JSONReader class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    // EFFECTS: parses wallet from JSON object and returns it
    private Wallet parseWallet(JSONObject jsonObject) {
        Wallet wallet = new Wallet();
        addCards(wallet, jsonObject);
        return wallet;
    }

    // MODIFIES: wallet
    // EFFECTS: parses cards from JSON object and adds them to wallet
    private void addCards(Wallet wallet, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("wallet");
        for (Object json : jsonArray) {
            JSONObject nextCard = (JSONObject) json;
            addCard(wallet, nextCard);
        }
    }

    // EFFECTS : converts a JSON object of a transaction to a transaction
    public Transaction toTransaction(JSONObject jsonObject) {
        String transactionName = jsonObject.getString("transactionName");
        double amount = jsonObject.getDouble("amount");
        return new Transaction(transactionName, amount);
    }

    // REQUIRES : jsonArray != null
    // EFFECTS : converts a JSONArray of transactions to a list of transactions
    public List<Transaction> toArrayList(JSONArray jsonArray) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            transactions.add(toTransaction(jsonArray.getJSONObject(i)));
        }
        return transactions;
    }

    // MODIFIES: wallet
    // EFFECTS: parses card from JSON object and adds it to wallet
    private void addCard(Wallet wallet, JSONObject jsonObject) {
        String cardType = jsonObject.getString("cardType");
        String bankName = jsonObject.getString("bankName");
        String ownerName = jsonObject.getString("ownerName");
        String cvv = jsonObject.getString("cvv");
        JSONArray transactionsJsonArray = jsonObject.getJSONArray("transactions");
        List<Transaction> transactions = toArrayList(transactionsJsonArray);

        if (cardType.equals("CREDIT CARD")) {
            double creditLimit = jsonObject.getDouble("creditLimit");
            wallet.addCreditCard(bankName, ownerName, cvv, creditLimit, transactions);
        } else {
            double currentBalance = jsonObject.getDouble("currentBalance");
            wallet.addDebitCard(bankName, ownerName, cvv, currentBalance, transactions);
        }
    }
}