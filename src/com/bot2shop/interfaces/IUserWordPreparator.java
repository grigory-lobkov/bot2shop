package com.bot2shop.interfaces;

/*
 *   Interface for Prepare user words for a further work
 *   Implementations package: com.bot2shop.prepare
 */

public interface IUserWordPreparator<From, To> {

    To prepare(From word);

}
