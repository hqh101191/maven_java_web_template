/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.template.service;

import com.mycompany.web.template.IF.AccountIF;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mycompany.web.template.commons.Tool;
import com.mycompany.web.template.config.MyConfig;
import com.mycompany.web.template.model.Account;
import com.mycompany.web.template.dao.AccountDao;

/**
 *
 * @author Private
 */
@Service
@Transactional
public class AccountService implements AccountIF {

    private static final Logger logger = Logger.getLogger(AccountService.class);

    @Autowired
    AccountDao accDao;

    static String[] USER_BLACK_LIST = {"admin",
        "administrator",
        "moderator",
        "mode",
        "quantri",};

    public static boolean isBlackList(String user) {
        boolean result = false;
        for (String one : USER_BLACK_LIST) {
            if (user.startsWith(one)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public ArrayList<Account> view(int page, int maxRow, String key, String phone, String email, int status, boolean isdel) {
        return accDao.view(page, maxRow, key, phone, email, status, isdel);
    }

    @Override
    public int count(String key, String phone, String email, int status, boolean isdel) {
        return accDao.count(key, phone, email, status, isdel);
    }

    public Account getAccountLogin(HttpSession session) {
        Account acc = null;
        try {
            acc = (Account) session.getAttribute(MyConfig.USER_SESSION_NAME);
        } catch (Exception e) {
            logger.error(Tool.getLogMessage(e));
            Tool.out(e.getMessage());
        }
        return acc;
    }

    public String getUserName(HttpServletRequest request) {
        Account acc = getAccountLogin(request);
        if (acc != null) {
            return acc.getUsername();
        } else {
            return "Unknow";
        }
    }

    public Account getAccountLogin(HttpServletRequest request) {
        Account acc = null;
        try {
            HttpSession session = request.getSession(false);
            acc = (Account) session.getAttribute(MyConfig.USER_SESSION_NAME);
        } catch (Exception e) {
            logger.error(Tool.getLogMessage(e));
            Tool.out(e.getMessage());
        }
        return acc;
    }

    @Override
    public Account findById(int accID) {
        return accDao.findById(accID);
    }

    @Override
    public Account checkLoginDB(String userName, String password) {
        Account account = accDao.checkLoginDB(userName, password);
        if (account != null) {
        }
        return account;
    }

    @Override
    public int create(Account oneAcc) {
        if (isBlackList(oneAcc.getUsername())) {
            return -1;
        }
        return accDao.create(oneAcc);
    }

    @Override
    public Account update(Account accUpdate) {
        return accDao.update(accUpdate);
    }

    @Override
    public Account delete(int accID) {
        return accDao.delete(accID);
    }

    @Override
    public Account deleteForEver(int accID) {
        return accDao.deleteForEver(accID);
    }

    @Override
    public Account undoDelete(int accID) {
        return accDao.undoDelete(accID);
    }

    public HashMap<String, Boolean> checkRight(HttpServletRequest request) {
        HashMap<String, Boolean> hasRole = new HashMap<>();
        Account account = getAccountLogin(request);
        if (account != null) {
        }
        return hasRole;
    }

    public boolean checkAccess(HttpServletRequest request) {
        boolean right = false;
        Account account = getAccountLogin(request);
        if (account != null) {
            right = true;
        }
        return right;
    }

    @Override
    public boolean existsByUsername(String username) {
        return accDao.existsByUsername(username);
    }

    public boolean checkLogin(HttpServletRequest request) {
        boolean right = false;
        Account account = getAccountLogin(request);
        if (account != null) {
            right = true;
        }
        return right;
    }

}
