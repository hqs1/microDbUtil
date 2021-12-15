package Utils;

import Model.User;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DBmysqlTest {
    static String datebase = "librarymanagement";
    static String url = "jdbc:mysql://localhost:3306/" + datebase + "?useUnicode=true&characterEncoding=utf-8";
    static String username = "root";
    static String password = "";
    DBmysql dBmysql = new DBmysql(url,username,password);
    @Test
    public void select() {
        //无条件查询
        List<User> list = dBmysql.select(User.class);
        list.forEach(obj->System.out.println(obj.toString()));
    }

    @Test
    public void testSelect() {
        //条件查询
        Map map = new HashMap();
        map.put("username = ","root");
        List<User> list = dBmysql.select(User.class,map);
        list.forEach(obj->System.out.println(obj.toString()));
    }

    @Test
    public void insert() {
        //插入数据
        User user = new User();
        user.setUsername("xiaoming");
        user.setPassword("123456");
        int upDateCount = dBmysql.insert(User.class,user);
        System.out.println("更新"+upDateCount+"行");
    }

    @Test
    public void update() {
        /* 更新数据 */
        //1.获取数据
        User user = null;
        Map map = new HashMap();
        map.put("username = ","xiaoming");
        user = (User) dBmysql.select(User.class,map).get(0);
        //2.修改数据
        user.setPassword("123");
        //3.更新数据
        int upDateCount = dBmysql.update(User.class,user,"username");
        System.out.println("更新"+upDateCount+"行");
    }

    @Test
    public void delete() {
        /* 删除数据 */
        //1.添加条件
        Map map = new HashMap();
        map.put("username = ","xiaoming");
        //2.删除数据
        int upDateCount = dBmysql.delete(User.class,map);
        System.out.println("更新"+upDateCount+"行");
    }
}