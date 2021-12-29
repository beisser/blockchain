package de.beisser.blockchain.models;

import de.beisser.blockchain.utility.SHA3Helper;

import java.io.Serializable;


/**
 * 1. byte-Arrays:
 * - abspeichern so viel wie möglich als byte-Array um größe zu minimieren
 * - Strings brauchen oft für Encoding zusätzliche Bytes
 * <p>
 * 2. nonce
 * - dient als Transaktionszähler für konkreten Absender
 * <p>
 * 3. Serialisierung: Konvertieren von Object in einen Byte-Stream
 * - TRANSIENT: werden nicht serialisiert. Dadurch dass diese Werte dann nicht in der serialisierten Form des Objects
 * - vorhanden sind heißt das in unseren Fall:
 * - DIE WERTE WERDEN NICHT MIT IN DIE HASH-BERECHNUNG AUFGENOMMEN
 * <p>
 * 4. später hinzugefügte Werte
 * - folgende Werte sind zum Zeitpunkt des Erstellens der Transaktion nicht bekannt
 * - sie werden dann im Zuge des Minings hinzugefügt
 * <p>
 * 5. Tatsächlicher Preis
 * - tatsächlicher Preis wird berechnet aus Basispreis multipliziert mit verbrauchten Einheiten
 * - überschreibtet der erreichnete Preis das angegebene Limit wird die Transaktion abgebrochen
 * <p>
 * 6. Berechnung der Transaktion ID
 * - WICHTIG: ALLE ATTRIBUTE DIE NICHT IN DIE ID-BERECHNUNG MIT EINFLIEßEN SOLLEN MIT "TRANSIENT" FLAGGEN
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

    public String getTransactionIdAsString() {
        return SHA3Helper.hash256AsHex(this);
    }

    public void setTransactionId(byte[] transactionId) {
        this.transactionId = transactionId;
    }

    public byte[] getSender() {
        return sender;
    }

    public void setSender(byte[] sender) {
        this.sender = sender;
    }

    public byte[] getReceiver() {
        return receiver;
    }

    public void setReceiver(byte[] receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public double getTransactionFeeBasePrice() {
        return transactionFeeBasePrice;
    }

    public void setTransactionFeeBasePrice(double transactionFeeBasePrice) {
        this.transactionFeeBasePrice = transactionFeeBasePrice;
    }

    public double getTransactionFeeLimit() {
        return transactionFeeLimit;
    }

    public void setTransactionFeeLimit(double transactionFeeLimit) {
        this.transactionFeeLimit = transactionFeeLimit;
    }

    public long getReceivedTimeStamp() {
        return receivedTimeStamp;
    }

    public void setReceivedTimeStamp(long receivedTimeStamp) {
        this.receivedTimeStamp = receivedTimeStamp;
    }

    public byte[] getBlockId() {
        return blockId;
    }

    public void setBlockId(byte[] blockId) {
        this.blockId = blockId;
    }

    public double getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(double transactionFee) {
        this.transactionFee = transactionFee;
    }

    public int getSizeInByte() {
        return sizeInByte;
    }

    public void setSizeInByte(int sizeInByte) {
        this.sizeInByte = sizeInByte;
    }
}
