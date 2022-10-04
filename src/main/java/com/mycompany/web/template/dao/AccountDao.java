/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.template.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.mycompany.web.template.commons.Md5;
import com.mycompany.web.template.commons.Tool;
import com.mycompany.web.template.config.MyConfig;
import com.mycompany.web.template.db.DBPool;
import com.mycompany.web.template.model.Account;
import com.mycompany.web.template.IF.AccountIF;
import com.mycompany.web.template.commons.DateProc;

/**
 *
 * @author Private
 */
@Repository
public class AccountDao implements AccountIF {

    private static Logger logger = Logger.getLogger(AccountDao.class);

    @Override
    public ArrayList<Account> view(int page, int maxRow, String key, String phone, String email, int status, boolean isDel) {
        ArrayList<Account> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM account WHERE ISDEL = ? ";
        if (!Tool.checkNull(key)) {
            sql += " AND (USERNAME LIKE ? OR FULLNAME LIKE ?) ";
        }
        if (!Tool.checkNull(phone)) {
            sql += " AND PHONE LIKE ? ";
        }
        if (!Tool.checkNull(email)) {
            sql += " AND EMAIL LIKE ? ";
        }
        if (status != MyConfig.STATUS.ALL.val) {
            sql += " AND STATUS = ? ";
        }
        sql += " ORDER BY ID DESC LIMIT ?,?";
        try {
            conn = DBPool.getConnection();
            pstm = conn.prepareStatement(sql);
            int i = 1;
            pstm.setBoolean(i++, isDel);
            if (!Tool.checkNull(key)) {
                pstm.setString(i++, "%" + key + "%");
                pstm.setString(i++, "%" + key + "%");
            }
            if (!Tool.checkNull(phone)) {
                pstm.setString(i++, "%" + phone + "%");
            }
            if (!Tool.checkNull(email)) {
                pstm.setString(i++, "%" + email + "%");
            }
            if (status != MyConfig.STATUS.ALL.val) {
                pstm.setInt(i++, status);
            }
            pstm.setInt(i++, (page - 1) * maxRow);
            pstm.setInt(i++, maxRow);
            rs = pstm.executeQuery();
            while (rs.next()) {
                Account one = new Account();
                one.setId(rs.getInt("id"));
                one.setUsername(rs.getString("USERNAME"));
                one.setPassword(rs.getString("PASSWORD"));
                one.setFullname(rs.getString("FULLNAME"));
                one.setAddress(rs.getString("ADDRESS"));
                one.setPhone(rs.getString("PHONE"));
                one.setEmail(rs.getString("EMAIL"));
                one.setCreatedAt(rs.getLong("CREATEDAT"));
                one.setCreatedBy(rs.getString("CREATEDBY"));
                one.setUpdatedAt(rs.getLong("UPDATEDAT"));
                one.setUpdatedBy(rs.getString("UPDATEDBY"));
                one.setStatus(rs.getInt("STATUS"));
                one.setDel(rs.getBoolean("ISDEL"));
                one.setDesc(rs.getString("DESC"));
                result.add(one);
            }
        } catch (SQLException ex) {
            logger.error(Tool.getLogMessage(ex));
            Tool.out(Tool.getLogMessage(ex));
        } finally {
            DBPool.freeConn(rs, pstm, conn);
        }
        return result;
    }

