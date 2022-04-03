package ui;


import model.Card;
import model.Transaction;
import model.Wallet;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class WalletApp {
    private static final String JSON_STORE = "./data/wallet.json";
    private Scanner input;
    private Wallet wallet;
    private int option;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // MODIFIES: this
    // EFFECTS: initializes scanner, creates wallet instance, and runs the wallet app
    public WalletApp() {
        input = new Scanner(System.in);
        wallet = new Wallet();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runApp();
    }

    // REQUIRES: 0 <= option <= 11
    // MODIFIES: this
    // EFFECTS: displays App menu and calls appropriate methods for Wallet App
    @SuppressWarnings("methodlength")
    private void runApp() {
        option = 0;
        displayMenu();
        option = Integer.parseInt(input.nextLine());
        while (option != 11) {
            if (option == 1) {
                showCards();
            } else if (option == 2) {
                addCardToWallet();
            } else if (option == 3) {
                removeCardFromWallet();
            } else if (option == 4) {
                viewCard();
            } else if (option == 5) {
                viewTransactions();
            } else if (option == 6) {
                switchCurrentCard();
            } else if (option == 7) {
                makePayment();
            } else if (option == 8) {
                getStatus();
            } else if (option == 9) {
                saveWallet();
            } else if (option == 10) {
                loadWallet();
            }
            displayMenu();
            option = Integer.parseInt(input.nextLine());
            if (option == 11) {
                System.out.println("THANK YOU! HOPE TO SEE YOU AGAIN");
            }
        }
    }

    // REQUIRES: wallet.size()>0
    // EFFECTS : returns credit limit / balance of the current card
    public void getStatus() {
        System.out.println("-------------------------------------------------------");
        if (wallet.getCurrentCard().getCardType().equals("CREDIT CARD")) {
            System.out.println("CURRENT CARD : CREDIT CARD \nREMAINING CREDIT : "
                    + wallet.getCurrentCard().getCardStatus());
        } else {
            System.out.println("CURRENT CARD : DEBIT CARD \nREMAINING BALANCE : "
                    + wallet.getCurrentCard().getCardStatus());
        }
        System.out.println("-------------------------------------------------------");
    }

    // EFFECTS: Displays a list of every card in the wallet
    //          with its corresponding information
    public void showCards() {
        int i = 1;
        for (Card card : wallet.getWallet()) {
            System.out.println(i + ". " + card.getCardType() + " | "
                    + "NAME : " + card.getOwnerName() + " | BANK : " + card.getBankName());
            ++i;
        }
    }

    // EFFECTS: Displays every transaction in the list of transactions
    //          for the current card
    public void viewTransactions() {
        System.out.println("**********************************");
        int i = 1;
        for (Transaction transaction : wallet.getCurrentCard().getTransactions()) {
            System.out.println(i + " : " + transaction.getTransactionName() + " - $" + transaction.getAmount());
            ++i;
        }
        System.out.println("**********************************");
    }

    // REQUIRES: wallet.size()>0
    // EFFECTS: prints out a basic representation of the current card
    public void viewCard() {
        System.out.println("-----------------------------------------");
        System.out.println("-----------------------------------------");
        System.out.println(wallet.getCurrentCard().getCardType());
        System.out.println("BANK: " + wallet.getCurrentCard().getBankName() + "   NAME: "
                + wallet.getCurrentCard().getOwnerName());
        System.out.println("-----------------------------------------");
        System.out.println("-----------------------------------------");
    }

    // REQUIRES: 1 <= index <= getWalletSize()
    // MODIFIES: this
    // EFFECTS: displays list of cards and switches the current card based on user choice
    private void switchCurrentCard() {
        showCards();
        System.out.println("Enter the index of the card you want to choose :");
        int index = Integer.parseInt(input.nextLine());
        boolean check = wallet.switchCurrentCard(index);
        if (check) {
            System.out.println("Your current card was successfully changed!");
        } else {
            System.out.println("Sorry the wallet is empty");
        }
    }

    // REQUIRES: wallet.size() > 0, paymentValue > 0,
    //           getBalance() - paymentValue > 0 or getCreditLimit() - paymentValue > 0
    // MODIFIES: this
    // EFFECTS:  deducts payment value from card and adds transaction to list
    private void makePayment() {
        System.out.println("Enter the institution you'd like to pay");
        String institution = input.nextLine();
        System.out.println("Enter the amount to be paid");
        double paymentValue = Double.parseDouble(input.nextLine());
        wallet.getCurrentCard().pay(paymentValue, institution);
        System.out.println("Payment successful!");
    }

    // REQUIRES: 1 <= index <= getWalletSize()
    // MODIFIES: this
    // EFFECTS:  removes card from wallet based on users choice
    private void removeCardFromWallet() {
        showCards();
        System.out.println("Please enter the index of the card you want to remove");
        int index = Integer.parseInt(input.nextLine());
        boolean check = wallet.removeCard(index);
        if (check) {
            System.out.println("The card was successfully removed!");
        } else {
            System.out.println("You are trying to remove the current card or there are no cards at all");
        }
    }

    // REQUIRES: bankName & fullName must only contain alphabets;
    //           cvv.length() = 3 & must only contain numeric characters
    //           currentBalance >=0
    // MODIFIES: this
    // EFFECTS: Adds a new credit/debit card to wallet
    private void addCardToWallet() {
        System.out.println("Enter your bank name :");
        String bankName = input.nextLine();
        System.out.println("Enter your full name :");
        String fullName = input.nextLine();
        System.out.println("Enter your 3-digit CVV number :");
        String cvv = input.nextLine();
        System.out.println("Please enter 1 to add a credit card or 2 for debit");
        int cardChoice = Integer.parseInt(input.nextLine());
        if (cardChoice == 1) {
            wallet.addCreditCard(bankName, fullName, cvv);
        } else {
            System.out.println("Enter your current balance");
            double currentBalance = Double.parseDouble(input.nextLine());
            wallet.addDebitCard(bankName, fullName, cvv, currentBalance);
        }
        System.out.println("The card has been added!");
    }

    // EFFECTS: saves the wallet to file
    private void saveWallet() {
        try {
            jsonWriter.open();
            jsonWriter.write(wallet);
            jsonWriter.close();
            System.out.println("Your wallet has been saved. You can access it whenever you'd like");
        } catch (FileNotFoundException e) {
            System.out.println("SORRY! We are Unable to save your wallet");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads wallet from file
    private void loadWallet() {
        try {
            wallet = jsonReader.read();
            System.out.println("Loading your wallet... ");

        } catch (IOException e) {
            System.out.println("SORRY! We are unable to fetch your wallet");
        }
    }

    // EFFECTS: displays user driven wallet app menu
    private void displayMenu() {
        System.out.println("Pick one of the following options:");
        System.out.println("1 -> View Wallet");
        System.out.println("2 -> Add Card to Wallet");
        System.out.println("3 -> Remove a card from the wallet");
        System.out.println("4 -> View Current Card");
        System.out.println("5 -> View Card Transaction History");
        System.out.println("6 -> Switch Current Card");
        System.out.println("7 -> Make Payment");
        System.out.println("8 -> Check Credit Limit / Card Balance of Card");
        System.out.println("9 -> Save work room to file");
        System.out.println("10 -> Load work room from file");
        System.out.println("11 -> EXIT");
    }


}