package com.brano.oop2.db;

import com.brano.oop2.db.daos.DAO;
import com.brano.oop2.models.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Database {
    private final String tmpPath = "src/main/resources/com/brano/oop2/db/tmp/";

    LinkedHashMap<String, DAO<Model>> daos = new LinkedHashMap<>();

    public Database(String... args) throws Exception {
        for (String arr : args) {
            Object obj = Class.forName(arr).getMethod("getInstance").invoke(null, null);
            if (obj instanceof DAO) {
                daos.put(arr, (DAO<Model>) obj);
            }

        }
    }

    /**
     * Functions deserialize all the DAOS with all the data
     */
    public void loadAll() {
        daos.replaceAll((path, dao) -> {
            try {
                String[] fullPath = path.split("\\.");
                File file = new File(tmpPath + fullPath[fullPath.length - 1] + ".ser");
                if (!file.exists()) return dao;
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                dao.setAll((ArrayList<Model>) in.readObject());
                in.close();
                fileIn.close();
            } catch (IOException i) {
                i.printStackTrace();
                return null;
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                c.printStackTrace();
                return null;
            }
            return dao;
        });
        System.out.println("Load complete");
    }

    /**
     * Function serialize all the DAOS
     */
    public void saveAll() {
        daos.forEach((path, dao) -> {
            try {
                String[] fullPath = path.split("\\.");
                File dir = new File(tmpPath);
                if (!dir.exists()) dir.mkdir();
                File file = new File(tmpPath + fullPath[fullPath.length - 1] + ".ser");
                file.createNewFile();
                FileOutputStream fileOut =
                        new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(dao.getAll());
                out.close();
                fileOut.close();
                System.out.println("Serialized data is saved.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
