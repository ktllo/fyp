/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp.tmp;

import java.sql.*;
import java.io.*;
/**
 *
 * @author leolo
 */
public class DataFetcher {
    public static void main(String [] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, FileNotFoundException{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/label", "root", "rootpass");
        ResultSet result = conn.createStatement().executeQuery("SELECT * FROM `comment` WHERE sid IN(SELECT sid FROM `comment` GROUP BY sid HAVING COUNT(*) = 30);");
        //result.first();
        
        while(result.next()){
            System.out.println("[INFO]cid="+result.getString("id"));
            String dest = "output/"+result.getString("sid")+"-"+result.getString("id")+".txt";
            PrintWriter out = new PrintWriter(dest);
            System.out.println("[INFO]Output to "+dest);
            out.print(result.getString("content"));
            out.flush();
            out.close();
        }
        result.close();
        conn.close();
    }
}
