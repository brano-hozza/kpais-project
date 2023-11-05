package com.brano.oop2.services;

import com.brano.oop2.db.daos.DAOExceptions;
import com.brano.oop2.db.daos.UserDAO;
import com.brano.oop2.models.deals.DealModel;
import com.brano.oop2.models.users.*;

import java.util.List;

public class UserService {
    static UserService instance;

    private final UserDAO dao;

    private UserService() {
        this.dao = UserDAO.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public List<UserModel> getUsers() {
        return this.dao.getAll();
    }

    public<T> List<T> getUsers(ROLES role) {
        return this.dao.getAll(role);
    }

    public List<Dealmaker> getAvailableDealmakers() {
        return this.dao.getAvailableDealmakers();
    }

    /**
     * Method validates user data
     *
     * @return String error or null
     */
    public String validateUser(String username, UserModel user) {
        if (username.length() < 5) {
            return "Meno užívateľa musí mať aspoň 5 znakov!";
        }
        if (!user.getUsername().equals(username) && dao.get(username) != null) {
            return "Meno užívateľa musí byť jedinečné!";
        }
        return null;
    }

    public void updateBuyer(UserModel model, String username, String[] address) throws DAOExceptions.ModelSaveException {
        DealModel deal = null;
        if (model instanceof Buyer)
            deal = ((Buyer) model).getDeal();
        Buyer buyer = new Buyer(model.getID(), username, model.getPassword(), deal);
        buyer.setAddress(address[0], address[1], Integer.parseInt(address[2]));
        dao.update(buyer);
    }

    public void updateDealmaker(UserModel model, String username, String[] address, boolean available) throws DAOExceptions.ModelSaveException {
        String deal = "";
        boolean userAvailable = true;
        if (model instanceof Dealmaker)
            deal = ((Dealmaker) model).getDeal();
        if (model instanceof Employee)
            userAvailable = ((Employee) model).isAvailable();
        Dealmaker dm = new Dealmaker(model.getID(), username, model.getPassword(), userAvailable, deal);
        dm.setAvailable(available);
        dm.setAddress(address[0], address[1], Integer.parseInt(address[2]));
        dao.update(dm);

    }

    public void updateAdmin(UserModel model, String username, String[] address) throws DAOExceptions.ModelSaveException {
        Admin admin = new Admin(model.getID(), username, model.getPassword());
        admin.setAddress(address[0], address[1], Integer.parseInt(address[2]));
        dao.update(admin);
    }

    public void updateDeveloper(UserModel model, String username, String[] address, boolean available) throws DAOExceptions.ModelSaveException {
        String project = "";
        if (model instanceof TeamPerson)
            project = ((TeamPerson) model).getProject();
        String type = "FE";
        if (model instanceof Developer)
            type = ((Developer) model).getType();
        Developer developer = new Developer(model.getID(), username, model.getPassword(), available, project, type);
        developer.setAddress(address[0], address[1], Integer.parseInt(address[2]));
        dao.update(developer);

    }

    public void updateTeamManager(UserModel model, String username, String[] address, boolean available) throws DAOExceptions.ModelSaveException {
        String project = "";
        if (model instanceof TeamPerson)
            project = ((TeamPerson) model).getProject();
        TeamManager tm = new TeamManager(model.getID(), username, model.getPassword(), available, project);
        tm.setAddress(address[0], address[1], Integer.parseInt(address[2]));
        dao.update(tm);

    }

    public void updateUser(UserModel user) throws DAOExceptions.ModelSaveException {
        this.dao.update(user);
    }
}
