package service;

import entity.Servers;

import java.sql.Connection;
import java.util.List;

/**
 * 数据库的业务逻辑接口
 */
public interface DatabaseDAO {
	
	//连接SQL Server数据库
	public boolean connSqlServer(Servers s);
	
	//备份单个数据库
	public boolean backUpSingleDatabase(String databaseName);
	
	//备份单个表
	public boolean backUpSingleTable(String tableNmae);
	
	//恢复数据库
	public boolean dataReduction(String name);

    //列出server下所有的数据库
    public List listServerAllDatabase(Servers s);

	public Connection getSqlServerConnection(Servers s);
}
