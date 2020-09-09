package com.lagou.edu.service.impl;

import com.lagou.edu.annotation.Autowire;
import com.lagou.edu.annotation.Service;
import com.lagou.edu.annotation.TransactionManager;
import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.pojo.Account;
import com.lagou.edu.service.TransferService;


/**
 * @author 应癫
 */
@Service("transferService")
@TransactionManager
public class TransferServiceImpl implements TransferService {
    @Autowire
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

        Account from = accountDao.queryAccountByCardNo(fromCardNo);
        Account to = accountDao.queryAccountByCardNo(toCardNo);

        from.setMoney(from.getMoney()-money);
        to.setMoney(to.getMoney()+money);

        int c = 1/0;
        accountDao.updateAccountByCardNo(to);
        accountDao.updateAccountByCardNo(from);


    }
}
