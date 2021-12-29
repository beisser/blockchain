package de.beisser.blockchain.services;

import de.beisser.blockchain.models.Block;
import de.beisser.blockchain.models.Chain;
import de.beisser.blockchain.models.Transaction;
import de.beisser.blockchain.utility.SHA3Helper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 1. Den Block- und den Transaktion-Cache unbedingt als ConcurrentHashMap, da Miner und Web-API nebenläufig darauf zugreifen werden
 *
 * 2. Difficulty entscheidet darüber wie viele Blöcke pro Sekunde erzeugt werden können (Muss je nach System angepasst werden)
 *
 * 3. Prüfung ob die Aufgabe gelöst worden ist
 */
public class Blockchain {

    public final static int MAX_BLOCK_SIZE_IN_BYTES = 1120;
    public final static int NETWORK_ID = 1;

    private Chain chain;
    private BigInteger difficulty;
    private Map<String, Block> blockCache;
    private Map<String, Transaction> transactionCache;

    public Blockchain() {
        this.chain = new Chain(NETWORK_ID);
        this.blockCache = new ConcurrentHashMap<>();       // 1.
        this.transactionCache = new ConcurrentHashMap<>();
        this.difficulty = new BigInteger(
                "16000");      // 2.
    }

    public void addBlock(Block block) {
        chain.add(block);
        blockCache.put(SHA3Helper.digestToHex(block.getBlockHash()), block);

        for (Transaction transaction : block.getTransactions()) {
            transactionCache.put(transaction.getTransactionIdAsString(), transaction);
        }
    }

    public boolean fulfillsDifficulty(byte[] digest) {      // 3.
        BigInteger temp = new BigInteger(digest);

        return temp.compareTo(difficulty) <= 0;
    }

    public BigInteger getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(BigInteger difficulty) {
        this.difficulty = difficulty;
    }

    public byte[] getPreviousHash() {
        return chain.getLast().getBlockHash();
    }

    public int size() {
        return chain.size();
    }

    public Transaction getTransactionByHash(String hex) {
        return transactionCache.get(hex);
    }

    public Block getBlockByHash(String hex) {
        return blockCache.get(hex);
    }

    public Block getBlockByHash(byte[] hash) {
        return blockCache.get(SHA3Helper.digestToHex(hash));
    }

    public Block getLatestBlock() {
        return chain.getLast();
    }

    public List<Block> getLatestBlocks(int size, int offset) {
        List<Block> blocks = new ArrayList<>();

        Block block = this.getLatestBlock();

        for (int i = 0; i < (size + offset); i++) {
            if (block != null) {
                if (i >= offset) {
                    blocks.add(block);
                }

                String previousHash = SHA3Helper.digestToHex(block.getBlockHeader().getPreviousBlockHash());
                block = this.getBlockByHash(previousHash);
            }
        }

        return blocks;
    }

    public Block getChildOfBlock(Block block) {
        return chain.get(chain.getChain().indexOf(block) + 1);
    }
}
