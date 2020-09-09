package com.lagou.edu.utils;


import com.lagou.edu.annotation.Service;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @authorAdministrator
 * @date 2020/9/120:43
 * @description
 */
//线程和连接池关联起来
@Service
public class CollentionUtils {

    private ThreadLocal<java.sql.Connection> threadLocal = new ThreadLocal<>();

    public Connection getConnectFromThread() throws SQLException {
        Connection connection = threadLocal.get();
        if(connection==null){
            connection=DruidUtils.getInstance().getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }

}
