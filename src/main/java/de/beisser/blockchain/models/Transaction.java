package de.beisser.blockchain.models;

import de.beisser.blockchain.utility.SHA3Helper;

import java.io.Serializable;

public class Transaction implements Serializable {

    private transient byte[] transactionId;
    private byte[] sender;
    private byte[] receiver;
    private double amount;
    private int nonce;
    private double transactionFeeBasePrice;
    private double transactionFeeLimit;

    // transient: werden nicht mit in die Hash-Berechnung aufgenommen
    // da zum Zeitpunkt des Erstellens der Transaktion nicht bekannt
    // wir dann im Zuge des Minings hinzugefügt
    private transient long receivedTimeStamp;
    private transient byte[] blockId;
    private transient double transactionFee;
    private transient int sizeInByte;

    public Transaction(byte[] sender, byte[] receiver, double amount, int nonce, double transactionFeeBasePrice, double transactionFeeLimit) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.nonce = nonce;
        this.transactionFeeBasePrice = transactionFeeBasePrice;
        this.transactionFeeLimit = transactionFeeLimit;

        this.transactionId = SHA3Helper.hash256(this);
    }
}
