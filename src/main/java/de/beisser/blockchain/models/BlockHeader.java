package de.beisser.blockchain.models;

import de.beisser.blockchain.utility.SHA3Helper;

import java.io.Serializable;

/**
 * BlockHeader:
 * - Infos die zur Berechnung des BlockId-Hash benötigt werden
 * - hier sind alle Attribute zwingend notwendig
 */
public class BlockHeader implements Serializable {

    private int sourceCodeVersion;
    private int miningNonce = 0;
    private long miningStartingTimestamp;
    private byte[] previousBlockHash;
    private byte[] transactionListHash;

    public BlockHeader(long miningStartingTimestamp, byte[] previousBlockHash, byte[] transactionListHash) {
        this.miningStartingTimestamp = miningStartingTimestamp;
        this.previousBlockHash = previousBlockHash;
        this.transactionListHash = transactionListHash;
    }

public byte[] getBlockHash() {          // Hash des BlockHeaders ist die ID des dazugehörigen Blocks
        return SHA3Helper.hash256(this);
    }

    public void incrementMiningNonce() {
        if (this.miningNonce == Integer.MAX_VALUE) {
            throw new ArithmeticException("nonce to high");
        }

        this.miningNonce++;
    }

    public int getSourceCodeVersion() {
        return sourceCodeVersion;
    }

    public void setSourceCodeVersion(int sourceCodeVersion) {
        this.sourceCodeVersion = sourceCodeVersion;
    }

    public int getMiningNonce() {
        return miningNonce;
    }

    public void setMiningNonce(int miningNonce) {
        this.miningNonce = miningNonce;
    }

    public long getMiningStartingTimestamp() {
        return miningStartingTimestamp;
    }

    public void setMiningStartingTimestamp(long miningStartingTimestamp) {
        this.miningStartingTimestamp = miningStartingTimestamp;
    }

    public byte[] getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(byte[] previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public byte[] getTransactionListHash() {
        return transactionListHash;
    }

    public void setTransactionListHash(byte[] transactionListHash) {
        this.transactionListHash = transactionListHash;
    }

}
