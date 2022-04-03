package ui;


import model.Card;
import model.Event;
import model.EventLog;
import model.Transaction;
import model.Wallet;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;


//class representing the wallet GUI and its various frames
public class GUI extends JFrame implements ActionListener, LogPrinter {
    public static final int WELCOME_WIDTH = 500;
    public static final int WELCOME_HEIGHT = 750;
    private static final String JSON_STORE = "./data/wallet.json";

    private JsonReader jsonReader; //reader
    private JsonWriter jsonWriter; // writer
    private Wallet wallet; // wallet

    private JFrame addCardFrame; // frame to add a new card
    private JTextField bankName; // bank name
    private JTextField fullName; // full name
    private JTextField cvv;      // cvv
    private JTextField currentBalance; // current balance
    private JRadioButton cc; // radio button for credit card
    private JRadioButton dc; // radio button for debit card

    private JFrame viewCardsFrame; // frame to view cards

    private JFrame makePaymentFrame; // frame to make a payment
    private JTextField transactionName; // transaction name
    private JTextField transactionAmount; // transaction amount

    private JFrame successFrame; // success popup frame
    private JFrame failFrame;  // fail popup frame

    private JList<String> list; // stores all cards
    private JButton removeButton; // remove card button
    private JButton switchButton; // switch current card button

    private JFrame transactionHistoryFrame; // frame to view transaction history for the current card

    private JFrame loadFrame;

