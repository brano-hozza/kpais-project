package com.brano.oop2.models.deals;

import com.brano.oop2.models.users.Buyer;
import com.brano.oop2.models.users.Dealmaker;
import com.brano.oop2.models.users.TeamManager;

import java.util.Date;

public class Portfolio extends DealModel {
    private static final long serialVersionUID = 1L;

    public Portfolio(int ID, String name, Buyer buyer, Dealmaker dealmaker, double price, Date date, TeamManager teamManager) {
        super(ID, name, buyer, dealmaker, price, date, DEAL_TYPE.PORTFOLIO, teamManager);
    }

    public Portfolio(String name, Buyer buyer, Dealmaker dealmaker, double price, Date date) {
        super(name, buyer, dealmaker, price, date, DEAL_TYPE.PORTFOLIO);
    }
}
