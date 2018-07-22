
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JOTIBA
 */
import java.sql.*;
import javax.swing.*;
class javaconnect 
{
    
    Connection conn = null;
    public static Connection ConnecrDb()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn =(Connection) DriverManager.getConnection("jdbc:sqlite:3DP.sqlite");
         //   JOptionPane.showMessageDialog(null, "Checking into Database.......Please Wait!");
                    return conn;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
    
    
}
