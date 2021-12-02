package de.beisser.blockchain.models;

public class BlockHeader {

    private int version;
    private int nonce = 0;
    private long timestamp;
    private byte[] previousHash;
    private byte[] transactionListHash;

    public BlockHeader(long timestamp, byte[] previousHash, byte[] transactionListHash) {
        this.timestamp = timestamp;
        this.previousHash = previousHash;
        this.transactionListHash = transactionListHash;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(byte[] previousHash) {
        this.previousHash = previousHash;
    }

    public byte[] getTransactionListHash() {
        return transactionListHash;
    }

    public void setTransactionListHash(byte[] transactionListHash) {
        this.transactionListHash = transactionListHash;
    }
}
