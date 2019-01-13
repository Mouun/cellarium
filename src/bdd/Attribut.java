package bdd;

import classes.Couleurs;
import classes.Wine;
import util.CellariumUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static util.QueryUtil.*;

/**
 * Classe permettant de stocker de maniere permanente des informations relatives aux clés étrangéres d'un vin de la base de données
 * Chaque attribut qui nous interesse est modélisé par une hashmap<String,Integer> le string correspondant au nom de l'attribut et l'Integer à son id dans la base de données
 * Permet de ne pas tout le temps avoir de besoin d'aller chercher des informations dans la base de données
 * On génére ces hashmap via une connexion à la base de données.
 * On regénére ces hashmap seleument si elle est confronté à une informations inconnue
 */
public class Attribut {
    static HashMap<String, Integer> domaines;
    static HashMap<String, Integer> appellations;
    static HashMap<String, Integer> cepages;
    static HashMap<String, Integer> typeAppellations;
    static HashMap<String, Integer> regions;
    static HashMap<Wine, Integer> vins;


    private static HashMap<String, Integer> generateTableMap(String table, String name, String id) throws SQLException
    {
        Connexion con = new Connexion();
        HashMap<String, Integer> temp = new HashMap<String, Integer>();
        //String sql ="Select nomdomaine,iddomaine from domaine ";
        String sql = buildSelect(table, name, id);
        ResultSet rs = con.executeStatement(sql);
        while (rs.next()) {
            temp.put(CellariumUtil.clearBOM(rs.getString(name).toLowerCase()), rs.getInt(id));
        }
        return temp;
    }

    private static void generateDomaine() throws SQLException {
        domaines = generateTableMap(TABLE_DOMAINE, DOMAINE_NOM, DOMAINE_ID);
    }

    private static void generateAppellation() throws SQLException {
        appellations = generateTableMap(TABLE_APPELLATION, APPELLATION_NOM, APPELLATION_ID);
    }

    private static void generateTypeAppellation() throws SQLException {
        typeAppellations = generateTableMap(TABLE_TYPEAPPELLATION, TYPEAPPELLATION_NOM, TYPEAPPELLATION_ID);
    }

    private static void generateCepage() throws SQLException {
        cepages = generateTableMap(TABLE_CEPAGE, CEPAGE_NOM, CEPAGE_ID);
    }

    private static void generateRegion() throws SQLException {
        regions = generateTableMap(TABLE_REGION, REGION_NOM, REGION_ID);
    }

