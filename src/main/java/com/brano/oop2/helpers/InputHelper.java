package com.brano.oop2.helpers;

import com.brano.oop2.db.daos.DealDAO;
import com.brano.oop2.db.daos.TaskDAO;
import com.brano.oop2.db.daos.UserDAO;
import com.brano.oop2.models.deals.DealModel;
import com.brano.oop2.models.tasks.TaskModel;
import com.brano.oop2.models.users.UserModel;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;


public class InputHelper {
    /**
     * Fix the input just for whole numbers
     *
     * @param field TextField to be fixed
     */
    public static void fixCountInput(TextField field) {
        field.setText("0");
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]+")) {
                field.setText(newValue.replaceAll("[^\\d.]", ""));
                StringBuilder aus = new StringBuilder(newValue);
                boolean firstPointFound = false;
                for (int i = 0; i < aus.length(); i++) {
                    if (aus.charAt(i) == '.') {
                        if (!firstPointFound)
                            firstPointFound = true;
                        else
                            aus.deleteCharAt(i);
                    }
                }
                newValue = aus.toString();
            }
        });
    }

    /**
     * Fix the input for valid price
     *
     * @param field TextField to be fixed
     */
    public static void fixPriceInput(TextField field) {
        field.setText("0");
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]+(\\.[0-9]*)?")) {
                field.setText(newValue.replaceAll("[^\\d.]", ""));
                StringBuilder aus = new StringBuilder(newValue);
                boolean firstPointFound = false;
                for (int i = 0; i < aus.length(); i++) {
                    if (aus.charAt(i) == '.') {
                        if (!firstPointFound)
                            firstPointFound = true;
                        else
                            aus.deleteCharAt(i);
                    }
                }
                newValue = aus.toString();
            }
        });
    }

    /**
     * Converter for ChoiceBox of users
     *
     * @param <T>
     */
    public static class UserConverter<T> extends StringConverter<T> {

        @Override
        public String toString(T object) {
            if(object == null) return "";
            return ((UserModel) object).getUsername();
        }

        @Override
        public T fromString(String string) {
            if(string == null || string.isEmpty()) return null;
            //noinspection unchecked
            return (T) UserDAO.getInstance().get(string);
        }
    }

    /**
     * Converter for ChoiceBox of deals
     *
     * @param <T>
     */
    public static class DealConverter<T> extends StringConverter<T> {
        @Override
        public String toString(T object) {
            return ((DealModel) object).getName();
        }

        @Override
        public T fromString(String string) {
            //noinspection unchecked
            return (T) DealDAO.getInstance().get(string);
        }
    }

    /**
     * Converter for ChoiceBox of tasks
     *
     * @param <T>
     */
    public static class TaskConverter<T> extends StringConverter<T> {
        @Override
        public String toString(T object) {
            return ((TaskModel) object).getName();
        }

        @Override
        public T fromString(String string) {
            //noinspection unchecked
            return (T) TaskDAO.getInstance().get(string);
        }
    }
}
