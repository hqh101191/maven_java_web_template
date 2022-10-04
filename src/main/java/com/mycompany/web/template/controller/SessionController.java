/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.template.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.mycompany.web.template.commons.HttpUtil;
import com.mycompany.web.template.config.MyConfig;
import com.mycompany.web.template.config.MyContext;
import com.mycompany.web.template.model.Account;
import com.mycompany.web.template.model.UserActionLog;
import com.mycompany.web.template.model.ext.AngularModel;
import com.mycompany.web.template.model.ext.ResponResult;

/**
 *
 * @author Private
 */
@Controller
public class SessionController extends AbstractBackEnConst {

    private final String TABLE = "ACCOUNT";

    @InitBinder
    @Override
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringtrimmer);
    }

    @GetMapping({"/index", "/", ""})
    public String index(ModelMap model, HttpServletRequest request) {
        model.put(TITLE, LANG.get("generic.home"));
        return "index";
    }

    @GetMapping("/sessionExpire")
    public ResponseEntity<AngularModel<Account>> sessionExpire(HttpServletRequest request) {
        AngularModel<Account> ngModel = new AngularModel<>();
        ngModel.setResult(new ResponResult(RESULT.NO_LOGIN, "Session Expire..."));
        return new ResponseEntity<>(ngModel, HttpStatus.OK);
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, ModelMap model) {
        Account account = accountService.getAccountLogin(request);
        model.put(TITLE, LANG.get("generic.login"));
        if (account != null) {
            return INDEX_PAGE;
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginProcess(ModelMap model, HttpServletRequest request) {
        String username = HttpUtil.getString(request, "username");
        String password = HttpUtil.getString(request, "password");
        HttpSession session = request.getSession(false);
        Account account = accountService.checkLoginDB(username, password);
        if (account != null) {
            if (account.getStatus() == MyConfig.STATUS.LOCK.val || account.getStatus() == MyConfig.STATUS.UNACTIVE.val) {
                ResponResult result = new UserActionLog(TABLE, 0, ACTION.LOGIN, RESULT.FAIL, "Tài khoản của bạn bị khóa hoặc chưa active, vui lòng liên hệ quản trị!:user=" + username + "|p=" + password).logAction(request);
                model.addAttribute("error", "Tài khoản của bạn bị khóa hoặc chưa active, vui lòng liên hệ quản trị!");
                return "login";
            }
            if (account.isDel()) {
                ResponResult result = new UserActionLog(TABLE, 0, ACTION.LOGIN, RESULT.FAIL, "Tài khoản không tồn tại, vui lòng liên hệ quản trị!:user=" + username + "|p=" + password).logAction(request);
                model.addAttribute("error", "Tài khoản không tồn tại, vui lòng liên hệ quản trị!");
                return "login";
            }
            session.setAttribute(MyConfig.USER_SESSION_NAME, account);
            MyContext.putSessionOnline(username, session);
            ResponResult result = new UserActionLog(TABLE, account.getId(), ACTION.LOGIN, RESULT.SUCCESS, "Đăng nhập thành công").logAction(request);
            return INDEX_PAGE;
        } else {
            ResponResult result = new UserActionLog(TABLE, 0, ACTION.LOGIN, RESULT.NO_RIGHT, "Đăng nhập thất bại vui lòng kiểm tra lại!:user=" + username + "|p=" + password).logAction(request);
            model.addAttribute("error", "Đăng nhập thất bại vui lòng kiểm tra lại!:user=" + username + "|p=" + password);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Account acc = accountService.getAccountLogin(request);
        if (acc != null) {
            new UserActionLog(TABLE, acc.getId(), ACTION.LOGIN, RESULT.SUCCESS, "Đăng xuất thành công").logAction(request);
            MyContext.logOutSession(acc.getUsername());
        }
        if (session != null) {
            session.removeAttribute(MyConfig.USER_SESSION_NAME);
            session.invalidate();
        }
        return "redirect:/login";
    }

    @Override
    public String list(ModelMap modelMap, HttpServletRequest request, RedirectAttributes redrAtt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity getData(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
