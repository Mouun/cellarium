package util;

public class QueryUtil {
    public static final String TABLE_UTILISATEUR = "utilisateur",TABLE_CONTENIR="contenir",TABLE_CASIER = "casier" ,TABLE_APPELLATION = "appellation", TABLE_BOUTEILLE = "bouteille", TABLE_CAVE = "cavevin", TABLE_DOMAINE = "domaine", TABLE_TYPEAPPELLATION = "typeappellation", TABLE_CEPAGE = "cepage", TABLE_REGION = "region", TABLE_VIN = "vin";
    public static final String DOMAINE_NOM = "nomdomaine", DOMAINE_ID = "iddomaine", APPELLATION_NOM = "nomappellation", APPELLATION_ID = "idapp", APPELLATION_IDREGION = "idregion", TYPEAPPELLATION_NOM = "nomta", TYPEAPPELLATION_ID = "idta", CEPAGE_NOM = "nomcepage", CEPAGE_ID = "idcepage", REGION_NOM = "nomregion", REGION_ID = "idregion";
    public static final String VIN_ID = "idvin", VIN_COULEUR = "couleur", VIN_IDDOMAINE = "iddomaine", VIN_IDCEPAGE = "idcepage", VIN_IDAPP = "idapp", VIN_DEGRES = "degres", BOUTEILLE_ID = "idbouteille", BOUTEILLE_VOLUME = "volume", BOUTEILLE_IDVIN = "idvin", BOUTEILLE_CODE = "code", BOUTEILLE_IMAGE = "image", BOUTEILLE_MILLESIME = "millesime";
    public static final String UTILISATEUR_EMAIL = "email", UTILISATEUR_ID = "idutilisateur", CASIER_ID="idcasier",CASIER_NOM="nomcasier",CASIER_IDUTILISATEUR="idutilisateur";
    public static final String CONTENIR_IDBOUTEILLE="idbouteille",CONTENIR_IDCASIER="idcasier",CONTENIR_QUANTITE="quantite";

    private QueryUtil() {
    }

    /**
     * Construit une requête SQL Select avec les paramètres donnés de la forme SELECT attr1, attr2, ... FROM table.
     *
     * @param table table à requêter
     * @param attr  colonnes à reprendre
     * @return la requête SQL
     */
    public static String buildSelect(String table, String... attr) {
        String query = "SELECT " + commaConcatenate(attr) + " FROM " + table;

        return query;
    }

    /**
     * Construit une requête SQL Insert avec les paramètres donnés de la forme INSERT INTO table (attr1, attr3, ...) VALUES ('attr2', 'attr4', ...).<br>
     * Les args d'indice impair (le 1er, le 3e...) sont les noms de colonnes, les pairs (le 2e, le 4e...) sont les valeurs.
     *
     * @param table table à requêter
     * @param args  liste alternant entre les noms de colonnes et les valeurs
     * @return null si le nombre d'args est impair, la requête SQL sinon
     */
    public static String buildInsert(String table, String... args) {
        if (args.length % 2 != 0) return null;

        String query = "INSERT INTO " + table + " (";

        //Séparation colonnes et valeurs
        String[] columns = new String[args.length / 2];
        String[] values = new String[args.length / 2];

        for (int i = 0; i < args.length; i++) {
            if (i % 2 == 0) columns[i / 2] = args[i];
            else values[i / 2] = args[i];
        }

        //Colonnes
        query += commaConcatenate(columns) + ") VALUES (";

        //Valeurs
        query += commaConcatenate(quote(values)) + ")";

        return query;
    }

    /**
     * Construit une requête SQL Update avec les paramètres donnés de la forme Update table SET attr1=attr2,attr3=attr4
     * Les args d'indice impair (le 1er, le 3e...) sont les noms de colonnes, les pairs (le 2e, le 4e...) sont les valeurs.
     *
     * @param table table à requêter
     * @param args  liste alternant entre les noms de colonnes et les valeurs
     * @return null si le nombre d'args est impair, la requête SQL sinon
     */
    public static String buildUpdate(String table, String... args) {
        if (args.length % 2 != 0) return null;

        String query = "UPDATE  " + table + " SET ";

        for (int i = 0; i < args.length; i+=2) {
            query += args[i] + " = " + quote(args[i+1]) + ", ";
        }

        query = query.substring(0, query.lastIndexOf(", "));

        return query;
    }
    public static String buildUpdateNoQuote(String table, String... args) {
        if (args.length % 2 != 0) return null;

        String query = "UPDATE  " + table + " SET ";

        for (int i = 0; i < args.length; i+=2) {
            query += args[i] + " = " + args[i+1] + ", ";
        }

        query = query.substring(0, query.lastIndexOf(", "));

        return query;
    }


    public static String buildDeleteId(String table, String idName, int id)
    {
        return "DELETE FROM " + table + " WHERE " + idName + " = " + id;
    }

    /**
     * Prends un tableau de String de taille N et le transforme en chaîne de type tab[0], tab[1], ..., tab[N-2], tab[N].<br>
     * Utilisé pour la construction des requêtes SQL.
     */
    private static String commaConcatenate(String[] tab) {
        String result = "";

        for (int i = 0; i < tab.length - 1; i++) {
            result += tab[i] + ", ";
        }
        result += tab[tab.length - 1];

        return result;
    }

    /**
     * Entoure la chaîne en paramètre de simple quotes.
     *
     * @return une nouvelle chaîne
     */
    private static String quote(String str) {
        return "'" + str + "'";
    }

    /**
     * Entoure chaque chaîne du tableau avec des simple quotes. Le tableau en paramètre est modifié.
     *
     * @return le tableau passé en paramètre, pour simplifier l'écriture
     */
    private static String[] quote(String[] tab) {
        for (int i = 0; i < tab.length; i++) tab[i] = quote(tab[i]);

        return tab;
    }
}