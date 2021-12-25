package de.beisser.blockchain.models;

import de.beisser.blockchain.utility.SHA3Helper;

import java.io.Serializable;


/**
 * 1. byte-Arrays:
 *     - abspeichern so viel wie möglich als byte-Array um größe zu minimieren
 *     - Strings brauchen oft für Encoding zusätzliche Bytes
 *
 * 2. nonce
 *     - dient als Transaktionszähler für konkreten Absender
 *
 * 3. Serialisierung: Konvertieren von Object in einen Byte-Stream
 *      - TRANSIENT: werden nicht serialisiert. Dadurch dass diese Werte dann nicht in der serialisierten Form des Objects
 *      - vorhanden sind heißt das in unseren Fall:
 *      - DIE WERTE WERDEN NICHT MIT IN DIE HASH-BERECHNUNG AUFGENOMMEN
 *
 * 4. später hinzugefügte Werte
 *      - folgende Werte sind zum Zeitpunkt des Erstellens der Transaktion nicht bekannt
 *      - sie werden dann im Zuge des Minings hinzugefügt
 *
 * 5. Tatsächlicher Preis
 *      - tatsächlicher Preis wird berechnet aus Basispreis multipliziert mit verbrauchten Einheiten
 *      - überschreibtet der erreichnete Preis das angegebene Limit wird die Transaktion abgebrochen
 *
 * 6. Berechnung der Transaktion ID
 *      - WICHTIG: ALLE ATTRIBUTE DIE NICHT IN DIE ID-BERECHNUNG MIT EINFLIEßEN SOLLEN MIT "TRANSIENT" FLAGGEN
 */
public class Transaction implements Serializable {

    private transient byte[] transactionId;             // 1.
    private byte[] sender;
    private byte[] receiver;
    private double amount;
    private int nonce;                                 // 2.
    private double transactionFeeBasePrice;
    private double transactionFeeLimit;

    private transient long receivedTimeStamp;          // 3. + 4.
    private transient byte[] blockId;
    private transient double transactionFee;           // 5.
    private transient int sizeInByte;

    public Transaction(byte[] sender, byte[] receiver, double amount, int nonce, double transactionFeeBasePrice, double transactionFeeLimit) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.nonce = nonce;
        this.transactionFeeBasePrice = transactionFeeBasePrice;
        this.transactionFeeLimit = transactionFeeLimit;

        this.transactionId = SHA3Helper.hash256(this);      // 6.
    }

    public byte[] getTransactionId() {
        return transactionId;
    }
}
