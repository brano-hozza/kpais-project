package com.brano.oop2.db.daos;

import com.brano.oop2.db.daos.DAOExceptions.ModelNotFoundException;
import com.brano.oop2.models.deals.DealModel;
import com.brano.oop2.models.users.Dealmaker;

import java.util.ArrayList;


public class DealDAO implements DAO<DealModel> {
    public static int lastID = 0;
    static DealDAO instance;
    ArrayList<DealModel> dealModels = new ArrayList<>();

    private DealDAO() {
    }

    public static DealDAO getInstance() {
        if (instance == null) {
            instance = new DealDAO();
        }
        return instance;
    }

    @Override
    public ArrayList<DealModel> getAll() {
        return dealModels;
    }

    @Override
    public void setAll(ArrayList<DealModel> list) {
        dealModels.addAll(list);
        lastID = list.size();
    }

    @Override
    public void update(DealModel item) throws ModelNotFoundException {
        int i = 0;
        for (DealModel dealModel : dealModels) {
            if (dealModel.getID() == (item).getID()) break;
            i++;
        }
        dealModels.set(i, item);
    }

    @Override
    public void save(DealModel item) {
        item.setID(lastID);
        lastID++;
        dealModels.add(item);
    }

    @Override
    public DealModel get(int ID) throws ModelNotFoundException {
        for (DealModel dealModel : dealModels) {
            if (dealModel.getID() == ID) {
                return dealModel;
            }
        }
        throw new ModelNotFoundException();
    }

    /**
     * Get the deal based on deal name
     *
     * @param name Name of the deal
     * @return Deal model
     */
    public DealModel get(String name) {
        for (DealModel dealModel : dealModels) {
            if (dealModel.getName().equals(name)) {
                return dealModel;
            }
        }
        return null;
    }

    /**
     * Get all deals for selected dealmaker
     *
     * @param dm Dealmaker of the deals
     * @return ArrayList of deals
     */
    public ArrayList<DealModel> getAllFor(Dealmaker dm) {
        ArrayList<DealModel> deals = new ArrayList<>();
        for (DealModel deal : dealModels) {
            if (deal.getDealmaker().getID() == dm.getID()) {
                deals.add(deal);
            }
        }
        return deals;
    }

    /**
     * Sets the deal as completed
     *
     * @param project Name of the project
     */
    public void completeProject(String project) {
        for (DealModel dealModel : dealModels) {
            if (dealModel.getName().equals(project)) {
                dealModel.complete();
            }
        }
    }
}
