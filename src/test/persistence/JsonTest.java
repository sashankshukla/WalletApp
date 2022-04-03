package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    protected void checkCard(String ownerName, String bankName, String cvv,
                             double cardStatus, String cardType, Card card) {
        assertEquals(ownerName, card.getOwnerName());
        assertEquals(bankName, card.getBankName());
        assertEquals(cvv, card.getCvv());
        assertEquals(cardStatus, card.getCardStatus());
        assertEquals(cardType, card.getCardType());
    }
}