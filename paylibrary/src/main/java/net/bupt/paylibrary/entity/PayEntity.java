package net.bupt.paylibrary.entity;

import java.io.Serializable;

/**
 * 实体类
 */
public class PayEntity implements Serializable {
    private String cardno;
    private String prodcode;
    private String description;
    private String total;

    public PayEntity(String cardno, String prodcode, String description, String total) {
        this.cardno = cardno;
        this.prodcode = prodcode;
        this.description = description;
        this.total = total;
    }

    public String getCardno() {
        return cardno;
    }

    public String getProdcode() {
        return prodcode;
    }

    public String getDescription() {
        return description;
    }

    public String getTotal() {
        return total;
    }
}
