package de.beisser.blockchain.models;

import java.util.ArrayList;
import java.util.List;

public class Chain {

    private List<Block> chain = new ArrayList<>();
    private int networkId;

    public Chain(int networkId) {
        this.networkId = networkId;
        chain.add(new GenesisBlock());
    }

    public void add(Block block) {
        chain.add(block);
    }

    public Block get(int index) {
        return chain.get(index);
    }

    public Block getLast() {
        return chain.get(chain.size() - 1);
    }

    public int size() {
        return chain.size();
    }

    public List<Block> getChain() {
        return chain;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    private static class GenesisBlock extends Block
    {
        public static byte[] ZERO_HASH_IN = new byte[32];

        public GenesisBlock( )
        {
            super(ZERO_HASH_IN);
        }
    }
}
