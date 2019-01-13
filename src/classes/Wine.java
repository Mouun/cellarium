package classes;

import bdd.Attribut;
import bdd.Connexion;

import java.sql.ResultSet;
import java.sql.SQLException;

import static util.QueryUtil.*;

public class Wine {
    private String domaine;
    private String appellation;
    private Couleurs couleur;
    private String cepage;
    private float degres;

    public Wine(String domaine, String appellation, Couleurs couleur, String cepage, float degres) {
        this.domaine = domaine;
        this.appellation = appellation;
        this.couleur = couleur;
        this.cepage = cepage;
        this.degres=degres;
  //      String sql = "select region.nomregion,typeappellation.nomta from vin INNER JOIN appellation on(vin.idapp=appellation.idapp) INNER JOIN region on(region.idregion = appellation.idregion) INNER JOIN typeappellation ON (appellation.idtype = typeappellation.idta) where "
    }

    public String insertQuery() throws SQLException
    {
        return buildInsert(TABLE_VIN,
                VIN_COULEUR, couleur.toString(),
                VIN_DEGRES, Float.toString(degres),
                VIN_IDAPP, Integer.toString(Attribut.getAppellationId(appellation)),
                VIN_IDCEPAGE, Integer.toString(Attribut.getCepageId(cepage)),
                VIN_IDDOMAINE, Integer.toString(Attribut.getDomaineId(domaine)));
    }

    public String toString()
    {
        return domaine + " " + appellation;
    }

    public Couleurs getCouleur() { return couleur; }

    public String getDomaine() { return domaine; }
    public String getAppellation() { return appellation; }
    public String getCepage() { return cepage; }
    public float getDegres() { return degres; }
//    public String getRegion() throws SQLException {
//        Connexion con = new Connexion();
//        String sql = "select region.nomregion from vin INNER JOIN appellation on(vin.idapp=appellation.idapp) INNER JOIN region on(region.idregion = appellation.idregion) where " + VIN_ID + "= " + Attribut.getVinId(this);
//        ResultSet rs = con.executeStatement(sql);
//        rs.next();
//        return rs.getString(1);
//    }
//    public String getTypeApp() throws SQLException {
//        Connexion con = new Connexion();
//        String sql = "select typeappellation.nomta from vin INNER JOIN appellation on(vin.idapp=appellation.idapp) INNER JOIN typeappellation ON (appellation.idtype = typeappellation.idta) where " + VIN_ID + "= " + Attribut.getVinId(this);
//        ResultSet rs = con.executeStatement(sql);
//        rs.next();
//        return rs.getString(1);
//    }

}
