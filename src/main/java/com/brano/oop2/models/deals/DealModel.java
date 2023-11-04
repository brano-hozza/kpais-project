package com.brano.oop2.models.deals;

import com.brano.oop2.models.Model;
import com.brano.oop2.models.users.Buyer;
import com.brano.oop2.models.users.Dealmaker;
import com.brano.oop2.models.users.TeamManager;

import java.util.Date;

public abstract class DealModel implements Model {
    private static final long serialVersionUID = 1L;
    private int ID;
    private boolean done = false;
    private boolean returned = false;
    private String name;
    private Buyer buyer;
    private Dealmaker dealmaker;
    private double price;
    private Date date;
    private DEAL_TYPE dealType;
    private TeamManager teamManager;

    public DealModel(int ID, String name, Buyer buyer, Dealmaker dealmaker, double price, Date date, DEAL_TYPE dealType, TeamManager teamManager) {
        this.ID = ID;
        this.name = name;
        this.buyer = buyer;
        this.dealmaker = dealmaker;
        this.date = date;
        this.price = price;
        this.dealType = dealType;
        this.teamManager = teamManager;
    }

    public DealModel(String name, Buyer buyer, Dealmaker dealmaker, double price, Date date, DEAL_TYPE dealType) {
        this.name = name;
        this.buyer = buyer;
        this.dealmaker = dealmaker;
        this.date = date;
        this.price = price;
        this.dealType = dealType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public Dealmaker getDealmaker() {
        return dealmaker;
    }

    public void setDealmaker(Dealmaker dealmaker) {
        this.dealmaker = dealmaker;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DEAL_TYPE getDealType() {
        return dealType;
    }

    public void setDealType(DEAL_TYPE dealType) {
        this.dealType = dealType;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public int getID() {
        return ID;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public void setTeamManager(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    /**
     * Sets deal as done
     */

    public void complete() {
        this.done = true;
    }

    public void setDone(boolean val) {
        this.done = val;
    }

    public boolean getDone() {
        return this.done;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
}
