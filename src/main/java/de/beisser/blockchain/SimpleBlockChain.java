package de.beisser.blockchain;

import de.beisser.blockchain.utility.SHA3Helper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


public class SimpleBlockChain
{
    public static void main(String[] args)
    {
        final Blockchain localBlockchain = new Blockchain();
        localBlockchain.printBlocks();

        final Transaction transaction1 = new Transaction(50, "Alice", "Bob");
        final Transaction transaction2 = new Transaction(25, "Bob", "Cole");
        final Transaction transaction3 = new Transaction(35, "Alice", "Cole");

        System.out.println("========================================");

        localBlockchain.addBlock(transaction1, transaction2, transaction3);
        localBlockchain.printBlocks();

        System.out.println("========================================");

        final Transaction fakeTransaction = new Transaction(777, "Bob", "Alice");
        final Block block1 = localBlockchain.getChain().get(1);
        block1.setTransactions(singletonList(fakeTransaction));

        System.out.println(localBlockchain.validateChain());
    }

    static class Blockchain {
        private final List<Block> chain = new ArrayList<>();
        private List<String> all_transactions = new ArrayList<>();

        public Blockchain()
        {
            genesisBlock();
        }

        public void addBlock(Transaction... transaction) {
            final Block previousBlock = chain.get(chain.size() - 1);
            final Block newBlock = new Block(previousBlock.getHash(), 0, asList(transaction));

            final String proof = proofOfWork(newBlock, 2);
            System.out.println(proof);

            chain.add(newBlock);
        }

        public boolean validateChain() {
            for(int i = 1; i < chain.size(); i++) {
                Block current = chain.get(i);
                Block previous = chain.get(i - 1);

                if(!current.getHash().equals(current.generateHash())) {
                    return false;
                }

                if(!current.getPreviousHash().equals(previous.getHash())) {
                    return false;
                }
            }

            return true;
        }

        private String proofOfWork(Block block, int difficulty) {
            final List<Transaction> transactions = block.getTransactions();
            int nonce = block.getNonce();

            String proof = block.generateHash();

            while(!proof.startsWith(StringUtils.repeat("0", difficulty))) {
                block.setNonce(nonce += 1);
                proof = block.generateHash();
            }

            block.setNonce(0);
            return proof;
        }

        public void printBlocks() {
            for (int i = 0; i < chain.size(); i++)
            {
                final Block currentBlock = chain.get(i);
                System.out.println(String.format("%s: %s", i, currentBlock));
                currentBlock.printBlock();
            }
        }

        private void genesisBlock() {
            final Block genesis = new Block("0", 0, emptyList());
            chain.add(genesis);
        }

        public List<Block> getChain()
        {
            return chain;
        }
    }

    static class Block {
        private Date timestamp;
        private String hash;
        private String previousHash;
        private int nonce;
        private List<Transaction> transactions;

        public Block(String previousHash, int nonce, List<Transaction> transactions)
        {
            this.timestamp = new Date();
            this.transactions = transactions;
            this.previousHash = previousHash;
            this.nonce = nonce;
            this.hash = generateHash();
        }

        public void printBlock() {
            System.out.println("timestamp:" + timestamp);
            System.out.println("transactions:" + transactions);
            System.out.println("current hash: " + hash);
            System.out.println("previous hash: " + previousHash);
        }

        private String generateHash()
        {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(timestamp);
            stringBuilder.append(transactions);
            stringBuilder.append(previousHash);
            stringBuilder.append(nonce);
            String blockContents = stringBuilder.toString();
            return SHA3Helper.hash256AsHexString(blockContents);
        }

        public String getHash()
        {
            return hash;
        }

        public String getPreviousHash()
        {
            return previousHash;
        }

        public List<Transaction> getTransactions()
        {
            return transactions;
        }

        public void setTransactions(List<Transaction> transactions)
        {
            this.transactions = transactions;
        }

        public int getNonce()
        {
            return nonce;
        }

        public void setNonce(int nonce)
        {
            this.nonce = nonce;
        }
    }

    static class Transaction {
        private int amount;
        private String sender;
        private String receiver;

        public Transaction(int amount, String sender, String receiver)
        {
            this.amount = amount;
            this.sender = sender;
            this.receiver = receiver;
        }

        public int getAmount()
        {
            return amount;
        }

        public void setAmount(int amount)
        {
            this.amount = amount;
        }

        public String getSender()
        {
            return sender;
        }

        public void setSender(String sender)
        {
            this.sender = sender;
        }

        public String getReceiver()
        {
            return receiver;
        }

        public void setReceiver(String receiver)
        {
            this.receiver = receiver;
        }
    }
}