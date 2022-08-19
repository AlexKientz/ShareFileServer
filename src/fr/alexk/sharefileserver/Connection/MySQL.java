package fr.alexk.sharefileserver.Connection;


import fr.alexk.sharefileserver.utils.User;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQL {
    
    private Connection connection;
    public String host, database, username, password;

    public String nameUser, passwordUser;

    public Map<String, User> userTemporaly = new HashMap<>();
    public int port;
    public void mysqlSetup() {

        try {
            synchronized (this){
                if(getConnection() != null && !getConnection().isClosed()){
                    return;
                }
                Class.forName("com.mysql.cj.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/sharefile", "user", "Ludovic03"));
                System.out.println("MySQL Connected");
            }
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void getData(String name){
        Connection con = getConnection();
        try {
            System.out.println(name);
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM user WHERE name = ?");
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                userTemporaly.put(name, new User(rs.getString("name"), rs.getString("password")));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

}
