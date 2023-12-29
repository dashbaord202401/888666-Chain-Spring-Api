package cn.org.gry.chainmaker.subscribe;

import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.util.encoders.Hex;
import org.chainmaker.pb.common.ChainmakerBlock;
import org.chainmaker.pb.common.ChainmakerTransaction;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.ChainClient;

/**
 * @author yejinhua  Email:yejinhua@gzis.ac.cn
 * @version 1.0
 * @description
 * @since 2023/12/8 10:38
 * Copyright (C) 2022-2023 CASEEDER, All Rights Reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public abstract class BaseSubscribe implements Runnable {
    private Boolean isSubscribeBlock = false;
    private Boolean isSubscribeTx = false;
    private Boolean isEnd = false;
    private Long sleepTime = 1000 * 10L;
    protected ChainClient chainClient;

    public abstract void setChainClient(ChainClient chainClient);

    public void run() {
        if (isSubscribeBlock) {
            SubscribeBlock();
        }
        if (isSubscribeTx) {
            SubscribeTx();
        }
        while (!isEnd) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void SubscribeBlock() {
        StreamObserver<ResultOuterClass.SubscribeResult> responseObserverBlock = buildObserverBlock();
        try {
            chainClient.subscribeBlock(0, -1, true, false, responseObserverBlock);
            System.out.println("开始订阅区块");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void SubscribeTx() {
        StreamObserver<ResultOuterClass.SubscribeResult> responseObserverTx = buildObserverTx();
        try {
            chainClient.subscribeTx(0, -1, "", new String[]{}, responseObserverTx);
            System.out.println("开始订阅交易");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StreamObserver<ResultOuterClass.SubscribeResult> buildObserverTx() {
        return new StreamObserver<ResultOuterClass.SubscribeResult>() {
            @Override
            public void onNext(ResultOuterClass.SubscribeResult result) {
                try {
                    ChainmakerTransaction.Transaction transactionInfo = ChainmakerTransaction.Transaction.parseFrom(result.getData());
                    System.out.print("订阅到： txId:" + transactionInfo.getPayload().getTxId());
                    System.out.print(", code:" + transactionInfo.getResult().getCode().getNumber());
                    if (transactionInfo.getResult().getCode().getNumber() == ResultOuterClass.TxStatusCode.SUCCESS.getNumber()) {
                        System.out.println(", result :" + transactionInfo.getResult().getContractResult().getResult());
                    } else {
                        System.out.print(", message:" + transactionInfo.getResult().getMessage());
                        System.out.println(", contract message:" + transactionInfo.getResult().getContractResult().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable throwable) {
                // can add log here
            }
            @Override
            public void onCompleted() {
                // can add log here
            }
        };
    }

    public static StreamObserver<ResultOuterClass.SubscribeResult> buildObserverBlock() {
        return new StreamObserver<ResultOuterClass.SubscribeResult>() {
            @Override
            public void onNext(ResultOuterClass.SubscribeResult result) {
                try {
                    com.google.protobuf.ByteString d = result.getData();
                    ChainmakerBlock.BlockInfo blockInfo = ChainmakerBlock.BlockInfo.parseFrom(d);
                    System.out.println("订阅到： blockHeight: " + blockInfo.getBlock().getHeader().getBlockHeight() +
                            ", blockHash: " + Hex.toHexString(blockInfo.getBlock().getHeader().getBlockHash().toByteArray()) +
                            ", txCount: " + blockInfo.getBlock().getTxsCount() +
                            ", chainId: " + blockInfo.getBlock().getHeader().getChainId());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                // just do nothing
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                // just do nothing
            }
        };
    }
}
