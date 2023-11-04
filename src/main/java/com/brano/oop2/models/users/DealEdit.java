package com.brano.oop2.models.users;

import com.brano.oop2.models.deals.DEAL_TYPE;
import com.brano.oop2.models.deals.DealModel;
import com.brano.oop2.models.deals.Eshop;
import com.brano.oop2.models.deals.Portfolio;
import javafx.scene.control.Alert;

import java.util.Date;

public interface DealEdit {
    /**
     * Updates the current deal
     *
     * @param type        Deal type
     * @param id          deal ID
     * @param projectName Project/Deal name
     * @param buyer       Buyer of the deal
     * @param dm          Dealmaker of the deal
     * @param price       Price of the deal
     * @param date        Finish line date
     * @param count       Amount of products
     * @param teamManager Manager of project
     * @return Updated deal
     */
    default DealModel updateDeal(DEAL_TYPE type, int id, String projectName, Buyer buyer, Dealmaker dm, Double price, Date date, int count, TeamManager teamManager) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba!");
        if (projectName.length() < 6) {
            alert.setHeaderText("Názov projektu musí byť minimálne 6 znakov!");
            alert.show();
            return null;
        }
        if (price < 1) {
            alert.setHeaderText("Zadajte realnu cenu vacsiu ako 0!");
            alert.show();
            return null;
        }
        if (type == DEAL_TYPE.ESHOP && count < 1) {
            alert.setHeaderText("Zadajte realny pocet produktov, vacsi ako 0!");
            alert.show();
            return null;
        }

        DealModel deal = null;
        try {
            if (type == DEAL_TYPE.ESHOP) {
                deal = new Eshop(
                        id,
                        projectName,
                        buyer,
                        dm,
                        price,
                        date,
                        count,
                        teamManager
                );
            }
            if (type == DEAL_TYPE.PORTFOLIO) {
                deal = new Portfolio(
                        id,
                        projectName,
                        buyer,
                        dm,
                        price,
                        date,
                        teamManager
                );
            }
        } catch (Exception e) {
            alert.setHeaderText("Nepodarilo sa vytvorit zakazku!");
            alert.show();
            System.out.println(e);
            return null;
        }
        if (deal != null)
            deal.getDealmaker().setAvailable(false);
        return deal;
    }
}
