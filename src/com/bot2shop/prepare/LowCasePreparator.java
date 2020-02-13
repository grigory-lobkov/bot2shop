package com.bot2shop.prepare;

import com.bot2shop.interfaces.IPreparator;

import static com.bot2shop.model.Config.KEYWORD_MULTIPLIER_COST;

/*
 *   Simplest user words preparator. Just lower case
 *   Invoke example: IPreparator<String> preparator = new LowCasePreparator();
 */


public class LowCasePreparator implements IPreparator {

    // Lower case key word
    @Override
    public String prepareKey(String keyWord) {
        return keyWord.toLowerCase().trim();
    }

    // Determine keyword base weight
    @Override
    public float getBaseKeywordWeight(Object keyWord) {
        return (float) Math.pow(KEYWORD_MULTIPLIER_COST, ((String) keyWord).length());
    }

    // Split user input to words and lower case it
    @Override
    public String[] prepareInput(String string) {
        String[] result = string.split("[, ?.@]+");
        for (int i = result.length - 1; i >= 0; i--) {
            result[i] = result[i].toLowerCase();
        }
        return result;
    }

}