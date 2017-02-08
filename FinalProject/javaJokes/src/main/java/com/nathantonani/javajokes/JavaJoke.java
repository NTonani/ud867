package com.nathantonani.javajokes;

/**
 * Created by ntonani on 1/30/17.
 */

public class JavaJoke {
    private static final String FREE_JOKE = "This is a funny joke";
    private static final String PAID_JOKE = "This is a hilarious joke!";

    private boolean mIsPaid;

    public JavaJoke(boolean isPaid){
        mIsPaid = isPaid;
    }

    /*
     * Return joke based on product flavor
     */

    public String tellJoke(){
        return mIsPaid ?
                PAID_JOKE :
                FREE_JOKE;
    }
}
