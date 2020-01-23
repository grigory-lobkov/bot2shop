package com.bot2shop.processor;

import com.bot2shop.model.Session;

public class IncomeText {

    public void processMessage(Session session, String inText){
        session.conn.sendText(session.sessId, inText);
    }

}
