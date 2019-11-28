package com.bot2shop.prepare;

import com.bot2shop.interfaces.IUserWordPreparator;

public class LowCase implements IUserWordPreparator {

    @Override
    public String prepare(Object word) {
        if (word.getClass() != String.class) {
            System.out.println("LowCase. Not supported class got, expected String"); // TODO: raise error
            return null;
        }
        String str = (String) word;
        return str.toLowerCase();
    }

}
