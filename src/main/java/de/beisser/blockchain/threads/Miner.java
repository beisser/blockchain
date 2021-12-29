package de.beisser.blockchain.threads;

import de.beisser.blockchain.models.Block;
import de.beisser.blockchain.models.Transaction;
import de.beisser.blockchain.services.Blockchain;
import de.beisser.blockchain.services.DependencyManager;
import de.beisser.blockchain.services.PendingTransactionsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Da mehrere Miner gleichzeitig in einem eigenen Thread laufen sollen implementieren wir hier das Runnable Interface
 * <p>
 * 1. Zunächst wird ein neuer Block erstellt:
 * - dazu wird der Hash des letzten akzeptierten Blocks in der Blockchain und ausstehende Transaktionen geladen
 */
public class Miner implements Runnable {

    private boolean mining = true;
    private List<MinerListener> listeners = new ArrayList<>();
    private boolean cancelBlock = false;
    private Block block;

    @Override
    public void run() {
        while (isMining()) {
            block = getNewBlockForMining();     // 1.

            while (!cancelBlock && doesNotFulfillDifficulty(block.getBlockHash())) {
                try {
                    block.incrementNonce();
                } catch (ArithmeticException e) {
                    restartMining();
                }
            }

            if (cancelBlock) {
                block = null;
                cancelBlock = false;
            } else {
                blockMined(block);
            }
        }
    }

    private Block getNewBlockForMining() {
        PendingTransactionsService pendingTransactions = DependencyManager.getPendingTransactionsService();
        Blockchain blockchain = DependencyManager.getBlockchain();
        List<Transaction> transactions = pendingTransactions.getTransactionsForNextBlock();

        return new Block(transactions, blockchain.getPreviousHash());
    }

    private void restartMining() {
        PendingTransactionsService pendingTransactions = DependencyManager.getPendingTransactionsService();
        List<Transaction> transactions = pendingTransactions.getTransactionsForNextBlock();

        block.setTransactions(transactions);
    }

    private void blockMined(Block block) {
        if (block.getTransactions().size() > 0) {
            for (Transaction transaction : block.getTransactions()) {
                transaction.setBlockId(block.getBlockHash());
            }
        }

        DependencyManager.getBlockchain().addBlock(block);
        DependencyManager.getPendingTransactionsService().clearPendingTransactions(block);

        for (MinerListener listener : listeners) {
            listener.notifyNewBlock(block);
        }
    }

    private boolean doesNotFulfillDifficulty(byte[] digest) {
        Blockchain blockchain = DependencyManager.getBlockchain();
        return !blockchain.fulfillsDifficulty(digest);
    }

    public void setCancelBlock(boolean cancelBlock) {
        this.cancelBlock = cancelBlock;
    }

    public void cancelBlock() {
        this.cancelBlock = true;
    }

    public void stopMining() {
        this.mining = false;
    }

    public boolean isMining() {
        return mining;
    }

    public void registerListener(MinerListener listener) {
        listeners.add(listener);
    }
}

