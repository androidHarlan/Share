package com.example.lhc.share.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by a1 on 2017/6/29.
 */
public class CardBean implements IPickerViewData {
    int id;
    String cardNo;

    public CardBean(int id, String cardNo) {
        this.id = id;
        this.cardNo = cardNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPickerViewText() {
        return cardNo;
    }
}
