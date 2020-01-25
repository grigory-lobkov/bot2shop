package com.bot2shop.interfaces;

public interface IProcessor<Fr> {

    void process(int connId, String sessionId, Fr parameter);

}
