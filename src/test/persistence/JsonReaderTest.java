package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    // Test taken from JSONReaderTest class in
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Wallet wallet = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWallet() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyWallet.json");
        try {
            Wallet wallet = reader.read();
            assertEquals(0, wallet.getWalletSize());
            assertNull(wallet.getCurrentCard());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWallet() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralWallet.json");
        try {
            Wallet wallet = reader.read();
            List<Card> cards = wallet.getWallet();
            assertEquals(2, wallet.getWalletSize());
            checkCard("Sashank Shukla", "CIBC", "167", 4702.79,
                    "CREDIT CARD", cards.get(0));
            checkCard("Nakul Girish", "TD BANK", "345", 8987,
                    "DEBIT CARD", cards.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}