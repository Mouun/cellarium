package classes;

import bdd.Attribut;
import bdd.Connexion;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.w3c.dom.Attr;

import java.sql.ResultSet;
import java.sql.SQLException;

import static util.QueryUtil.*;

public class Bottle {

    private int volume;
    private Byte[] image;
    private int id;
    private String barcode;
    private Wine vin;
    private int millesime;
    public static final int ID_UNDEFINED = -1;
    private BottleCircle currentCircle;

    public void setId(int id) {
        this.id = id;
    }

    public Bottle(int volume, int millesime, String barcode, Wine vin) {
        this.id=ID_UNDEFINED;
        this.volume = volume;
        this.millesime = millesime;
        this.barcode = barcode;
        this.vin = vin;

        currentCircle = null;
    }

    public int getVolume() {
        return volume;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;

    }

    public Wine getVin() {
        return vin;
    }

    public String getBarcode() {
        return barcode;
    }
    public String insertQuery() throws SQLException
    {
        return buildInsert(TABLE_BOUTEILLE,
                BOUTEILLE_VOLUME, Integer.toString(volume),
                BOUTEILLE_IDVIN, Integer.toString(Attribut.getVinId(vin)),
                BOUTEILLE_CODE, barcode,
                BOUTEILLE_MILLESIME, Integer.toString(millesime));
    }

    public String toString()
    {
        return vin + " " + millesime;
    }

    public BottleCircle getCircle()
    {
        BottleCircle circle = new BottleCircle(24.0, this);
        circle.setFill(vin.getCouleur().getColor());
        circle.strokeProperty().setValue(Paint.valueOf("#000000"));
        circle.setStrokeWidth(0.0);

        currentCircle = circle;
        return circle;
    }

    public int getMillesime() { return millesime; }

    public void highlightCircle()
    {
        if (currentCircle == null) return;

        currentCircle.setStrokeWidth(4.0);
    }

    public void dehighlightCircle()
    {
        if (currentCircle == null) return;

        currentCircle.setStrokeWidth(0.0);
    }

    public int getId() {
        return id;
    }

    public void save() throws SQLException {
        String sql;
        Connexion con = new Connexion();
        if (id == ID_UNDEFINED){
            sql= buildInsert(TABLE_BOUTEILLE,BOUTEILLE_VOLUME,Integer.toString(volume),BOUTEILLE_IDVIN,Integer.toString(Attribut.getVinId(vin)),BOUTEILLE_MILLESIME,Integer.toString(millesime)) + " returning idbouteille";
            ResultSet rs =  con.executeStatement(sql);
            rs.next();
            id= rs.getInt(1);

        }
    }
}
