package com.lagou.edu.utils;

import com.lagou.edu.annotation.Autowire;
import com.lagou.edu.annotation.Service;

import java.sql.SQLException;

/**
 * @authorAdministrator
 * @date 2020/9/121:50
 * @description
 */
@Service
public class TransactionManagerUtils {
    @Autowire
    private CollentionUtils collentionUtils;

    public void setCommit(boolean flag) throws SQLException {
        collentionUtils.getConnectFromThread().setAutoCommit(flag);
    }

    public void commit() throws SQLException {
        collentionUtils.getConnectFromThread().commit();
    }

    public void rollBack() throws SQLException {
        collentionUtils.getConnectFromThread().rollback();
    }
}
