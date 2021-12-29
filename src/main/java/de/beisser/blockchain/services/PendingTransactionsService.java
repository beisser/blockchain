package de.beisser.blockchain.services;

import de.beisser.blockchain.models.Block;
import de.beisser.blockchain.models.Transaction;
import de.beisser.blockchain.utility.SizeHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Verwaltet alle ausstehenden Transaktionen die noch nicht in einen Block aufgenommen wurden
 * Miner bevorzugen die Transaktionen mit dem höchsten Basispreis
 * <p>
 * 1. Ausstehende Transaktionen werden nach Basispreis in der Queue gehalten (je höher der Basispreis desto weiter vorne ist die Transaktion)
 * <p>
 * 2. viele Miner sind gleichzeitig auf der Suche nach dem richtigen Nonce (um den nächsten Block zu erstellen)
 * - dabei kann folgendes Szenario passieren
 * - Miner A holt sich mehrere Transaktionen aus der Queue und versucht den Nonce zu suchen
 * - gleichzeitg macht auch Miner B dies
 * - wenn nun Miner B schneller fertig ist, als Miner A wird der Block von Miner B in die de.beisser.blockchain.services.Blockchain aufgenommen
 * - Miner A erhält während der Suche nach dem Nonce den neuen Block. Dieser neue Block kann dann natürlich Transaktionen enthalten
 * die auch Miner A in seinen Block packen wollte. Daher muss er die Transaktionen aus dem neuen Block von Miner B aus seinem zu erstellenden Block rausnehmen
 * <p>
 * 3. Transaktionen für neuen zu erstellenden Block sammeln
 * <p>
 * 4. WICHTIG: Transaktionen dürfen nicht aus den pendingTransactions entfernt werden, da ja auch andere Miner auf ausstehende Transaktionen zugreifen müssen
 * - daher wird hier die PriorityQueue geklont
 */
public class PendingTransactionsService {

    private PriorityQueue<Transaction> pendingTransactions;

    public PendingTransactionsService() {
        Comparator<Transaction> comparator = new TransactionComparatorByFee();      // 1.
        pendingTransactions = new PriorityQueue<>(11, comparator);
    }

    public PendingTransactionsService(Comparator<Transaction> comparator) {
        pendingTransactions = new PriorityQueue<>(11, comparator);
    }

    public void addPendingTransaction(Transaction transaction) {
        pendingTransactions.add(transaction);
    }

    public void clearPendingTransactions(Block block) {
        clearPendingTransactions(block.getTransactions());
    }

    public void clearPendingTransactions(List<Transaction> transactions) {      // 2.
        for (Transaction transaction : transactions) {
            pendingTransactions.remove(transaction);
        }
    }

    public List<Transaction> getTransactionsForNextBlock() {        // 3.
        List<Transaction> nextTransactions = new ArrayList<>();

        int transactionCapacity = SizeHelper.calculateTransactionCapacity();

        PriorityQueue<Transaction> temp = new PriorityQueue<>(pendingTransactions);     // 4.
        while (transactionCapacity > 0 && !temp.isEmpty()) {
            nextTransactions.add(temp.poll());
            transactionCapacity--;
        }

        return nextTransactions;
    }

    public boolean pendingTransactionsAvailable() {
        return !pendingTransactions.isEmpty();
    }
}
