package com.example.yandextask;

public class CardItem {
    private int mIcon;
    private String mTicker;
    private String mFullName;
    private String mCurrentPrice;
    private String mDeltaPrice;

    public CardItem( String mTicker, String mFullName, String mCurrentPrice, String mDeltaPrice) {
//        this.mIcon = mIcon;
        this.mTicker = mTicker;
        this.mFullName = mFullName;
        this.mCurrentPrice = mCurrentPrice;
        this.mDeltaPrice = mDeltaPrice;
    }

    public CardItem(String mTicker) {
        this.mTicker = mTicker;
        this.mFullName = "aaa";
        this.mCurrentPrice = "aaa";
        this.mDeltaPrice = "aaa";
    }

    public int getmIcon() {
        return mIcon;
    }

    public String getmTicker() {
        return mTicker;
    }

    public String getmFullName() {
        return mFullName;
    }

    public String getmCurrentPrice() {
        return mCurrentPrice;
    }

    public String getmDeltaPrice() {
        return mDeltaPrice;
    }

    public void changeTicker(String ticker) {
        this.mTicker=ticker;
    }
}
