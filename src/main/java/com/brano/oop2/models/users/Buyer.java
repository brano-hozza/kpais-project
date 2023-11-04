package com.brano.oop2.models.users;

import com.brano.oop2.controllers.DEAL_STATUS;
import com.brano.oop2.db.daos.DAOExceptions;
import com.brano.oop2.db.daos.DealDAO;
import com.brano.oop2.models.deals.DEAL_TYPE;
import com.brano.oop2.models.deals.DealModel;
import com.brano.oop2.models.deals.Eshop;
import com.brano.oop2.models.deals.Portfolio;

import java.util.Date;

public class Buyer extends UserModel {
    private static final long serialVersionUID = 1L;
    private DealModel deal;

    public Buyer(int ID, String username, String password, DealModel deal) {
        super(ID, username, password, ROLES.BUYER);
        this.deal = deal;
    }

    public DealModel getDeal() {
        return deal;
    }

    public void setDeal(DealModel deal) {
        this.deal = deal;
    }

    /**
     * Checks the state of current buyer deal
     *
     * @return Deal status
     */
    public DEAL_STATUS checkExistingDeal() {
        if (deal != null) {
            try {
                deal = DealDAO.getInstance().get(deal.getID());
            } catch (DAOExceptions.ModelNotFoundException e) {
                deal = null;
            }
        }
        this.setDeal(deal);
        if (deal == null)
            return DEAL_STATUS.NONE;
        if (deal.isReturned()) {
            this.deal = null;
            return DEAL_STATUS.COMPLETE;
        }
        return DEAL_STATUS.PENDING;
    }

    public static class IncorrectDealExceptions extends Exception {
    }

    public static class ShortNameException extends IncorrectDealExceptions {
    }

    public static class LowPriceException extends IncorrectDealExceptions {
    }

    public static class NoDealTypeExeption extends IncorrectDealExceptions {
    }

    public static class NeedAmountException extends IncorrectDealExceptions {
    }

    /**
     * Creates new deal
     *
     * @param name      Deal name
     * @param type      Deal type
     * @param dealmaker Dealmaker to take care of deal
     * @param price     Price of the deal
     * @param amount    Amount of products
     * @throws IncorrectDealExceptions Check if deal has correct parameters
     */
    public void createNewDeal(String name, DEAL_TYPE type, Dealmaker dealmaker, double price, int amount) throws IncorrectDealExceptions {
        if (name.length() < 5) throw new ShortNameException();
        if (price < 1) throw new LowPriceException();
        if (type == null) throw new NoDealTypeExeption();
        if (type == DEAL_TYPE.ESHOP && amount < 1) throw new NeedAmountException();
        DealModel newDeal;
        if (type == DEAL_TYPE.ESHOP) {
            newDeal = new Eshop(name, this, dealmaker, price, new Date(System.currentTimeMillis()), amount);
        } else {
            newDeal = new Portfolio(name, this, dealmaker, price, new Date(System.currentTimeMillis()));
        }
        dealmaker.setAvailable(false);
        this.setDeal(newDeal);
        dealmaker.setDeal(newDeal.getName());
        DealDAO.getInstance().save(newDeal);

    }
}
