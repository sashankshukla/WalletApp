package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreditCardTest{
    CreditCard test;
    CreditCard testJson;
    @BeforeEach
    public void runBefore(){
        test = new CreditCard("CIBC", "Sashank Shukla" , "119");

    }

    @Test
    public void testCreditCard(){
        assertEquals("CIBC" , test.getBankName());
        assertEquals("Sashank Shukla" , test.getOwnerName());
        assertEquals("119" , test.getCvv());
        assertEquals(5000 , test.getCreditLimit());
        assertEquals("CREDIT CARD" , test.getCardType());
        assertEquals(0 , test.getTransactions().size());
        assertEquals( 5000 ,test.getCardStatus());
    }

    @Test
    public void testCreditCardConstructor(){
        Transaction t = new Transaction("popeyes" , 330.98);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(t);
        testJson = new CreditCard("CIBC" , "Sashank Shukla" , "119" ,
                345 , transactions);
        assertEquals("CIBC" , testJson.getBankName());
        assertEquals("Sashank Shukla" , testJson.getOwnerName());
        assertEquals("119" , testJson.getCvv());
        assertEquals(345 , testJson.getCreditLimit());
        assertEquals("CREDIT CARD" , testJson.getCardType());
        assertEquals(1 , testJson.getTransactions().size());
        assertEquals( 345 ,testJson.getCardStatus());
        assertEquals(transactions , testJson.getTransactions());
    }

    @Test
    public void testPay(){
        test.pay(100 , "Walmart");
        assertEquals(4900 , test.getCreditLimit());
        assertEquals(1 , test.getTransactions().size());
        assertEquals("Walmart" , test.getTransactions().get(0).getTransactionName());
        assertEquals(100 , test.getTransactions().get(0).getAmount());
        assertEquals( 4900 ,test.getCardStatus());
    }


}
