package com.brano.oop2.models.deals;

import com.brano.oop2.models.users.Buyer;
import com.brano.oop2.models.users.Dealmaker;
import com.brano.oop2.models.users.TeamManager;

import java.util.Date;

public class Eshop extends DealModel {
    private static final long serialVersionUID = 1L;
    private int productCount;

    public Eshop(int ID, String name, Buyer buyer, Dealmaker dealmaker, double price, Date date, int productCount, TeamManager teamManager) throws Exception {
        super(ID, name, buyer, dealmaker, price, date, DEAL_TYPE.ESHOP, teamManager);
        this.productCount = productCount;
    }

    public Eshop(String name, Buyer buyer, Dealmaker dealmaker, double price, Date date, int productCount) {
        super(name, buyer, dealmaker, price, date, DEAL_TYPE.ESHOP);
        this.productCount = productCount;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    /**
     * Complete the project and all the products
     */
    @Override
    public void complete() {
        super.complete();
        this.setProductCount(0);
    }

}
