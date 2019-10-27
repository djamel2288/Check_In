package sample.db;

import java.sql.*;

public class db_check_in {
    /*public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            // sample.db parameters
            String url = "jdbc:sqlite:C:/Users/djamel/IdeaProjects/check_in2/check_in2.sqlite ";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        }
        catch (SQLException e) {
            System.out.println("111111111111   "+e.getMessage());
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return conn;
    }

    public static void selectAll(){
        String sql = "SELECT * FROM participants";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("participants_id") +  "\t" +
                        rs.getString("nom") + "\t" +
                        rs.getString("prenom") + "\t" +
                        rs.getString("email") + "\t" +
                        rs.getInt("nbr_presence"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/

    public static Connection connect() throws ClassNotFoundException {
        // SQLite connection string
        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:C:/Users/djamel/IdeaProjects/check_in2/check_in2";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            //System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    /**
     * select all rows in the warehouses table
     */
    public static void selectAll(){
        String sql = "SELECT * FROM participants";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("participants_id") +  "\t" +
                        rs.getString("nom") + "\t" +
                        rs.getString("prenom") + "\t" +
                        rs.getString("email") + "\t" +
                        rs.getInt("nbr_presence"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
