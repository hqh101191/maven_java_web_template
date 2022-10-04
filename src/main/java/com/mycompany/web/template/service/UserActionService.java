package com.mycompany.web.template.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mycompany.web.template.model.UserActionLog;
import com.mycompany.web.template.IF.UserActionIF;

@Service
public class UserActionService implements UserActionIF {

    @Autowired
    UserActionIF userAction;

    @Override
    public ArrayList<UserActionLog> view(int page, int maxRow, String user_name, String table_action, String action_type, String result_status) {
        return userAction.view(page, maxRow, user_name, table_action, action_type, result_status);
    }

    @Override
    public int count(String user_name, String table_action, String action_type, String result_status) {
        return userAction.count(user_name, table_action, action_type, result_status);
    }

    @Override
    public UserActionLog findById(int id) {
        return userAction.findById(id);
    }
}
