package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    Wallet test;

    @BeforeEach
    public void runBefore() {
        test = new Wallet();
    }

    @Test
    public void testWallet() {
        assertEquals(0, test.getWalletSize());
        assertNull(test.getCurrentCard());
    }

    @Test
    public void testAddCreditCard() {
        test.addCreditCard("CIBC", "Sashank Shukla", "167");
        assertEquals(1, test.getWalletSize());
        assertEquals("CIBC", test.getWallet().get(0).getBankName());
        assertEquals("Sashank Shukla", test.getWallet().get(0).getOwnerName());
        assertEquals("167", test.getWallet().get(0).getCvv());
        assertEquals(1, test.getWalletSize());
        assertEquals("CIBC", test.getCurrentCard().getBankName());
        assertEquals("Sashank Shukla", test.getCurrentCard().getOwnerName());
        assertEquals("167", test.getCurrentCard().getCvv());
        test.addCreditCard("TD", "Ethan Grunys", "999");
        assertEquals(2, test.getWalletSize());
        assertEquals("TD", test.getWallet().get(1).getBankName());
        assertEquals("Ethan Grunys", test.getWallet().get(1).getOwnerName());
        assertEquals("999", test.getWallet().get(1).getCvv());
    }

    @Test
    public void testAddDebitCard() {
        assertEquals(0, test.getWalletSize());
        List<Transaction> transactions = new ArrayList<>();
        test.addDebitCard("C", "S", "111", 200, transactions);
        assertEquals(1, test.getWalletSize());
        assertEquals(test.getWallet().get(0) , test.getCurrentCard());
        assertEquals("C", test.getCurrentCard().getBankName());
        assertEquals("S", test.getCurrentCard().getOwnerName());
        assertEquals("111", test.getCurrentCard().getCvv());
        assertEquals(200, test.getCurrentCard().getCardStatus());
        assertEquals(transactions, test.getCurrentCard().getTransactions());
        test.addDebitCard("CIBC", "Sashank Shukla", "167", 1000);
        assertEquals(2, test.getWalletSize());
        assertNotEquals(test.getWallet().get(1) , test.getCurrentCard());
        assertEquals("CIBC", test.getWallet().get(1).getBankName());
        assertEquals("Sashank Shukla", test.getWallet().get(1).getOwnerName());
        assertEquals("167", test.getWallet().get(1).getCvv());
        assertEquals(1000, test.getWallet().get(1).getCardStatus());
        test.addDebitCard("TD", "Ethan Grunys", "999", 4000);
        assertEquals(3, test.getWalletSize());
        assertEquals("TD", test.getWallet().get(2).getBankName());
        assertEquals("Ethan Grunys", test.getWallet().get(2).getOwnerName());
        assertEquals("999", test.getWallet().get(2).getCvv());
        test.addDebitCard("B", "A", "112", 210, transactions);
        assertNotEquals("B" , test.getCurrentCard().getBankName());
        assertNotEquals("A" , test.getCurrentCard().getOwnerName());
        assertNotEquals("112" , test.getCurrentCard().getCvv());
        assertNotEquals(210 , test.getCurrentCard().getCardStatus());
    }


    @Test
    public void testRemoveCard() {
        assertFalse(test.removeCard(1));
        test.addCreditCard("SCOTIABANK", "Sarthak Nath", "887");
        assertFalse(test.removeCard(1));
        test.addCreditCard("AMERICAN EXPRESS", "Akshit S", "335");
        test.addCreditCard("CIBC", "Sashank Shukla", "654");
        assertFalse(test.removeCard(1));
        assertEquals(3, test.getWalletSize());
        assertTrue(test.removeCard(2));
        assertEquals(2, test.getWalletSize());
        assertEquals("SCOTIABANK", test.getWallet().get(0).getBankName());
        assertEquals("Sarthak Nath", test.getWallet().get(0).getOwnerName());
        assertEquals("887", test.getWallet().get(0).getCvv());
        assertEquals("CIBC", test.getWallet().get(1).getBankName());
        assertEquals("Sashank Shukla", test.getWallet().get(1).getOwnerName());
        assertEquals("654", test.getWallet().get(1).getCvv());
    }

    @Test
    public void testSwitchCurrentCard() {
        assertFalse(test.switchCurrentCard(1));
        test.addCreditCard("SCOTIABANK", "Sarthak Nath", "887");
        assertTrue(test.switchCurrentCard(1));
        test.addDebitCard("AMERICAN EXPRESS", "Akshit S", "335", 5000);
        assertTrue(test.switchCurrentCard(2));
    }
}