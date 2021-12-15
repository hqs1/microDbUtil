package Utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResultSetUtils {
    //结果集转List
    public static <T> List<T> resultSetToList(ResultSet rs, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        Object obj = null;
        try {
            while (rs.next()) {
                obj = cls.getDeclaredConstructor().newInstance();
                Field[] fields = cls.getDeclaredFields();
                for (Field fd : fields) {
                    fd.setAccessible(true);
                    fd.set(obj, rs.getObject(fd.getName()));
                }
                list.add((T) obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //结果集转实体
    public static <T> Object resultSetToBean(ResultSet rs, Class<T> cls) {
        Object obj = null;
        try {
            while (rs.next()) {
                obj = cls.getDeclaredConstructor().newInstance();
                Field[] fields = cls.getDeclaredFields();
                for (Field fd : fields) {
                    fd.setAccessible(true);
                    fd.set(obj, rs.getObject(fd.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}