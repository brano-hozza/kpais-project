package com.brano.oop2.db.daos;

import com.brano.oop2.models.users.Buyer;
import com.brano.oop2.models.users.Dealmaker;
import com.brano.oop2.models.users.ROLES;
import com.brano.oop2.models.users.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<UserModel> {
    public static int lastID = 0;
    static UserDAO instance;
    ArrayList<UserModel> userModels = new ArrayList<>();

    private UserDAO() {

    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    @Override
    public void update(UserModel item) throws DAOExceptions.ModelSaveException {
        if (item == null) throw new DAOExceptions.ModelSaveException();
        int i = 0;
        for (UserModel userModel : userModels) {
            if (userModel!=null &&  userModel.getID() == (item).getID()) break;
            i++;
        }
        userModels.set(i, item);
    }

    @Override
    public void save(UserModel item) throws DAOExceptions.ModelSaveException {
        if (item == null) throw new DAOExceptions.ModelSaveException();
        lastID++;
        userModels.add(item);
    }

    @Override
    public UserModel get(int ID) {
        if (userModels == null) {
            System.out.println("Blbost");
        }
        for (UserModel userModel : userModels) {
            if (userModel.getID() == ID) {
                return userModel;
            }
        }
        return null;
    }

    @Override
    public ArrayList<UserModel> getAll() {
        return userModels;
    }

    @Override
    public void setAll(ArrayList<UserModel> list) {
        userModels.addAll(list);
        lastID = list.size();
    }

    /**
     * Function looks for user based on username
     *
     * @param username Username
     * @return UserModel or null
     */
    public UserModel get(String username) {
        for (UserModel userModel : userModels) {
            if (userModel.getUsername().equals(username)) {
                return userModel;
            }
        }
        return null;
    }

    /**
     * Function returns all the users
     *
     * @return ArrayList of users
     */
    public List<UserModel> getUsers() {
        return userModels;
    }

    /**
     * Function checks if user entered valid credentials
     *
     * @param username Username
     * @param password User password
     * @return Profile or NULL on error
     */
    public UserModel login(String username, String password) {
        UserModel userModel = this.get(username);
        if (userModel == null || !userModel.getPassword().equals(password)) {
            return null;
        }
        return userModel;
    }

    /**
     * Function registers new user
     *
     * @param username Username
     * @param password Password
     */
    public UserModel register(String username, String password) {
        Buyer newBuyer = new Buyer(lastID, username, password, null);
        try {
            this.save(newBuyer);
        } catch (DAOExceptions.ModelSaveException e) {
            e.printStackTrace();
            System.out.println("Model is null for unknown reason");
        }
        return newBuyer;
    }

    /**
     * Get all users based on role
     *
     * @param role Specified role
     * @param <T>  Model type
     * @return ArrayList of models
     */
    public <T> ArrayList<T> getAll(ROLES role) {
        ArrayList<T> users = new ArrayList<>();
        for (UserModel usr : userModels) {
            if (usr.getRole() == role) users.add((T) usr);
        }
        return users;
    }

    /**
     * Returns all available dealmakers
     *
     * @return
     */
    public List<Dealmaker> getAvailableDealmakers() {
        List<Dealmaker> available = new ArrayList<>();
        for (UserModel userModel : userModels) {
            if (userModel instanceof Dealmaker && ((Dealmaker) userModel).isAvailable())
                available.add((Dealmaker) userModel);
        }
        return available;
    }

}
