package cn.org.gry.chainmaker.contract;

import cn.org.gry.chainmaker.utils.Result;
import org.bouncycastle.util.encoders.Hex;
import org.chainmaker.pb.common.ChainmakerBlock;
import org.chainmaker.pb.common.ChainmakerTransaction;
import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.ChainClientException;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class SystemContract {
    @Autowired
    private ChainClient chainClient;

    private final Long rpcCallTimeOut = 10000L;

    public Result getBlockByHeight(Long height, Boolean withRWSet) {
        ChainmakerBlock.BlockInfo blockInfo = null;
        try {
            blockInfo = chainClient.getBlockByHeight(height, withRWSet, rpcCallTimeOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(blockInfo);

        Map<String, String> data = new HashMap<>();
        data.put("blockHeight", String.valueOf(blockInfo.getBlock().getHeader().getBlockHeight()));
        data.put("blockHash", Hex.toHexString(blockInfo.getBlock().getHeader().getBlockHash().toByteArray()));
        data.put("previousBlockHash", Hex.toHexString(blockInfo.getBlock().getHeader().getPreBlockHash().toByteArray()));
        // 根据时间戳格式化为年月日
        long timestamp = blockInfo.getBlock().getHeader().getBlockTimestamp();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        data.put("blockTime", dateFormat.format(new Date(timestamp*=1000)));
        Result result = Result.success("区块信息", blockInfo.getBlock().getTxs(0).getPayload().getTxId(), data);

        return result;
    }

    public Result getTxByTxId(String txId) {
        ChainmakerTransaction.TransactionInfo txInfo = null;
        try {
            txInfo = chainClient.getTxByTxId(txId, rpcCallTimeOut);
        } catch (ChainMakerCryptoSuiteException | ChainClientException e) {
            e.printStackTrace();
        }
        System.out.println(txInfo);

        Result result = null;
        if (txInfo != null) {
            Map<String, Object> data = new HashMap<>();
            // TODO 填信息
            result = Result.success("交易信息", txId, data);
        } else {
            result = Result.fail("交易信息", txId, null);
        }

        return result;
    }
}
