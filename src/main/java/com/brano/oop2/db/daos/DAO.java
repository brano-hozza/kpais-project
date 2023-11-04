package com.brano.oop2.db.daos;

import com.brano.oop2.db.daos.DAOExceptions.ModelNotFoundException;

import java.util.ArrayList;

public interface DAO<Model> {


    /**
     * Function updates selected model in list
     *
     * @param item Item to be updated
     * @throws ModelNotFoundException when cannot find model
     */
    void update(Model item) throws ModelNotFoundException, DAOExceptions.ModelSaveException;

    /**
     * Function adds new model to list
     *
     * @param item Model to add
     * @throws DAOExceptions.ModelSaveException when cannot save model
     */
    void save(Model item) throws DAOExceptions.ModelSaveException;

    /**
     * Function returns model from list
     *
     * @param ID id of item in list
     * @return model
     * @throws ModelNotFoundException Throws when cant find model
     */
    Model get(int ID) throws ModelNotFoundException;

    /**
     * Function return ArrayList of loaded models
     *
     * @return ArrayList of models
     */
    ArrayList<Model> getAll();

    /**
     * Function return ArrayList of loaded models
     */
    void setAll(ArrayList<Model> list);

}
