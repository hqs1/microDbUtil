package Utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Driver;

public class DBmysql {
    private Connection connection = null;
    public DBmysql(String url, String username, String password) {
        try {
            Driver driver = new Driver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (connection != null) {
            System.out.println("数据连接成功");
        } else {
            System.out.println("数据连接失败,请检查数据库信息是否正常");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void Close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> select(Class<T> cls) {
        List<T> list = null;
        try {
            Statement st = connection.createStatement();
            ResultSet rt = st.executeQuery("SELECT * FROM " + cls.getSimpleName().toLowerCase() + ";");
            list = ResultSetUtils.resultSetToList(rt, cls);
            rt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public <T> List<T> select(Class<T> cls, Map<T,T> map) {
        List<T> list = null;
        String sql = "SELECT * FROM " + cls.getSimpleName().toLowerCase();

        if (map.size() > 0)
            sql += " where";

        for (T key : map.keySet()) {
            sql += " "+key+" ? AND";
        }

        if (map.size() > 0)
            sql = sql.substring(0, sql.length() - 3);

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            int count = 0;
            for (T value : map.values()) {
                ps.setObject(++count,value);
            }
//            System.out.println(ps.toString());
            ResultSet rt = ps.executeQuery();
            list = ResultSetUtils.resultSetToList(rt, cls);
            rt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(Class cls, Object obj) {
        int value = 0;
        String sql = "insert into " + cls.getSimpleName().toLowerCase() + "(";
        try {
            Field[] fields = cls.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                sql += fields[i].getName() + ",";
            }
            //删掉最后一个,
            sql = sql.substring(0, sql.length() - 1);
            sql += ") values (";
            //添加值问号
            for (int i = 0; i < fields.length; i++) {
                sql += "?,";
            }
            //删掉最后一个,
            sql = sql.substring(0, sql.length() - 1);
            sql += ");";
            PreparedStatement ps = connection.prepareStatement(sql);
            //赋值
            for (int i = 0; i < fields.length; i++) {
                ps.setObject(i + 1, fields[i].get(obj));
            }
//            System.out.println(ps.toString());
            value = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public  int update(Class cls, Object obj, String key) {
        int value = 0;
        Object wobj = null;
        String sql = "update " + cls.getSimpleName().toLowerCase() + " set ";
        try {
            Field[] fields = cls.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (!fields[i].getName().equals(key))
                    sql += fields[i].getName() + " = ?,";
            }
            //删掉最后一个","
            sql = sql.substring(0, sql.length() - 1);
            sql += " where " + key + " = ? ;";
            PreparedStatement ps = connection.prepareStatement(sql);
            //赋值
            int j = 0;
            for (int i = 0; i < fields.length; i++) {
                if (!fields[i].getName().equals(key)) {
                    ps.setObject(++j, fields[i].get(obj));
                } else
                    wobj = fields[i].get(obj);
            }
            if (wobj == null) {
                return -1;
            }
            //where条件赋值
            ps.setObject(fields.length, wobj);
//            System.out.println(ps.toString());
            value = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public <T> int delete(Class cls, Map<T,T> map){
        int value = 0;
        String sql = "delete from " + cls.getSimpleName().toLowerCase();

        if (map.size() > 0)
            sql += " where";

        for (T key : map.keySet()) {
            sql += " "+key+" ? AND";
        }

        if (map.size() > 0)
            sql = sql.substring(0, sql.length() - 3);

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            int count = 0;
            for (T data : map.values()) {
                ps.setObject(++count,data);
            }
//            System.out.println(ps.toString());
            value = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
