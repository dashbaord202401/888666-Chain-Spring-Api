package cn.org.gry.chainmaker.archive;

import cn.org.gry.chainmaker.archive.notice.BaseNotice;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.util.encoders.Hex;
import org.chainmaker.pb.common.ChainmakerBlock;
import org.chainmaker.pb.common.ChainmakerTransaction;
import org.chainmaker.pb.common.Request;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.pb.store.Store;
import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/8 16:11
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public abstract class BaseArchive {
    protected ChainClient chainClient;

    protected Long rpcCallTimeout;

    public abstract void setChainClient(ChainClient chainClient);

    // 风险提示!!!!,本方法是删除区块，使用前需用归档工具将对应区块归档!!!
    // 数据归档
    public ResultOuterClass.TxResponse Archive(Long targetBlockHeight) throws ChainMakerCryptoSuiteException {
        Request.Payload payload = chainClient.createArchiveBlockPayload(targetBlockHeight);
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            responseInfo = chainClient.sendArchiveBlockRequest(payload, rpcCallTimeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (responseInfo != null) {
            System.out.println("归档结果：");
            System.out.println(responseInfo);
        }
        return responseInfo;
    }

    //归档恢复
    public void Restore(Long targetBlockHeight) {
        ResultOuterClass.TxResponse responseInfo = null;
        try {
            Store.BlockWithRWSet fullBlock = chainClient.getArchivedFullBlockByHeight(targetBlockHeight);
            Request.Payload payload = chainClient.createRestoreBlockPayload(fullBlock.toByteArray());
            responseInfo = chainClient.sendRestoreBlockRequest(payload, rpcCallTimeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (responseInfo != null) {
            System.out.println("归档恢复结果：");
            System.out.println(responseInfo);
        }
    }

    //归档查询
    public ChainmakerTransaction.TransactionInfo GetFromArchiveStore(Long targetBlockHeight) {
        ChainmakerBlock.BlockInfo blockInfo = null;
        ChainmakerTransaction.TransactionInfo transactionInfo = null;

        try {
            blockInfo = chainClient.getArchivedBlockByHash(Hex.toHexString(blockInfo.getBlock().getHeader().getBlockHash().toByteArray()),
                    false, rpcCallTimeout);
            String txid = blockInfo.getBlock().getTxs(0).getPayload().getTxId();
            transactionInfo = chainClient.getArchivedTxByTxId(txid, rpcCallTimeout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (transactionInfo != null) {
            System.out.println("归档查询结果：");
            System.out.println(transactionInfo);
        }

        return transactionInfo;
    }


    public void getChainGenesisHash(Long targetBlockHeight) {
        ChainmakerBlock.BlockInfo block = null;
        try {
            block = chainClient.getBlockByHeight(targetBlockHeight, false, rpcCallTimeout);
            String genesisHash = Hex.toHexString(block.getBlock().getHeader().getBlockHash().toByteArray());
            System.out.println(genesisHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void archiveStore(Long targetArchiveHeight) {
        try {
            chainClient.archiveBlocks(targetArchiveHeight, new BaseNotice(), rpcCallTimeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void archiveReStore(Long restoreHeight) {
        try {
            chainClient.restoreBlocks(restoreHeight, new BaseNotice(), rpcCallTimeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getArchiveStatus() {
        Store.ArchiveStatus status = null;
        try {
            status = chainClient.getArchiveStatus(rpcCallTimeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(status);
    }
}
