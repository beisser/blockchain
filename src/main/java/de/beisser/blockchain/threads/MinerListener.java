package de.beisser.blockchain.threads;

import de.beisser.blockchain.models.Block;

public interface MinerListener {
    void notifyNewBlock(Block block);
}
