package util;

import common.DbResult;



import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * JDBC数据库操作公共类
 *
 * @author pan_junbiao
 */
public class DBHelper
{
    private static String DRIVER_CLASS; // 数据库驱动
    private static String DB_URL; // 数据库连接地址
    private static String DB_USER; // 数据库用户名称
    private static String DB_PASSWORD;// 数据库用户密码

    /**
     * 静态代码块加载配置文件信息与数据库驱动类
     */
    static
    {
        try
        {
            // 创建Properties类对象
            Properties properties = new Properties();

            // 读取db.properties属性文件到输入流中
            InputStream is = DBHelper.class.getResourceAsStream("/Database.properties");

            // 从输入流中加载属性列表
            properties.load(is);

            // 获取数据库连接属性值
            DRIVER_CLASS = properties.getProperty("DRIVER_CLASS");
            DB_URL = properties.getProperty("DB_URL");
            DB_USER = properties.getProperty("DB_USER");
            DB_PASSWORD = properties.getProperty("DB_PASSWORD");

            // 加载数据库驱动类
            Class.forName(DRIVER_CLASS);

        }
        catch (ClassNotFoundException cnfe)
        {
            cnfe.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接对象
     */
    private static Connection getConnection()
    {
        Connection conn = null;

        try
        {
            // 获取数据库连接对象
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }

        return conn;
    }



    /**
     * 查询列表
     *
     * @param sql 查询SQL语句
     * @return 结果集
     */
    public static DbResult executeQuery(String sql)
    {
        Connection conn = null;
        PreparedStatement preStmt = null;
        ResultSet res = null;
        DbResult dbResult=null;
        try
        {
            conn = getConnection();
            preStmt = conn.prepareStatement(sql);
            res = preStmt.executeQuery();
            dbResult =new DbResult(res,preStmt,conn);

        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        return dbResult;
    }

    /**
     * 查询列表
     *
     * @param sql    查询SQL语句
     * @param params 参数集合
     * @return 结果集
     */
    public static DbResult executeQuery(String sql, List<Object> params)
    {
        Connection conn = null;
        PreparedStatement preStmt = null;
        ResultSet res = null;
        DbResult dbResult=null;
        try
        {
            conn = getConnection();
            preStmt = conn.prepareStatement(sql);
            setParams(preStmt, params);
            res = preStmt.executeQuery();
            dbResult=new DbResult(res,preStmt,conn);
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();

        }
        return dbResult;
    }

    /**
     * 执行操作
     *
     * @param sql 执行SQL语句
     * @return 受影响条数
     */
    public static int executeOperate(String sql)
    {
        int count = 0;
        Connection conn = null;
        PreparedStatement preStmt = null;

        try
        {
            conn = getConnection();
            preStmt = conn.prepareStatement(sql);
            count = preStmt.executeUpdate();

        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        finally
        {
            closeResource(preStmt, conn);
        }

        return count;
    }

    public static int executeOperate(String sql, List<Object> params)
    {
        return executeOperate(sql, params, false);
    }

    /**
     * 执行操作
     *
     * @param sql              执行SQL语句
     * @param params           参数集合
     * @param returnPrimaryKey 是否需要接收生成数据的主键
     * @return 受影响条数
     */
    public static int executeOperate(String sql, List<Object> params, boolean returnPrimaryKey)
    {
        int result = 0;
        Connection conn = null;
        PreparedStatement preStmt = null;
        try
        {
            conn = getConnection();
            if (returnPrimaryKey)
            {
                preStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            }
            else
            {
                preStmt = conn.prepareStatement(sql);
            }

            setParams(preStmt, params);
            int temp = preStmt.executeUpdate();
            if (returnPrimaryKey)
            {
                ResultSet generatedKeys = preStmt.getGeneratedKeys();
                if (generatedKeys.next())
                {
                    int id = generatedKeys.getInt(1);
                    result = id;
                }
            }
            else
            {
                result = temp;
            }
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        finally
        {
            closeResource(preStmt, conn);
        }
        return result;
    }

    /**
     * 执行返回单个值
     *
     * @param sql 执行SQL语句
     * @return 单个值
     */
    public static Object executeScalar(String sql)
    {
        Object resObj = null;
        Connection conn = null;
        PreparedStatement preStmt = null;
        ResultSet res = null;

        try
        {
            conn = getConnection();
            preStmt = conn.prepareStatement(sql);
            res = preStmt.executeQuery();
            if (res.next())
            {
                resObj = res.getObject(1);
            }

        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        finally
        {
            closeResource(res, preStmt, conn);
        }

        return resObj;
    }

    /**
     * 执行返回单个值
     *
     * @param sql    执行SQL语句
     * @param params 参数集合
     * @return 单个值
     */
    public static Object executeScalar(String sql, List<Object> params)
    {
        Object resObj = null;
        Connection conn = null;
        PreparedStatement preStmt = null;
        ResultSet res = null;

        try
        {
            conn = getConnection();
            preStmt = conn.prepareStatement(sql);
            setParams(preStmt, params);
            res = preStmt.executeQuery();
            if (res.next())
            {
                resObj = res.getObject(1);
            }

        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        finally
        {
            closeResource(res, preStmt, conn);
        }

        return resObj;
    }

    /**
     * 释放资源
     *
     * @param res ResultSet对象
     */
    public static void closeResource(ResultSet res)
    {
        try
        {
            // 关闭ResultSet对象
            if (res != null)
            {
                res.close();
            }

        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
    }

    /**
     * 释放资源
     *
     * @param stmt Statement对象
     * @param conn Connection对象
     */
    public static void closeResource(Statement stmt, Connection conn)
    {
        try
        {
            // 关闭Statement对象
            if (stmt != null)
            {
                stmt.close();
            }

            // 关闭Connection对象
            if (conn != null)
            {
                conn.close();
            }

        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
    }

    /**
     * 释放资源
     *
     * @param res  ResultSet对象
     * @param stmt Statement对象
     * @param conn Connection对象
     */
    public static void closeResource(ResultSet res, Statement stmt, Connection conn)
    {
        try
        {
            // 关闭ResultSet对象
            if (res != null)
            {
                res.close();
            }

            // 关闭Statement对象
            if (stmt != null)
            {
                stmt.close();
            }

            // 关闭Connection对象
            if (conn != null)
            {
                conn.close();
            }

        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
    }

    /**
     * 设置预处理的参数
     *
     * @param preStmt 预处理
     * @param params  参数集合
     * @throws SQLException
     */
    public static void setParams(PreparedStatement preStmt, List<Object> params) throws SQLException
    {
        if (params != null && params.size() > 0)
        {
            for (int i = 0; i < params.size(); i++)
            {
                preStmt.setObject(i + 1, params.get(i));
            }
        }
    }
}