    @Override
    public int count(String key, String phone, String email, int status, boolean isdel) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT count(*) FROM account WHERE ISDEL = ? ";
        if (!Tool.checkNull(key)) {
            sql += " AND (USERNAME like ? OR FULLNAME like ?)";
        }
        if (!Tool.checkNull(phone)) {
            sql += " AND PHONE like ? ";
        }
        if (!Tool.checkNull(email)) {
            sql += " AND EMAIL like ? ";
        }
        if (status != MyConfig.STATUS.ALL.val) {
            sql += " AND STATUS = ? ";
        }
        try {
            conn = DBPool.getConnection();
            pstm = conn.prepareStatement(sql);
            int i = 1;
            pstm.setBoolean(i++, isdel);
            if (!Tool.checkNull(key)) {
                pstm.setString(i++, "%" + key + "%");
                pstm.setString(i++, "%" + key + "%");
            }
            if (!Tool.checkNull(phone)) {
                pstm.setString(i++, "%" + phone + "%");
            }
            if (!Tool.checkNull(email)) {
                pstm.setString(i++, "%" + email + "%");
            }
            if (status != MyConfig.STATUS.ALL.val) {
                pstm.setInt(i++, status);
            }
            rs = pstm.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            logger.error(Tool.getLogMessage(ex));
            Tool.out(Tool.getLogMessage(ex));
        } finally {
            DBPool.freeConn(rs, pstm, conn);
        }
        return result;
    }

    @Override
    public Account findById(int accID) {
        Account one = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM account WHERE id = ?";
        try {
            conn = DBPool.getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, accID);
            rs = pstm.executeQuery();
            if (rs.next()) {
                one = new Account();
                one.setId(rs.getInt("id"));
                one.setUsername(rs.getString("USERNAME"));
                one.setPassword(rs.getString("PASSWORD"));
                one.setFullname(rs.getString("FULLNAME"));
                one.setAddress(rs.getString("ADDRESS"));
                one.setPhone(rs.getString("PHONE"));
                one.setEmail(rs.getString("EMAIL"));
                one.setCreatedAt(rs.getLong("CREATEDAT"));
                one.setCreatedBy(rs.getString("CREATEDBY"));
                one.setUpdatedAt(rs.getLong("UPDATEDAT"));
                one.setUpdatedBy(rs.getString("UPDATEDBY"));
                one.setStatus(rs.getInt("STATUS"));
                one.setDel(rs.getBoolean("ISDEL"));
                one.setDesc(rs.getString("DESC"));
            }
        } catch (SQLException ex) {
            logger.error(Tool.getLogMessage(ex));
            Tool.out(Tool.getLogMessage(ex));
        } finally {
            DBPool.freeConn(rs, pstm, conn);
        }
        return one;
    }

    @Override
    public Account checkLoginDB(String userName, String password) {
        Account one = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM account WHERE upper(USERNAME) = upper(?) AND PASSWORD = ?";
        try {
            conn = DBPool.getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, userName);
            pstm.setString(2, Md5.encryptMD5(password));
            rs = pstm.executeQuery();
            if (rs.next()) {
                one = new Account();
                one.setId(rs.getInt("id"));
                one.setUsername(rs.getString("USERNAME"));
                one.setPassword(rs.getString("PASSWORD"));
                one.setFullname(rs.getString("FULLNAME"));
                one.setAddress(rs.getString("ADDRESS"));
                one.setPhone(rs.getString("PHONE"));
                one.setEmail(rs.getString("EMAIL"));
                one.setCreatedAt(rs.getLong("CREATEDAT"));
                one.setCreatedBy(rs.getString("CREATEDBY"));
                one.setUpdatedAt(rs.getLong("UPDATEDAT"));
                one.setUpdatedBy(rs.getString("UPDATEDBY"));
                one.setStatus(rs.getInt("STATUS"));
                one.setDel(rs.getBoolean("ISDEL"));
                one.setDesc(rs.getString("DESC"));
            }
        } catch (SQLException ex) {
            logger.error(Tool.getLogMessage(ex));
            Tool.out(Tool.getLogMessage(ex));
        } finally {
            DBPool.freeConn(rs, pstm, conn);
        }
        return one;
    }

    @Override
    public int create(Account one) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "INSERT INTO account(USERNAME, PASSWORD, FULLNAME, ADDRESS, PHONE, EMAIL, CREATEDAT, CREATEDBY, STATUS, account.DESC, ISDEL) "
                + "        VALUES(  ?  ,   ?  ,   ?  , ?   , ?  ,  ? ,  ?   ,  ?   ,  ?  ,    ?   ,  ? )";
        try {
            conn = DBPool.getConnection();
            pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            pstm.setString(i++, one.getUsername());
            pstm.setString(i++, Md5.encryptMD5(one.getPassword()));
            pstm.setString(i++, one.getFullname());
            pstm.setString(i++, one.getAddress());
            pstm.setString(i++, one.getPhone());
            pstm.setString(i++, one.getEmail());
            pstm.setLong(i++, DateProc.createTimestamp().getTime());
            pstm.setString(i++, one.getCreatedBy());
            pstm.setInt(i++, one.getStatus());
            pstm.setString(i++, one.getDesc());
            pstm.setBoolean(i++, one.isDel());
            if (pstm.executeUpdate() == 1) {
                try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        result = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating Account failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error(Tool.getLogMessage(ex));
            Tool.out(Tool.getLogMessage(ex));
        } finally {
            DBPool.freeConn(rs, pstm, conn);
        }
        return result;
    }

    @Override
    public Account update(Account one) {
        Account result = findById(one.getId());
        if (result != null) {
            Connection conn = null;
            PreparedStatement pstm = null;
            String sql = "UPDATE account SET USERNAME = ?, ";
            if (!Tool.checkNull(one.getPassword())) {
                sql += " PASSWORD = ?, ";
            }
            sql += " FULLNAME = ?, ADDRESS = ?, PHONE = ?, EMAIL = ?, "
                    + " UPDATEDAT = ?, UPDATEDBY = ?, STATUS = ?, account.DESC = ? "
                    + " WHERE ID = ?";
            try {
                conn = DBPool.getConnection();
                conn.setAutoCommit(false);
                pstm = conn.prepareStatement(sql);
                int i = 1;
                pstm.setString(i++, one.getUsername());
                if (!Tool.checkNull(one.getPassword())) {
                    pstm.setString(i++, Md5.encryptMD5(one.getPassword()));
                }
                pstm.setString(i++, one.getFullname());
                pstm.setString(i++, one.getAddress());
                pstm.setString(i++, one.getPhone());
                pstm.setString(i++, one.getEmail());
                pstm.setLong(i++, DateProc.createTimestamp().getTime());
                pstm.setString(i++, one.getUpdatedBy());
                pstm.setInt(i++, one.getStatus());
                pstm.setString(i++, one.getDesc());
                pstm.setInt(i++, one.getId());
                if (pstm.executeUpdate() == 1) {
                    conn.commit();
                    return result;
                } else {
                    conn.rollback();
                    result = null;
                }
            } catch (SQLException ex) {
                DBPool.rollback(conn, result);
                logger.error(Tool.getLogMessage(ex));
                Tool.out(Tool.getLogMessage(ex));
            } finally {
                DBPool.freeConn(null, pstm, conn);
            }
        }
        return result;
    }

    @Override
    public Account deleteForEver(int id) {
        Account result = findById(id);
        if (result != null) {
            Connection conn = null;
            PreparedStatement pstm = null;
            String sql = "DELETE FROM account WHERE ID = ?";
            try {
                conn = DBPool.getConnection();
                conn.setAutoCommit(false);
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, id);
                if (pstm.executeUpdate() != 1) {
                    conn.rollback();
                    result = null;
                } else {
                    conn.commit();
                }
            } catch (SQLException ex) {
                DBPool.rollback(conn, result);
                logger.error(Tool.getLogMessage(ex));
                Tool.out(Tool.getLogMessage(ex));
            } finally {
                DBPool.freeConn(null, pstm, conn);
            }
        }
        return result;
    }

    @Override
    public Account delete(int id) {
        Account result = findById(id);
        if (result != null) {

            Connection conn = null;
            PreparedStatement pstm = null;
            String sql = "UPDATE account SET ISDEL = ? WHERE id = ?";
            try {
                conn = DBPool.getConnection();
                conn.setAutoCommit(false);
                pstm = conn.prepareStatement(sql);
                pstm.setBoolean(1, Boolean.TRUE);
                pstm.setInt(2, id);
                if (pstm.executeUpdate() == 1) {
                    result = findById(id);
                    conn.commit();
                } else {
                    DBPool.rollback(conn, result);
                }
            } catch (SQLException ex) {
                DBPool.rollback(conn, result);
                logger.error(Tool.getLogMessage(ex));
                Tool.out(Tool.getLogMessage(ex));
            } finally {
                DBPool.freeConn(null, pstm, conn);
            }

        }
        return result;
    }

    @Override
    public Account undoDelete(int id) {
        Account result = findById(id);
        if (result != null) {
            Connection conn = null;
            PreparedStatement pstm = null;
            String sql = "UPDATE account SET ISDEL = ? WHERE id = ?";
            try {
                conn = DBPool.getConnection();
                conn.setAutoCommit(false);
                pstm = conn.prepareStatement(sql);
                pstm.setBoolean(1, Boolean.FALSE);
                pstm.setInt(2, id);
                if (pstm.executeUpdate() == 1) {
                    result = findById(id);
                    conn.commit();
                } else {
                    DBPool.rollback(conn, result);
                }
            } catch (SQLException ex) {
                DBPool.rollback(conn, result);
                logger.error(Tool.getLogMessage(ex));
                Tool.out(Tool.getLogMessage(ex));
            } finally {
                DBPool.freeConn(null, pstm, conn);
            }
        }
        return result;
    }

    @Override
    public boolean existsByUsername(String username) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM account WHERE 1 = 1 AND USERNAME = ?";
        try {
            conn = DBPool.getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, username);
            rs = pstm.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException ex) {
            logger.error(Tool.getLogMessage(ex));
            Tool.out(Tool.getLogMessage(ex));
        } finally {
            DBPool.freeConn(rs, pstm, conn);
        }
        return result;
    }

}
