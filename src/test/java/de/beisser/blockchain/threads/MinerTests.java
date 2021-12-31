package de.beisser.blockchain.threads;

import de.beisser.blockchain.models.Block;
import de.beisser.blockchain.models.Transaction;
import de.beisser.blockchain.services.DependencyManager;
import de.beisser.blockchain.services.PendingTransactionsService;
import de.beisser.blockchain.utility.SHA3Helper;
import org.junit.Before;
import org.junit.Test;

public class MinerTests implements MinerListener {
    @Before
    public void setUp() {
        mockTransactions();
    }

    @Test
    public void testMiner() {
        Miner miner = new Miner();
        miner.registerListener(this);

        Thread minerThread = new Thread(miner);
        minerThread.start();

        while (DependencyManager.getPendingTransactionsService().pendingTransactionsAvailable()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        miner.stopMining();

        System.out.println("Länge der Blockchain: " + DependencyManager.getBlockchain().size());
    }

    @Override
    public void notifyNewBlock(Block block) {
        System.out.println("new block mined");

        System.out.println(SHA3Helper.digestToHex(block.getBlockHash()));
    }

    private void mockTransactions() {
        PendingTransactionsService transactions = DependencyManager.getPendingTransactionsService();

        for (int i = 0; i < 100; i++) {
            String sender = "testSender" + i;
            String receiver = "testReceiver" + i;
            double amount = i * 1.1;
            int nonce = i;
            double transactionFee = 0.0000001 * i;
            double transactionFeeLimit = 10.0;

            transactions.addPendingTransaction(
                    new Transaction(sender.getBytes(),
                            receiver.getBytes(),
                            amount,
                            nonce,
                            transactionFee,
                            transactionFeeLimit));
        }
    }
}
