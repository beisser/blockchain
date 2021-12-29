package de.beisser.blockchain.services;

import de.beisser.blockchain.threads.Miner;

public class DependencyManager {

    private static PendingTransactionsService pendingTransactionsService;

    public static PendingTransactionsService getPendingTransactionsService() {
        if (pendingTransactionsService == null) {
            pendingTransactionsService = new PendingTransactionsService();
        }

        return pendingTransactionsService;
    }

    public static void injectPendingTransactions(PendingTransactionsService pendingTransactions) {
        DependencyManager.pendingTransactionsService = pendingTransactions;
    }

    private static Blockchain blockchain;

    public static Blockchain getBlockchain() {
        if (blockchain == null) {
            blockchain = new Blockchain();
        }

        return blockchain;
    }

    public static void injectBlockchain(Blockchain blockchain) {
        DependencyManager.blockchain = blockchain;
    }

    private static Miner miner;

    public static Miner getMiner() {
        if (miner == null) {
            miner = new Miner();
        }

        return miner;
    }
}