package com.mycompany.web.template.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.mycompany.web.template.commons.Tool;
import com.mycompany.web.template.db.DBPool;
import com.mycompany.web.template.model.UserActionLog;
import com.mycompany.web.template.IF.UserActionIF;

@Repository
public class UserActionDao implements UserActionIF {

    static Logger logger = Logger.getLogger(UserActionDao.class);

    @Override
    public UserActionLog findById(int id) {
        UserActionLog result = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM USER_ACTION_LOG WHERE ID = ?";
        try {
            int i = 1;
            conn = DBPool.getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(i++, id);
            rs = pstm.executeQuery();
            if (rs.next()) {
                result = new UserActionLog();
                result.setId(rs.getInt("ID"));
                result.setUserName(rs.getString("USER_NAME"));
                result.setUserIp(rs.getString("USER_IP"));
                result.setUrlAction(rs.getString("URL_ACTION"));
                result.setTableAction(rs.getString("TABLE_ACTION"));
                result.setIdAction(rs.getInt("ID_ACTION"));
                result.setActionType(rs.getString("ACTION_TYPE"));
                result.setActionDate(rs.getLong("ACTION_DATE"));
                result.setResult(rs.getString("RESULT"));
                result.setInfo(rs.getString("INFO"));
                result.setLastObject(rs.getString("LAST_OBJECT"));
                result.setClassObject(rs.getString("CLASS_OBJECT"));
            }
        } catch (SQLException e) {
            logger.error(Tool.getLogMessage(e));
        } finally {
            DBPool.freeConn(rs, pstm, conn);
        }
        return result;
    }

    @Override
    public ArrayList<UserActionLog> view(int page, int maxRow, String user_name, String table_action, String action_type, String result_status) {
        ArrayList<UserActionLog> all = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM USER_ACTION_LOG WHERE 1 = 1 ";
        try {
            if (!Tool.checkNull(user_name)) {
                sql += " AND USER_NAME like ?";
            }
            if (!Tool.checkNull(table_action)) {
                sql += " AND TABLE_ACTION like ?";
            }
            if (!Tool.checkNull(action_type)) {
                sql += " AND ACTION_TYPE like ?";
            }
            if (!Tool.checkNull(result_status)) {
                sql += " AND RESULT like ?";
            }
            sql += " ORDER BY ID DESC LIMIT ?,?";
            int i = 1;
            conn = DBPool.getConnection();
            pstm = conn.prepareStatement(sql);
            if (!Tool.checkNull(user_name)) {
                pstm.setString(i++, "%" + user_name + "%");
            }
            if (!Tool.checkNull(table_action)) {
                pstm.setString(i++, "%" + table_action + "%");
            }
            if (!Tool.checkNull(action_type)) {
                pstm.setString(i++, "%" + action_type + "%");
            }
            if (!Tool.checkNull(result_status)) {
                pstm.setString(i++, "%" + result_status + "%");
            }
            pstm.setInt(i++, (page - 1) * maxRow);
            pstm.setInt(i++, maxRow);
            rs = pstm.executeQuery();
            while (rs.next()) {
                UserActionLog one = new UserActionLog();
                one.setId(rs.getInt("ID"));
                one.setUserName(rs.getString("USER_NAME"));
                one.setUserIp(rs.getString("USER_IP"));
                one.setUrlAction(rs.getString("URL_ACTION"));
                one.setTableAction(rs.getString("TABLE_ACTION"));
                one.setIdAction(rs.getInt("ID_ACTION"));
                one.setActionType(rs.getString("ACTION_TYPE"));
                one.setDateView(rs.getString("ACTION_DATE"));
                one.setActionDate(rs.getLong("ACTION_DATE"));
                one.setResult(rs.getString("RESULT"));
                one.setInfo(rs.getString("INFO"));
                one.setLastObject(rs.getString("LAST_OBJECT"));
                one.setClassObject(rs.getString("CLASS_OBJECT"));
                all.add(one);
            }
        } catch (SQLException e) {
            logger.error(Tool.getLogMessage(e));
            Tool.out(e.getMessage());
        } finally {
            DBPool.freeConn(rs, pstm, conn);
        }
        return all;
    }

    @Override
    public int count(String user_name, String table_action, String action_type, String result_status) {
        int all = 0;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM USER_ACTION_LOG WHERE 1=1";
        try {
            if (!Tool.checkNull(user_name)) {
                sql += " AND USER_NAME like ?";
            }
            if (!Tool.checkNull(table_action)) {
                sql += " AND TABLE_ACTION like ?";
            }
            if (!Tool.checkNull(action_type)) {
                sql += " AND ACTION_TYPE like ?";
            }
            if (!Tool.checkNull(result_status)) {
                sql += " AND RESULT like ?";
            }
            int i = 1;
            conn = DBPool.getConnection();
            pstm = conn.prepareStatement(sql);
            if (!Tool.checkNull(user_name)) {
                pstm.setString(i++, "%" + user_name + "%");
            }
            if (!Tool.checkNull(table_action)) {
                pstm.setString(i++, "%" + table_action + "%");
            }
            if (!Tool.checkNull(action_type)) {
                pstm.setString(i++, "%" + action_type + "%");
            }
            if (!Tool.checkNull(result_status)) {
                pstm.setString(i++, "%" + result_status + "%");
            }
            rs = pstm.executeQuery();
            if (rs.next()) {
                all = rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(Tool.getLogMessage(e));
        } finally {
            DBPool.freeConn(rs, pstm, conn);
        }

        return all;
    }
}
