package util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class ResultSetUtil
{
    public static <T> List<T> toList(ResultSet resultSet, Class<T> clazz) {
        //创建一个 T 类型的数组
        List<T> list = new ArrayList<>();
        try {
            //通过反射获取对象的实例
            T t = clazz.getConstructor().newInstance();
            //获取resultSet 的列的信息
            ResultSetMetaData metaData = resultSet.getMetaData();
            //遍历resultSet
            while (resultSet.next()) {
                //遍历每一列
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    //获取列的名字
                    String fName = metaData.getColumnLabel(i + 1);
                    //因为列的名字和我们EMP中的属性名是一样的，所以通过列的名字获得其EMP中属性
                    Field field = clazz.getDeclaredField(fName);
                    field.setAccessible(true);
                    field.set(t,resultSet.getObject(fName));
                }
                //把赋值后的对象 加入到list集合中
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回list
        return list;
    }
}
