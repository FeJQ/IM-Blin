package common;

import lombok.Getter;
import lombok.Setter;
import util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbResult
{
    public DbResult(){}
    public DbResult(ResultSet resultSet, PreparedStatement prestmt, Connection conn)
    {
        this.resultSet=resultSet;
        this.preStmt=prestmt;
        this.conn=conn;
    }
    @Getter
    private ResultSet resultSet;
    private PreparedStatement preStmt;
    private Connection conn;
    public void close()
    {
        DBHelper.closeResource(resultSet,preStmt,conn);
    }
}
