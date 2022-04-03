package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebitCardTest {
    DebitCard test;
    DebitCard testJson;

    @BeforeEach
    public void runBefore(){
        test = new DebitCard("CHASE", "Nakul Girish" , "791" , 7000);
    }

    @Test
    public void testDebitCard(){
        assertEquals("CHASE" , test.getBankName());
        assertEquals("Nakul Girish" , test.getOwnerName());
        assertEquals("791" , test.getCvv());
        assertEquals(7000, test.getBalance());
        assertEquals("DEBIT CARD" , test.getCardType());
        assertEquals(0 , test.getTransactions().size());
        assertEquals( 7000 , test.getCardStatus());
    }

    @Test
    public void testDebitCardConstructor(){
        Transaction t = new Transaction("popeyes" , 330.98);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(t);
        testJson = new DebitCard("CIBC" , "Sashank Shukla" , "119" ,
                345 , transactions);
        assertEquals("CIBC" , testJson.getBankName());
        assertEquals("Sashank Shukla" , testJson.getOwnerName());
        assertEquals("119" , testJson.getCvv());
        assertEquals(345 , testJson.getBalance());
        assertEquals("DEBIT CARD" , testJson.getCardType());
        assertEquals(1 , testJson.getTransactions().size());
        assertEquals( 345 ,testJson.getCardStatus());
        assertEquals(transactions , testJson.getTransactions());
    }

    @Test
    public void testPay(){
        test.pay(1500 , "Best Buy");
        assertEquals(5500 , test.getBalance());
        assertEquals(1 , test.getTransactions().size());
        assertEquals("Best Buy" , test.getTransactions().get(0).getTransactionName());
        assertEquals(1500 , test.getTransactions().get(0).getAmount());
        assertEquals( 5500 , test.getCardStatus());
    }
}