    //MODIFIES : this , wallet
    // EFFECTS : initializes json reader,writer ,instantiates wallet and displays main GUI Page
    public GUI() {
        super("Wallet App");
        init();
        setPreferredSize(new Dimension(WELCOME_WIDTH, WELCOME_HEIGHT));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.DARK_GRAY);
        loadingFrame();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException exception) {
            System.out.println("not working");
        }
        mainPage();
    }

    // EFFECTS : initializes json reader and writer to directory of json file
    private void init() {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        wallet = new Wallet();
    }


    // EFFECTS : creates a loading screen before starting the app
    private void loadingFrame() {
        loadFrame = new JFrame("Loading app");
        loadFrame.setPreferredSize(new Dimension(WELCOME_WIDTH, WELCOME_HEIGHT));
        loadFrame.setLayout(new BoxLayout(loadFrame.getContentPane(), BoxLayout.Y_AXIS));
        loadFrame.getContentPane().setBackground(Color.BLACK);

        Icon icon = new ImageIcon("./data/loading.gif");
        JLabel gif = new JLabel(icon);
        gif.setBounds(0, 0, 480, 480);
        loadFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadFrame.add(gif);
        loadFrame.setVisible(true);
        loadFrame.pack();
    }

    // MODIFIES: this
    // EFFECTS: Prepares and displays the home page with options to create or load wallet
    @SuppressWarnings("methodlength")
    private void mainPage() {
        JLabel title = new JLabel("WALLET APP");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Montserrat", Font.PLAIN, 36));
        title.setForeground(Color.WHITE);

        JButton newWallet = new JButton("New Wallet");
        newWallet.setActionCommand("newWallet");
        newWallet.addActionListener(this);
        newWallet.setAlignmentX(Component.CENTER_ALIGNMENT);
        newWallet.setFont(new Font("Montserrat", Font.PLAIN, 20));
        newWallet.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        newWallet.setBackground(new Color(30, 30, 31));
        newWallet.setForeground(Color.LIGHT_GRAY);

        JButton exitButton = new JButton("EXIT");
        exitButton.setActionCommand("exitApp");
        exitButton.addActionListener(this);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setFont(new Font("Montserrat", Font.PLAIN, 20));
        exitButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        exitButton.setBackground(new Color(30, 30, 31));
        exitButton.setForeground(Color.LIGHT_GRAY);


        JLabel walletImage = new JLabel(new ImageIcon("./data/wallet_image.png"));
        walletImage.setPreferredSize(new Dimension(100, 100));
        walletImage.setVisible(true);
        walletImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton loadWallet = new JButton("Load existing wallet");
        loadWallet.setActionCommand("loadWallet");
        loadWallet.addActionListener(this);
        loadWallet.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadWallet.setFont(new Font("Montserrat", Font.PLAIN, 20));
        loadWallet.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        loadWallet.setBackground(new Color(30, 30, 31));
        loadWallet.setForeground(Color.LIGHT_GRAY);

        add(title);
        add(walletImage);
        add(newWallet);
        add(loadWallet);
        add(exitButton);
        setVisible(true);
        pack();
    }


    //MODIFIES : this
    // EFFECTS : Performs appropriate functionality when an action event occurs
    @Override
    @SuppressWarnings("methodlength")
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("newWallet")) {
            optionsPage();
        } else if (e.getActionCommand().equals("exitApp")) {
            printLog(EventLog.getInstance());
            System.exit(0);
        } else if (e.getActionCommand().equals("loadWallet")) {
            load();
        } else if (e.getActionCommand().equals("addCard")) {
            addCard();
        } else if (e.getActionCommand().equals("submit")) {
            completeCardCreation();
        } else if (e.getActionCommand().equals("removeButton")) {
            removeCard();
        } else if (e.getActionCommand().equals("switchButton")) {
            switchCard();
        } else if (e.getActionCommand().equals("makePayment")) {
            makePayment();
        } else if (e.getActionCommand().equals("completePayment")) {
            performPayment();
        } else if (e.getActionCommand().equals("viewTransactionHistory")) {
            viewTransactionHistory();
        } else if (e.getActionCommand().equals("saveWallet")) {
            write();
        } else {
            viewCards();
        }
    }

    // Method taken from ScreenPrinter class in
    // https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
    // EFFECTS : displays the log of events occurred once application is closed
    @Override
    public void printLog(EventLog el) {
        for (Event event : el) {
            System.out.println(event.getDescription() + " on " + event.getDate());
        }
    }

    // REQUIRES: currentBalance.getText() must be numeric
    // MODIFIES: this ,wallet
    // EFFECTS : creates a new card based on user input
    public void completeCardCreation() {
        double balance = Double.parseDouble(currentBalance.getText());
        if (cc.isSelected()) {
            wallet.addCreditCard(bankName.getText(), fullName.getText(), cvv.getText());
        } else if (dc.isSelected()) {
            wallet.addDebitCard(bankName.getText(), fullName.getText(), cvv.getText(), balance);
        }
        successPage();
    }

    // MODIFIES : this ,wallet
    // EFFECTS : switches current card based on user selection
    private void switchCard() {
        if (!wallet.switchCurrentCard(list.getSelectedIndex() + 1)) {
            failPage();
        } else {
            successPage();
        }
    }

    //REQUIRES: transactionAmount.getText() must be numeric
    // MODIFIES: this , wallet
    // EFFECTS : performs a transaction using the current card
    public void performPayment() {
        String name = transactionName.getText();
        double amount = Double.parseDouble(transactionAmount.getText());
        wallet.getCurrentCard().pay(amount, name);
        successPage();
    }

    //REQUIRES : wallet.size>1
    //MODIFIES : this ,wallet
    // EFFECTS : removes card from wallet based on user selection, and displays new list of cards
    //           along with appropriate success or error popup
    private void removeCard() {
        if (!wallet.removeCard(list.getSelectedIndex() + 1)) {
            failPage();
        } else {
            viewCardsFrame.dispose();
            viewCards();
            successPage();
        }
    }

    // EFFECTS : loads saved wallet state back into program
    private void load() {
        try {
            wallet = jsonReader.read();
            optionsPage();
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Error! No saved data found!");
        }
    }

    // MODIFIES : data.json
    // EFFECTS: tries writing wallet into json file, and if unsuccessful
    // displays error message
    private void write() {
        try {
            jsonWriter.open();
            jsonWriter.write(wallet);
            jsonWriter.close();
            successPage();
        } catch (FileNotFoundException exception) {
            System.out.println("SORRY! We are Unable to save your wallet");
        }
    }

    //REQUIRES: one of cc||dc must be selected, currentBalance must be numeric
    // MODIFIES : this, wallet
    // EFFECTS : frame with functionality to add info and create a new credit and debit card
    @SuppressWarnings("methodlength")
    private void addCard() {
        addCardFrame = new JFrame("Add Card");
        addCardFrame.setPreferredSize(new Dimension(WELCOME_WIDTH, WELCOME_HEIGHT));
        addCardFrame.setLayout(new BoxLayout(addCardFrame.getContentPane(), BoxLayout.Y_AXIS));
        addCardFrame.getContentPane().setBackground(Color.darkGray);
        addCardFrame.setLayout(null);

        JLabel walletImage = new JLabel(new ImageIcon("./data/wallet_icon.png"));
        walletImage.setVisible(true);
        walletImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        walletImage.setBounds(220, 20, 60, 60);

        JLabel title = new JLabel("Choose one of the following options:");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Montserrat", Font.PLAIN, 20));
        title.setForeground(Color.WHITE);
        title.setBounds(65, 120, 400, 25);

        bankName = new JTextField("Enter your bank name");
        bankName.setBounds(90, 150, 300, 50);
        bankName.setFont(new Font("Montserrat", Font.PLAIN, 20));

        fullName = new JTextField("Enter your full name");
        fullName.setBounds(90, 215, 300, 50);
        fullName.setFont(new Font("Montserrat", Font.PLAIN, 20));

        cvv = new JTextField("Enter your 3 digit cvv");
        cvv.setBounds(90, 280, 300, 50);
        cvv.setFont(new Font("Montserrat", Font.PLAIN, 20));

        cc = new JRadioButton("Credit Card");
        cc.setFont(new Font("Montserrat", Font.PLAIN, 20));
        dc = new JRadioButton("Debit Card");
        dc.setFont(new Font("Montserrat", Font.PLAIN, 20));
        ButtonGroup group = new ButtonGroup();
        group.add(cc);
        group.add(dc);
        cc.setBounds(90, 350, 150, 40);
        dc.setBounds(240, 350, 150, 40);

        currentBalance = new JTextField("Current Balance");
        currentBalance.setBounds(90, 410, 300, 50);
        currentBalance.setFont(new Font("Montserrat", Font.PLAIN, 20));

        JButton submit = new JButton("Add card");
        submit.setBounds(90, 500, 300, 50);
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.setFont(new Font("Montserrat", Font.PLAIN, 28));
        submit.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        submit.setBackground(new Color(30, 30, 31));
        submit.setForeground(Color.LIGHT_GRAY);
        submit.setActionCommand("submit");
        submit.addActionListener(this);


        addCardFrame.add(walletImage);
        addCardFrame.add(title);
        addCardFrame.add(bankName);
        addCardFrame.add(fullName);
        addCardFrame.add(cvv);
        addCardFrame.add(cc);
        addCardFrame.add(dc);
        addCardFrame.add(currentBalance);
        addCardFrame.add(submit);
        addCardFrame.setVisible(true);
        addCardFrame.pack();

    }

    //REQUIRES : wallet.size()!=0 && transactionAmount must be numeric
    //MODIFIES : this ,wallet
    // EFFECTS : creates payment frame to perform a transaction for the current card
    @SuppressWarnings("methodlength")
    private void makePayment() {
        makePaymentFrame = new JFrame("Payment Gateway");
        makePaymentFrame.setPreferredSize(new Dimension(WELCOME_WIDTH, WELCOME_HEIGHT));
        makePaymentFrame.setLayout(new BoxLayout(makePaymentFrame.getContentPane(), BoxLayout.Y_AXIS));
        makePaymentFrame.getContentPane().setBackground(Color.darkGray);
        makePaymentFrame.setLayout(null);

        JLabel walletImage = new JLabel(new ImageIcon("./data/wallet_icon.png"));
        walletImage.setVisible(true);
        walletImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        walletImage.setBounds(220, 20, 60, 60);

        JLabel title = new JLabel("Payment Portal:");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Montserrat", Font.PLAIN, 20));
        title.setForeground(Color.WHITE);
        title.setBounds(190, 100, 400, 25);

        transactionName = new JTextField("Recipient");
        transactionName.setBounds(90, 200, 300, 50);
        transactionName.setFont(new Font("Montserrat", Font.PLAIN, 20));

        transactionAmount = new JTextField("Enter amount");
        transactionAmount.setBounds(90, 300, 300, 50);
        transactionAmount.setFont(new Font("Montserrat", Font.PLAIN, 20));

        JButton completePayment = new JButton("Complete Payment");
        completePayment.setBounds(90, 450, 300, 50);
        completePayment.setAlignmentX(Component.CENTER_ALIGNMENT);
        completePayment.setFont(new Font("Montserrat", Font.PLAIN, 28));
        completePayment.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        completePayment.setBackground(new Color(30, 30, 31));
        completePayment.setForeground(Color.LIGHT_GRAY);
        completePayment.setActionCommand("completePayment");
        completePayment.addActionListener(this);

        makePaymentFrame.add(walletImage);
        makePaymentFrame.add(title);
        makePaymentFrame.add(transactionName);
        makePaymentFrame.add(transactionAmount);
        makePaymentFrame.add(completePayment);
        makePaymentFrame.setVisible(true);
        makePaymentFrame.pack();
    }

    //MODIFIES : this ,wallet
    // EFFECTS : a frame that displays all cards in the current wallet
    @SuppressWarnings("methodlength")
    private void viewCards() {
        viewCardsFrame = new JFrame("Cards");
        viewCardsFrame.setPreferredSize(new Dimension(700, WELCOME_HEIGHT));
        viewCardsFrame.setLayout(new BoxLayout(viewCardsFrame.getContentPane(), BoxLayout.Y_AXIS));
        viewCardsFrame.getContentPane().setBackground(Color.darkGray);
        viewCardsFrame.setLayout(null);

        JLabel walletImage = new JLabel(new ImageIcon("./data/wallet_icon.png"));
        walletImage.setVisible(true);
        walletImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        walletImage.setBounds(280, 20, 60, 60);

        JLabel title = new JLabel("Your wallet:");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Montserrat", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBounds(230, 90, 200, 30);

        String[] cardList = new String[wallet.getWalletSize()];
        int i = 0;
        for (Card card : wallet.getWallet()) {
            String s = (i + 1) + ". " + card.getCardType()
                    + " : " + "  Name : " + card.getOwnerName() + "     Bank : " + card.getBankName();
            cardList[i] = s;
            ++i;
        }

        list = new JList<>(cardList);
        list.setBackground(Color.darkGray);
        list.setFont(new Font("Montserrat", Font.PLAIN, 20));
        list.setBounds(0, 150, 700, 200);
        list.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        list.setForeground(Color.LIGHT_GRAY);
        list.setVisibleRowCount(wallet.getWalletSize());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        removeButton = new JButton("Remove");
        removeButton.setBounds(200, 500, 100, 50);
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setFont(new Font("Montserrat", Font.PLAIN, 24));
        removeButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        removeButton.setBackground(new Color(30, 30, 31));
        removeButton.setForeground(Color.LIGHT_GRAY);
        removeButton.setActionCommand("removeButton");
        removeButton.addActionListener(this);

        switchButton = new JButton("Switch");
        switchButton.setBounds(350, 500, 100, 50);
        switchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        switchButton.setFont(new Font("Montserrat", Font.PLAIN, 25));
        switchButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        switchButton.setBackground(new Color(30, 30, 31));
        switchButton.setForeground(Color.LIGHT_GRAY);
        switchButton.setActionCommand("switchButton");
        switchButton.addActionListener(this);


        JScrollPane jsp = new JScrollPane(list);
        jsp.setBounds(0, 130, 700, 350);
        viewCardsFrame.add(walletImage);
        viewCardsFrame.add(title);
        viewCardsFrame.add(jsp);
        viewCardsFrame.add(removeButton);
        viewCardsFrame.add(switchButton);
        viewCardsFrame.setVisible(true);
        viewCardsFrame.pack();

    }

    //MODIFIES: this
    //EFFECTS : frame that allows user to look at all cards and switch to a new current card
    private void switchCurrentCard() {
        viewCards();
    }


    //REQUIRES : wallet.size()!=0
    // EFFECTS : frame that displays the transaction history of the current card
    private void viewTransactionHistory() {
        transactionHistoryFrame = new JFrame("Transactions");
        transactionHistoryFrame.setPreferredSize(new Dimension(600, 300));
        transactionHistoryFrame.setLayout(new BoxLayout(transactionHistoryFrame.getContentPane(), BoxLayout.Y_AXIS));
        transactionHistoryFrame.getContentPane().setBackground(Color.darkGray);

        String[] transactions = new String[wallet.getCurrentCard().getTransactions().size()];
        int i = 1;
        for (Transaction t : wallet.getCurrentCard().getTransactions()) {
            String transaction = (i) + " . " + t.getTransactionName() + " : "
                    + (t.getAmount()) + "$";
            transactions[i - 1] = transaction;
            ++i;
        }
        JList<String> transactionList = new JList<>(transactions);
        transactionList.setBackground(Color.darkGray);
        transactionList.setFont(new Font("Montserrat", Font.PLAIN, 20));
        transactionList.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        transactionList.setForeground(Color.LIGHT_GRAY);
        transactionList.setVisibleRowCount(wallet.getWalletSize());
        transactionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        transactionHistoryFrame.add(new JScrollPane(transactionList));
        transactionHistoryFrame.setVisible(true);
        transactionHistoryFrame.pack();
    }

    // EFFECTS : a fail popup for unsuccessful changes trying to be made
    private void failPage() {
        failFrame = new JFrame("Fail!");
        failFrame.setPreferredSize(new Dimension(150, 150));
        failFrame.setLayout(new BoxLayout(failFrame.getContentPane(), BoxLayout.Y_AXIS));
        failFrame.getContentPane().setBackground(Color.darkGray);


        JLabel walletImage = new JLabel(new ImageIcon("./data/fail_image.png"));
        walletImage.setVisible(true);
        walletImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        failFrame.add(walletImage);
        failFrame.setVisible(true);
        failFrame.pack();
    }

    // EFFECTS : a fail popup for successful changes trying to be made
    private void successPage() {
        successFrame = new JFrame("Success!");
        successFrame.setPreferredSize(new Dimension(150, 150));
        successFrame.setLayout(new BoxLayout(successFrame.getContentPane(), BoxLayout.Y_AXIS));
        successFrame.getContentPane().setBackground(Color.darkGray);


        JLabel walletImage = new JLabel(new ImageIcon("./data/sucess_image.png"));
        walletImage.setVisible(true);
        walletImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        successFrame.add(walletImage);
        successFrame.setVisible(true);
        successFrame.pack();
    }

    // MODIFIES: this
    // EFFECTS : frame with all functionality options to work on the wallet
    @SuppressWarnings("methodlength")
    private void optionsPage() {
        JFrame optionsFrame = new JFrame("Options");
        optionsFrame.setPreferredSize(new Dimension(WELCOME_WIDTH, WELCOME_HEIGHT));
        optionsFrame.setLayout(new BoxLayout(optionsFrame.getContentPane(), BoxLayout.Y_AXIS));
        optionsFrame.getContentPane().setBackground(Color.darkGray);


        JLabel walletImage = new JLabel(new ImageIcon("./data/wallet_icon.png"));
        walletImage.setVisible(true);
        walletImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Choose one of the following options:");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Montserrat", Font.PLAIN, 25));
        title.setForeground(Color.WHITE);

        JButton addCard = new JButton("Add a card");
        addCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        addCard.setFont(new Font("Montserrat", Font.PLAIN, 28));
        addCard.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        addCard.setBackground(new Color(30, 30, 31));
        addCard.setForeground(Color.LIGHT_GRAY);
        addCard.setActionCommand("addCard");
        addCard.addActionListener(this);

        JButton removeCard = new JButton("Remove a card.");
        removeCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeCard.setFont(new Font("Montserrat", Font.PLAIN, 28));
        removeCard.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        removeCard.setBackground(new Color(30, 30, 31));
        removeCard.setForeground(Color.LIGHT_GRAY);
        removeCard.setActionCommand("removeCard");
        removeCard.addActionListener(this);

        JButton makePayment = new JButton("Make a payment");
        makePayment.setAlignmentX(Component.CENTER_ALIGNMENT);
        makePayment.setFont(new Font("Montserrat", Font.PLAIN, 28));
        makePayment.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        makePayment.setBackground(new Color(30, 30, 31));
        makePayment.setForeground(Color.LIGHT_GRAY);
        makePayment.setActionCommand("makePayment");
        makePayment.addActionListener(this);

        JButton switchCurrentCard = new JButton("Switch Current Card");
        switchCurrentCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        switchCurrentCard.setFont(new Font("Montserrat", Font.PLAIN, 28));
        switchCurrentCard.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        switchCurrentCard.setBackground(new Color(30, 30, 31));
        switchCurrentCard.setForeground(Color.LIGHT_GRAY);
        switchCurrentCard.setActionCommand("switchCurrentCard");
        switchCurrentCard.addActionListener(this);

        JButton viewCards = new JButton("View all cards");
        viewCards.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewCards.setFont(new Font("Montserrat", Font.PLAIN, 28));
        viewCards.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        viewCards.setBackground(new Color(30, 30, 31));
        viewCards.setForeground(Color.LIGHT_GRAY);
        viewCards.setActionCommand("viewCards");
        viewCards.addActionListener(this);

        JButton viewTransactionHistory = new JButton("View Transaction History");
        viewTransactionHistory.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewTransactionHistory.setFont(new Font("Montserrat", Font.PLAIN, 28));
        viewTransactionHistory.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        viewTransactionHistory.setBackground(new Color(30, 30, 31));
        viewTransactionHistory.setForeground(Color.LIGHT_GRAY);
        viewTransactionHistory.setActionCommand("viewTransactionHistory");
        viewTransactionHistory.addActionListener(this);

        JButton saveWallet = new JButton("saveWallet");
        saveWallet.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveWallet.setFont(new Font("Montserrat", Font.PLAIN, 28));
        saveWallet.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        saveWallet.setBackground(new Color(30, 30, 31));
        saveWallet.setForeground(Color.LIGHT_GRAY);
        saveWallet.setActionCommand("saveWallet");
        saveWallet.addActionListener(this);


        optionsFrame.add(walletImage);
        optionsFrame.add(title);
        optionsFrame.add(addCard);
        optionsFrame.add(removeCard);
        optionsFrame.add(makePayment);
        optionsFrame.add(switchCurrentCard);
        optionsFrame.add(viewCards);
        optionsFrame.add(viewTransactionHistory);
        optionsFrame.add(saveWallet);
        optionsFrame.setVisible(true);
        optionsFrame.pack();
    }


}

