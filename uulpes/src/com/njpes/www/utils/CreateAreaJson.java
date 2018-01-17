package com.njpes.www.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * 将数据库中得newarea生成json格式的字符串， 到省市区为常量，而乡镇因为变动很大故使用service方式生成
 * 如果区县有变化，执行这个文件，将打印出来的内容复制到js/city.min.js中即可
 * 
 * @author 赵忠诚
 */
public class CreateAreaJson {

    private static String url = "jdbc:mysql://localhost:3306/njproject?useUnicode=true&amp;characterEncoding=UTF-8";
    // system为登陆oracle数据库的用户名
    private static String user = "root";
    // manager为用户名system的密码
    private static String password = "zhao1021";

    // 连接数据库的方法
    public static Connection getConnection() {
        try {
            // 初始化驱动包
            Class.forName("com.mysql.jdbc.Driver");
            // 根据数据库连接字符，名称，密码给conn赋值
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String argv[]) {
        Connection conn = getConnection();

        try {
            Statement st = conn.createStatement();

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            ArrayNode arraynode = mapper.createArrayNode();

            String sqlcity = "";
            String level = "2";
            ResultSet rs = st.executeQuery("SELECT code,name FROM newarea WHERE level='1'");
            while (rs.next()) {
                ObjectNode pnode = mapper.createObjectNode();
                pnode.put("p", rs.getString("name"));
                pnode.put("code", rs.getString("code"));
                level = "2";
                /*
                 * if(rs.getString("code").equals("11") ||
                 * rs.getString("code").equals("12") ||
                 * rs.getString("code").equals("31")
                 * ||rs.getString("code").equals("50")){ level = "3"; }
                 */

                sqlcity = "select code,name from newarea where level='" + level + "' and code like '"
                        + rs.getString("code") + "%'";
                Connection conn2 = getConnection();
                Statement st2 = conn2.createStatement();
                ResultSet cityrs = st2.executeQuery(sqlcity);
                ArrayNode cityarraynode = mapper.createArrayNode();
                while (cityrs.next()) {
                    ObjectNode cnode = mapper.createObjectNode();
                    cnode.put("n", cityrs.getString("name"));
                    cnode.put("code", cityrs.getString("code"));

                    if (level.equals("2")) {
                        ArrayNode aarraynode = mapper.createArrayNode();
                        Connection conn3 = getConnection();
                        Statement st3 = conn3.createStatement();
                        ResultSet ars = st3.executeQuery("select code,name from newarea where level='3' and code like '"
                                + cityrs.getString("code") + "%'");

                        while (ars.next()) {
                            ObjectNode anode = mapper.createObjectNode();
                            anode.put("s", ars.getString("name"));
                            anode.put("code", ars.getString("code"));
                            aarraynode.add(anode);
                        }
                        cnode.put("a", aarraynode);
                        st3.close();
                        conn3.close();
                    }
                    cityarraynode.add(cnode);
                }
                st2.close();
                conn2.close();
                pnode.put("c", cityarraynode);

                arraynode.add(pnode);

            }
            st.close();
            conn.close();
            root.put("citylist", arraynode);
            System.out.println(mapper.writeValueAsString(root));
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
