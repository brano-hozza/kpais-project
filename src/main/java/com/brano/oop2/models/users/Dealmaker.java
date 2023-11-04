package com.brano.oop2.models.users;

import com.brano.oop2.db.daos.DAOExceptions;
import com.brano.oop2.db.daos.DealDAO;
import com.brano.oop2.models.deals.DealModel;

public class Dealmaker extends Employee implements DealEdit {
    private static final long serialVersionUID = 1L;
    private String deal;

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        if (deal.length() > 0)
            this.available = false;
        this.deal = deal;
    }


    public Dealmaker(int ID, String username, String password, boolean available, String deal) {
        super(ID, username, password, ROLES.DEALMAKER, available);
        if (deal.length() > 0)
            this.available = false;
        this.deal = deal;
    }

    /**
     * Set the deal as complete and return do buyer
     */
    public void returnDeal() {
        DealDAO.getInstance().get(this.deal).setReturned(true);
        this.deal = "";
        this.available = true;
    }

    @Override
    public void setAvailable(boolean available) {
        if (available) {
            DealModel deal = (DealDAO.getInstance()).get(this.deal);
            if (deal != null) {
                System.out.println("idzem");
                deal.complete();
                deal.setReturned(true);
                try {
                    (DealDAO.getInstance()).update(deal);
                } catch (DAOExceptions.ModelNotFoundException e) {
                    System.out.println("Nenasiel sa model");
                    e.printStackTrace();
                }
            }
            this.deal = "";
        }
        this.available = available;
    }
}
