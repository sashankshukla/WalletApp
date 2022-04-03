package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {

    // Test taken from JSONWriterTest class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Test
    void testWriterInvalidFile() {
        try {
            Wallet wallet = new Wallet();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWallet() {
        try {
            Wallet wallet = new Wallet();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWallet.json");
            writer.open();
            writer.write(wallet);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWallet.json");
            wallet = reader.read();
            assertEquals(0, wallet.getWalletSize());
            assertNull(wallet.getCurrentCard());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Wallet wallet = new Wallet();
            wallet.addCreditCard("CIBC", "Sashank Shukla", "167");
            wallet.addDebitCard("TD BANK", "Nakul Girish", "345", 500);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWallet.json");
            writer.open();
            writer.write(wallet);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWallet.json");
            wallet = reader.read();
            List<Card> cards = wallet.getWallet();
            assertEquals(2, wallet.getWalletSize());
            checkCard("Sashank Shukla", "CIBC", "167", 5000, "CREDIT CARD",
                    cards.get(0));
            checkCard("Nakul Girish", "TD BANK", "345", 500, "DEBIT CARD",
                    cards.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}