    public static void generateVin() throws SQLException {
        String domaine = "";
        String cepage = "";
        String appellation = "";
        Connection con = Connexion.connexionBDD();
        HashMap<Wine, Integer> temp = new HashMap<Wine, Integer>();
        //  String sql ="Select nomregion,idregion from region ";
        String sql = buildSelect(TABLE_VIN, "*");
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            if (domaines == null) generateDomaine();
            if (cepages == null) generateCepage();
            if (appellations == null) generateAppellation();
            for (Map.Entry<String, Integer> e : domaines.entrySet()) {
                if (e.getValue() == rs.getInt(VIN_IDDOMAINE)) {
                    domaine = e.getKey();
                    break;
                }
            }
            for (Map.Entry<String, Integer> e : cepages.entrySet()) {
                if (e.getValue() == rs.getInt(VIN_IDCEPAGE)) {
                    cepage = e.getKey();
                    break;
                }
            }
            for (Map.Entry<String, Integer> e : appellations.entrySet()) {
                if (e.getValue() == rs.getInt(VIN_IDAPP)) {
                    appellation = e.getKey();
                    break;
                }
            }
            Wine vin = new Wine(domaine, appellation, Couleurs.fromString(rs.getString(VIN_COULEUR)), cepage, rs.getFloat(VIN_DEGRES));
            temp.put(vin, rs.getInt(VIN_ID));
        }
        vins = temp;
    }


    static HashMap<String, Integer> getDomaines() {
        return domaines;
    }

    static HashMap<String, Integer> getAppellations() {
        return appellations;
    }

    static HashMap<String, Integer> getCepages() {
        return cepages;
    }

    static HashMap<String, Integer> getTypeAppellations() {
        return typeAppellations;
    }

    static HashMap<String, Integer> getRegions() {
        return regions;
    }

    static HashMap<Wine, Integer> getVins() {
        return vins;
    }

    static public Set<String> getDomaineSet() throws SQLException {
        if (domaines == null) generateDomaine();
        return domaines.keySet();
    }

    static public Set<String> getAppellationSet() throws SQLException {
        if (appellations == null) generateAppellation();
        return appellations.keySet();
    }

    static public Set<String> getCepageSet() throws SQLException {
        if (cepages == null) generateCepage();
        return cepages.keySet();
    }

    static public Set<String> getTypeAppellationSet() throws SQLException {
        if (typeAppellations == null) generateTypeAppellation();
        return typeAppellations.keySet();
    }

    static public Set<String> getRegionSet() throws SQLException {
        if (regions == null) generateRegion();
        return regions.keySet();
    }

    static public Set<Wine> getVinSet() throws SQLException {
        if (vins == null) generateVin();
        return vins.keySet();
    }

    public static int getDomaineId(String name) throws SQLException {
        if (Attribut.getDomaines() == null) Attribut.generateDomaine();

        Integer id = Attribut.domaines.get(name.toLowerCase());

        if (id == null) {
            //Requête ajouter domaine
            generateDomaine();
            id = domaines.get(name.toLowerCase());
        }

        return id;
    }

    public static int getAppellationId(String name) throws SQLException {
        if (getAppellations() == null) generateAppellation();

        Integer id = appellations.get(name.toLowerCase());

        if (id == null) {
            //Requête ajouter domaine
            generateAppellation();
            id = appellations.get(name.toLowerCase());
        }
        return id;
    }

    public static int getTypeAppellationId(String name) throws SQLException {
        if (getTypeAppellations() == null) generateTypeAppellation();

        Integer id = typeAppellations.get(name.toLowerCase());

        if (id == null) {
            //Requête ajouter domaine
            generateTypeAppellation();
            id = typeAppellations.get(name.toLowerCase());
        }
        return id;
    }

    public static int getCepageId(String name) throws SQLException {
        if (getCepages() == null) generateCepage();

        Integer id = cepages.get(name.toLowerCase());

        if (id == null) {
            //Requête ajouter domaine
            generateCepage();
            id = cepages.get(name.toLowerCase());
        }
        return id;
    }

    public static int getRegionId(String name) throws SQLException {
        if (getRegions() == null) generateRegion();

        Integer id = regions.get(name.toLowerCase());

        if (id == null) {
            //Requête ajouter domaine
            generateRegion();
            id = regions.get(name.toLowerCase());
        }
        return id;
    }

    public static int getVinId(Wine vin) throws SQLException {
        if (getVins() == null) generateVin();

        Integer id = vins.get(vin);

        if (id == null) {
            //Requête ajouter domaine
            generateVin();
            id = vins.get(vin);
        }
        return id;
    }


    public static List<String> getAppFromReg(String region) throws SQLException {
        Connection con = Connexion.connexionBDD();
        List<String> listAppFromReg = new ArrayList<>();
        String sql = buildSelect(TABLE_APPELLATION, APPELLATION_NOM) + " WHERE " + APPELLATION_IDREGION + "=" + getRegionId(region);
        System.out.println(sql);
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            listAppFromReg.add(rs.getString(APPELLATION_NOM));
        }
        return listAppFromReg;
    }
    public static Wine getWineById(int id) throws SQLException {
        if(getVins() == null) generateVin();
        for (Map.Entry<Wine, Integer> e : vins.entrySet()) {
            if (e.getValue() == id) {
                return e.getKey();
            }
        }
        return null;
    }
}