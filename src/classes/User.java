package classes;

import bdd.Connexion;
import bdd.Password;
import util.QueryUtil;

import javax.management.Query;

import static util.QueryUtil.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {
    public static User active;
    private int id;
    private String nom;

    public User(int id, String nom)
    {
        this.id = id;
        this.nom = nom;
    }

    // Ajout d'un nouvel utilisateur dans la bdd
    public static void newUser(Connection con,String pseudo, String nom, String prenom, String date, String mdp, String email, String ville) {
        PreparedStatement pst = null;
        try {

            byte[]salt = Password.getNextSalt();

            String stm = "INSERT INTO Utilisateur(pseudo, nom,prenom,datenaiss,mdp,email,ville,salt) VALUES(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(stm);
            pst.setString(1, pseudo);
            pst.setString(2, nom);
            pst.setString(3, prenom);
            pst.setDate(4, Date.valueOf(date));
            pst.setBytes(5, Password.hash(mdp,salt));
            pst.setString(6, email);
            pst.setString(7, ville);
            pst.setBytes(8, salt);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(User.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    // Regarde dans la base de données si le login / mdp est correct
    public static boolean login(String email,String mdp) throws SQLException {
        Connection con = Connexion.connexionBDD();
        PreparedStatement pst = Objects.requireNonNull(con).prepareStatement(buildSelect(TABLE_UTILISATEUR,"count(*) as c")+" where " + UTILISATEUR_EMAIL + " = '" + email + "'");
        ResultSet rs=pst.executeQuery();
        rs.next();
        if(rs.getInt("c")>0)
        {
            //pst = con.prepareStatement("SELECT mdp,salt from utilisateur WHERE email='"+email+"'");
            pst = con.prepareStatement(QueryUtil.buildSelect(TABLE_UTILISATEUR, "mdp", "salt", UTILISATEUR_ID, "nom") + " WHERE email='"+email+"'");
            rs = null;
            rs=pst.executeQuery();
            rs.next();

            if (Password.isExpectedPassword(mdp,rs.getBytes(2),rs.getBytes(1)))
            {
                active = new User(rs.getInt(UTILISATEUR_ID), rs.getString("nom"));
                return true;
            }
            else return false;
        }
        else return false;
    }

    public int getId()
    {
        return id;
    }

    public String getNom()
    {
        return nom;
    }

    public List<Locker> loadLockers() throws SQLException, IOException {
        List<Locker> list = new ArrayList<>();
        String sql = buildSelect(TABLE_CASIER,CASIER_ID,CASIER_NOM) + " Where idutilisateur = '" + id + "'";
        Connexion con = new Connexion();
        ResultSet rs = con.executeStatement(sql);
        while(rs.next()){
            Locker l = new Locker(rs.getInt(1),rs.getString(2),this);
            list.add(l);
//            l.loadBottles();
        }

        return list;
    }

}
