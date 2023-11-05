package com.brano.oop2.services;

import com.brano.oop2.db.daos.DAOExceptions;
import com.brano.oop2.db.daos.DealDAO;
import com.brano.oop2.db.daos.UserDAO;
import com.brano.oop2.models.deals.DEAL_TYPE;
import com.brano.oop2.models.deals.DealModel;
import com.brano.oop2.models.users.Buyer;
import com.brano.oop2.models.users.Dealmaker;
import com.brano.oop2.models.users.TeamManager;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class DealService {
    static DealService instance;

    private final DealDAO dao;
    private final UserService userService;

    private DealService() {
        this.dao = DealDAO.getInstance();
        this.userService = UserService.getInstance();
    }

    public static DealService getInstance() {
        if (instance == null) {
            instance = new DealService();
        }
        return instance;
    }

    public List<DealModel> getAllDeals() {
        return dao.getAll();
    }

    public List<DealModel> getAllDealsFor(Dealmaker dm) {
        return dao.getAllFor(dm);
    }

    public DealModel updateDeal(Dealmaker currentDealmaker, DEAL_TYPE type, int id, String projectName, Buyer buyer, Dealmaker dm, Double price, Date date, int count, DealModel selectedDeal) throws DAOExceptions.ModelNotFoundException, DAOExceptions.ModelSaveException {
        DealModel newDeal = currentDealmaker.updateDeal(
                type,
                id,
                projectName,
                buyer,
                dm,
                price,
                date,
                count,
                selectedDeal.getTeamManager()
        );
        if (newDeal == null) {
            return null;
        }
        if (selectedDeal.isReturned()) {
            newDeal.complete();
        }
        dm.setAvailable(false);
        dao.update(newDeal);
        userService.updateUser(dm);
        return newDeal;
    }


}
