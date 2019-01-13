package classes;

import bdd.Attribut;
import bdd.Connexion;
import controlers.LockerControler;
import controlers.MainPanelControler;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import static util.QueryUtil.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Locker extends Pane {

    private LockerControler controller;
    private MainPanelControler mainPanelControler;

    private User user;
    private List<Bottle> bottles;
    private int[] couleurCount = new int[Couleurs.size];
    private int id;
    private String nom;
    public static final int ID_UNDEFINED = -1;

    public Locker(User u) throws IOException {
        this(ID_UNDEFINED, "Nom", u);
    }

    public Locker(int id, String nom, User u) throws IOException {
        user = u;
        this.id = id;
        this.nom = nom;
        bottles = new ArrayList<>();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/locker.fxml"));
        fxmlLoader.setControllerFactory(param -> controller = new LockerControler());
        Node view = (Node) fxmlLoader.load();
        getChildren().add(view);

        controller.setLocker(this);
        controller.setName(nom);
        controller.addTextListener((observable, oldValue, newValue) -> setNom(newValue));
    }

    public void addBottle(Bottle bottle)
    {
        bottles.add(bottle);
        controller.addBottle(bottle);
        couleurCount[bottle.getVin().getCouleur().ordinal()]++;
        controller.updateCount(couleurCount);
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public void setMainPanelControler(MainPanelControler controler)
    {
        mainPanelControler = controler;
        controller.setMainPanelControler(mainPanelControler);
    }

    public List<Bottle> getBottles() {
        return bottles;
    }

    public void synchronize() throws SQLException {
        String sql="";
        Connexion con = new Connexion();
        if(id == ID_UNDEFINED){
             sql = buildInsert(TABLE_CASIER,CASIER_NOM,nom,CASIER_IDUTILISATEUR, Integer.toString(user.getId()))+ " RETURNING idutilisateur";
            ResultSet rs = con.executeStatement(sql);
            rs.next();
            id = rs.getInt(1);
            bottlesSave();
        }
        else {
            sql = buildUpdate(TABLE_CASIER,CASIER_NOM,nom) + " WHERE " + CASIER_ID + " = " + id;
            con.executeUpdate(sql);
            bottlesSave();
        }
    }

    public String getNom() {
        return nom;
    }

    public void delete() throws SQLException {

        if (id == ID_UNDEFINED) return;
        Connexion con = new Connexion();
//        for(Bottle b : bottles){
//            mainPanelControler.removeBottle(b,con);
//        }


        con.executeUpdate(buildDeleteId(TABLE_CASIER, CASIER_ID, id));
    }

    public boolean isEmpty()
    {
        return bottles.isEmpty();
    }

    public void bottlesSave() throws SQLException {
        String sql;
        Connexion con = new Connexion();
        for(Bottle b : bottles){
            b.save();
            sql = buildSelect(TABLE_CONTENIR,"count(*)") + " WHERE " + CONTENIR_IDCASIER + " = " + id + " and " + CONTENIR_IDBOUTEILLE + " = " + b.getId();
            ResultSet rs = con.executeStatement(sql);
            rs.next();
            if(rs.getInt(1)==0){
                sql=buildInsert(TABLE_CONTENIR,CONTENIR_IDBOUTEILLE,Integer.toString(b.getId()),CONTENIR_IDCASIER,Integer.toString(id),CONTENIR_QUANTITE,"1");
                con.executeUpdate(sql);
            }
//            else{
//                sql = buildUpdateNoQuote(TABLE_CONTENIR,CONTENIR_QUANTITE,CONTENIR_QUANTITE + "+1") + " WHERE " + CONTENIR_IDCASIER + " = " + id + " and " + CONTENIR_IDBOUTEILLE + " = " + b.getId();
//                con.executeUpdate(sql);
//            }
        }
    }
    public void loadBottles() throws SQLException {
        Connexion con = new Connexion();
        String sql = "Select b.idbouteille,b.volume,b.idvin,b.code,b.millesime FROM Bouteille b inner join Contenir c on (b.idbouteille=c.idbouteille) where c.idcasier="+id;
        ResultSet rs = con.executeStatement(sql);
        while(rs.next()){
                Bottle b = new Bottle(rs.getInt(2),rs.getInt(5),rs.getString(4), Attribut.getWineById(rs.getInt(3)));
                b.setId(rs.getInt(1));
                mainPanelControler.addBottle(b, this);
        }

    }
    public void removeBottle(Bottle b, Connexion con) throws SQLException {
        if(b.getId() != ID_UNDEFINED){
            String sql = "Delete From " + TABLE_CONTENIR + " where " + CONTENIR_IDCASIER + " = " + id + "  and " + CONTENIR_IDBOUTEILLE + " = " + b.getId();
            con.executeUpdate(sql);
        }
        bottles.remove(b);
        controller.removeBottle(b);
        couleurCount[b.getVin().getCouleur().ordinal()]--;
        controller.updateCount(couleurCount);
    }

}
