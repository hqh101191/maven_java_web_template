package com.mycompany.web.template.controller;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.mycompany.web.template.commons.HttpUtil;
import com.mycompany.web.template.config.MyConfig;
import com.mycompany.web.template.model.UserActionLog;
import com.mycompany.web.template.model.ext.AngularModel;
import com.mycompany.web.template.model.ext.ResponResult;
import com.mycompany.web.template.service.UserActionService;

@Controller
@RequestMapping("/user-action")
public class UserActionController extends AbstractBackEnConst {

    private final String TABLE = "LOG_USER_ACTION";
    @Autowired
    UserActionService userActionService;

    @GetMapping("/list")
    @Override
    public String list(ModelMap model, HttpServletRequest request, RedirectAttributes redrAtt) {
        if (accountService.checkAccess(request)) {
            model.put(TITLE, LANG.get("userAction.title.list"));
            return "user-action";
        } else {
            ResponResult result = new UserActionLog(TABLE, 0, ACTION.VIEW, RESULT.NO_RIGHT, "Bạn không có quyền truy cập lịch sử người dùng thao tác").logAction(request);
            redrAtt.addFlashAttribute(RESP_NAME, result);
            return INDEX_PAGE;
        }
    }

    @GetMapping("/rest/data")
    @Override
    public ResponseEntity<AngularModel<UserActionLog>> getData(HttpServletRequest request) {
        AngularModel<UserActionLog> ngModel = new AngularModel<>();
        if (accountService.checkLogin(request)) {
            int crPage = HttpUtil.getInt(request, "crPage", 1);
            int maxRow = HttpUtil.getInt(request, "maxRow", MyConfig.MAX_ROW);
            String user_name = HttpUtil.getString(request, "user_name");
            String table_action = HttpUtil.getString(request, "table_action");
            String action_type = HttpUtil.getString(request, "action_type");
            String result_status = HttpUtil.getString(request, "result");

            ArrayList<UserActionLog> listData = userActionService.view(crPage, maxRow, user_name, table_action, action_type, result_status);

            int totalRow = userActionService.count(user_name, table_action, action_type, result_status);

            ngModel.setDatas(listData);
            ngModel.setTotalRow(totalRow);

            if (listData == null || listData.isEmpty()) {
                ngModel.setResult(new ResponResult(RESULT.FAIL, "Danh sách lịch sử người dùng thao tác trống"));
            } else {
                ngModel.setResult(new ResponResult(RESULT.SUCCESS, ""));
            }
        } else {
            ResponResult result = new UserActionLog(TABLE, 0, ACTION.VIEW, RESULT.NO_LOGIN, "Bạn chưa đăng nhập").logAction(request);
            ngModel.setResult(result);
        }
        return new ResponseEntity<>(ngModel, HttpStatus.OK);
    }

    @Override
    public String createView(ModelMap model, HttpServletRequest request, RedirectAttributes redrAtt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String createProcess(ModelMap model, HttpServletRequest request, RedirectAttributes redrAtt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String editView(ModelMap model, HttpServletRequest request, RedirectAttributes redrAtt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String editProcess(ModelMap model, HttpServletRequest request, RedirectAttributes redrAtt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<AngularModel<ResponResult>> delete(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
