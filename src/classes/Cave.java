package classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;

public class Cave {

    private String caveName;
    private String caveDescription;

    private static ArrayList<Locker> lockers = new ArrayList<>();
    private static ObservableList<Bottle> listBottles;
    private static ObservableList<String> listNomBouteilles = FXCollections.observableArrayList();

    public Cave(String caveName, String caveDescription) {
        this.caveName = caveName;
        this.caveDescription = caveDescription;
        listBottles = FXCollections.observableArrayList();
    }

    public static ObservableList<String> getListNomBouteilles() {
        return listNomBouteilles;
    }

    public static ObservableList<Bottle> getListBottles() {
        return listBottles;
    }

    /*public static void updateListBouteilles() {
        for (Bottle b : listBottles) {
            if(!contientBouteille(b)) {
                listNomBouteilles.add(b.getNom());
            }
        }
    }

    public static void ajouterBouteille(Bottle b) {
        listBottles.add(b);
        listNomBouteilles.add(b.getNom());
    }*/

    public static boolean contientBouteille(Bottle b) {
        return listBottles.contains(b);
    }


}
