package de.beisser.blockchain.models;

import de.beisser.blockchain.utility.SHA3Helper;
import de.beisser.blockchain.utility.SizeHelper;
import org.bouncycastle.util.Arrays;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private int magicNumber = 0xD9B4BEF9;
    private int blockSizeInByte;
    private int transactionCount;
    private List<Transaction> transactions;
    private BlockHeader blockHeader;

    public Block() {
    }

    public Block(byte[] previousHash) {
        this.transactionCount = 0;
        this.transactions = new ArrayList<>();
        this.blockSizeInByte = SizeHelper.calculateBlockSize(this);

        this.blockHeader = new BlockHeader(System.currentTimeMillis(), previousHash, getTransactionHash());
    }

    public Block(List<Transaction> transactions, byte[] previousHash) {
        this.transactions = transactions;
        this.transactionCount = transactions.size();
        this.blockSizeInByte = SizeHelper.calculateBlockSize(this);
        this.blockHeader = new BlockHeader(System.currentTimeMillis(), previousHash, getTransactionHash());
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        this.transactionCount++;

        this.blockHeader.setTransactionListHash(getTransactionHash());
        this.blockSizeInByte = SizeHelper.calculateBlockSize(this);
    }

    public byte[] getBlockHash() {
        return blockHeader.getBlockHash();
    }

    public int getMiningNonce() {
        return this.blockHeader.getMiningNonce();
    }

    public void setMiningNonce(int nonce) {
        this.blockHeader.setMiningNonce(nonce);
    }

    public void incrementNonce() throws ArithmeticException {
        this.blockHeader.incrementMiningNonce();
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(int magicNumber) {
        this.magicNumber = magicNumber;
    }

    public int getBlockSizeInByte() {
        return blockSizeInByte;
    }

    public void setBlockSizeInByte(int blockSizeInByte) {
        this.blockSizeInByte = blockSizeInByte;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public BlockHeader getBlockHeader() {
        return blockHeader;
    }

    public void setBlockHeader(BlockHeader blockHeader) {
        this.blockHeader = blockHeader;
    }

    private byte[] getTransactionHash() {
        byte[] transactionsInBytes = new byte[0];

        for (Transaction transaction : transactions) {
            transactionsInBytes = Arrays.concatenate(transactionsInBytes, transaction.getTransactionId());
        }

        return SHA3Helper.hash256(transactionsInBytes);
    }
}
