/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.template.IF;

import java.util.ArrayList;
import com.mycompany.web.template.model.Account;

/**
 *
 * @author Private
 */
public interface AccountIF extends BasicIF<Account> {

    public ArrayList<Account> view(int page, int maxRow, String key, String phone, String email, int status, boolean isdel);

    public int count(String key, String phone, String email, int status, boolean isdel);

    public Account checkLoginDB(String user, String pass);

    public Account undoDelete(int accID);

    public Account deleteForEver(int accID);

    public boolean existsByUsername(String username);

}
