package memo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnector{

    private Connection cn;


    // Oracleユーザー名とパスワードを固定で指定
    private static final String USER = "xxxxx";
    private static final String PASSWORD = "xxxxx";

    public OracleConnector(){

        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");

            cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", USER, PASSWORD);
            System.out.println("接続完了");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Connection getCn(){
        return cn;
    }
}