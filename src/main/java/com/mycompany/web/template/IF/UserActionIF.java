package com.mycompany.web.template.IF;

import java.util.ArrayList;
import com.mycompany.web.template.model.UserActionLog;

public interface UserActionIF {

    public ArrayList<UserActionLog> view(int page, int maxRow, String user_name, String table_action, String action_type, String result_status);

    public int count(String user_name, String table_action, String action_type, String result_status);

    public UserActionLog findById(int id);
}
