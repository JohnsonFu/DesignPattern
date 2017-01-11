package com.edu.fulinhua.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by fulinhua on 2017/1/11.
 */
public class show {
    public static void main(String[] args) {
        try{
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");

            String url="jdbc:mysql://localhost:3306/newtable";    //JDBC的URL
            Connection conn;

            conn = DriverManager.getConnection(url,    "root","root");
            Statement stmt = conn.createStatement(); //创建Statement对象
            System.out.println("成功连接到数据库！");

            String sql = "select * from grade where course='嵌入式系统概论' order by point desc";    //要执行的SQL
            ResultSet rs = stmt.executeQuery(sql);//创建数据对象
          //  System.out.println("编号"+"\t"+"姓名"+"\t"+"年龄");
            while (rs.next()){

                System.out.println(rs.getString("name")+" "+rs.getDouble("point"));
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
