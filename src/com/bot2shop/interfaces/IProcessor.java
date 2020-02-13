package com.bot2shop.interfaces;

/*
 *   Process something, inside connection snd session
 */


public interface IProcessor<Fr> {

    void process(int connId, String sessionId, Fr parameter);

}