/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.template.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mycompany.web.template.commons.HttpUtil;
import com.mycompany.web.template.commons.Tool;
import com.mycompany.web.template.db.DBPool;
import com.mycompany.web.template.model.ext.ModelEnum;
import com.mycompany.web.template.model.ext.ResponResult;
import com.mycompany.web.template.commons.AbstractConst.ACTION;
import com.mycompany.web.template.commons.AbstractConst.RESULT;
import com.mycompany.web.template.commons.DateProc;
import com.mycompany.web.template.service.AccountService;

/**
 *
 * @author Private
 */
@Component
public class UserActionLog implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(UserActionLog.class);
    
    @Autowired
    protected static final AccountService accountService = new AccountService();
    
    public UserActionLog(String table, int idAction, ACTION actionType, RESULT result, String info) {
        this.tableAction = table;
        this.idAction = idAction;
        this.actionType = actionType;
        this.result = result;
        this.info = info;
    }
    
    public UserActionLog(String table, int idAction, ACTION actionType, RESULT result, String info, Object lastObject) {
        this.tableAction = table;
        this.idAction = idAction;
        this.actionType = actionType;
        this.result = result;
        this.info = info;
        this.lastObject = Tool.toJson(lastObject);
        this.classObject = lastObject.getClass().toString();
    }
    
    private int id;
    private String userName;
    private String userIp;
    private String urlAction;
    private String tableAction;
    private int idAction;
    private ACTION actionType;
    private Long actionDate;
    private RESULT result;
    private String info;
    private String lastObject;
    private String classObject;
    private String kindAction;
    private String dateView;
    
    public UserActionLog() {
    }
    
    public static enum KIND_ACT {
        SYS("SYS User Action"),
        CUS("Customer Action");
        public String name;
        public String desc;
        
        private KIND_ACT(String desc) {
            this.desc = desc;
            this.name = this.name();
        }
    }
    
    public static ArrayList<ModelEnum> buildType() {
        ArrayList<ModelEnum> _mystatus = new ArrayList<>();
        for (KIND_ACT one : KIND_ACT.values()) {
            ModelEnum _status = new ModelEnum(one.name, one.desc);
            _mystatus.add(_status);
        }
        return _mystatus;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserIp() {
        return userIp;
    }
    
    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }
    
    public String getUrlAction() {
        return urlAction;
    }
    
    public void setUrlAction(String urlAction) {
        this.urlAction = urlAction;
    }
    
    public String getTableAction() {
        return tableAction;
    }
    
    public void setTableAction(String tableAction) {
        this.tableAction = tableAction;
    }
    
    public int getIdAction() {
        return idAction;
    }
    
    public void setIdAction(int idAction) {
        this.idAction = idAction;
    }
    
    public ACTION getActionType() {
        return actionType;
    }
    
    public void setActionType(ACTION actionType) {
        this.actionType = actionType;
    }
    
    public void setActionType(String actName) {
        this.actionType = ACTION.getTypeByname(actName);
    }
    
    public long getActionDate() {
        return actionDate;
    }
    
    public void setActionDate(long actionDate) {
        this.actionDate = actionDate;
    }
    
    public RESULT getResult() {
        return result;
    }
    
    public void setResult(RESULT result) {
        this.result = result;
    }
    
    public void setResult(String name) {
        this.result = RESULT.getResultByname(name);
    }
    
    public String getInfo() {
        return info;
    }
    
    public void setInfo(String info) {
        this.info = info;
    }
    
    public String getLastObject() {
        return lastObject;
    }
    
    public void setLastObject(String lastObject) {
        this.lastObject = lastObject;
    }
    
    public String getClassObject() {
        return classObject;
    }
    
    public void setClassObject(String classObject) {
        this.classObject = classObject;
    }
    
    public String getKindAction() {
        return kindAction;
    }
    
    public void setKindAction(String kindAction) {
        this.kindAction = kindAction;
    }
    
    public String getDateView() {
        return dateView;
    }
    
    public void setDateView(String dateView) {
        this.dateView = dateView;
    }
    
    public ResponResult logAction(HttpServletRequest request) {
        ResponResult _result = new ResponResult(this.result, this.info);
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "INSERT INTO USER_ACTION_LOG(USER_NAME,USER_IP,URL_ACTION,TABLE_ACTION,ID_ACTION,ACTION_TYPE,ACTION_DATE,RESULT,INFO,LAST_OBJECT,CLASS_OBJECT,KIND_ACTION)"
                + "                        VALUES(   ?     ,   ?   ,    ?     ,     ?      ,     ?   ,     ?     ,   ?       ,  ?   ,  ? ,    ?      ,     ?      ,    ?      )";
        try {
            String url = HttpUtil.getFullURL(request);
            String ip = HttpUtil.getClientIpAddr(request);
            this.userName = accountService.getUserName(request);
            conn = DBPool.getConnection();
            pstm = conn.prepareStatement(sql);
            int i = 1;
            pstm.setString(i++, this.getUserName());
            pstm.setString(i++, ip);
            pstm.setString(i++, url);
            pstm.setString(i++, this.getTableAction());
            pstm.setInt(i++, this.getIdAction());
            pstm.setString(i++, this.actionType.name());
            pstm.setLong(i++, DateProc.createTimestamp().getTime());
            pstm.setString(i++, this.result.name());
            pstm.setString(i++, this.getInfo());
            pstm.setString(i++, this.getLastObject());
            pstm.setString(i++, getClassObject());
            pstm.setString(i++, KIND_ACT.SYS.name);
            pstm.execute();
        } catch (SQLException e) {
            DBPool.rollback(conn, result);
            logger.error(Tool.getLogMessage(e));
            Tool.out(e.getMessage());
        } finally {
            DBPool.freeConn(rs, pstm, conn);
        }
        return _result;
    }
    
}
