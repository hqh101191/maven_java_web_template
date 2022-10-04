/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.template.controller;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.mycompany.web.template.commons.HttpUtil;
import com.mycompany.web.template.commons.Tool;
import com.mycompany.web.template.config.MyConfig;
import com.mycompany.web.template.model.Account;
import com.mycompany.web.template.model.UserActionLog;
import com.mycompany.web.template.model.ext.AngularModel;
import com.mycompany.web.template.model.ext.ResponResult;

/**
 *
 * @author Private
 */
@Controller
@RequestMapping("/account")
public class AccountController extends AbstractBackEnConst {

    private final String REDIRECT_VIEW = "redirect:/account/list";
    private final String TABLE = "ACCOUNT";
    private final String MODEL_NAME = "account";

    @GetMapping("/list")
    @Override
    public String list(ModelMap model, HttpServletRequest request, RedirectAttributes redrAtt) {
        if (accountService.checkAccess(request)) {
            model.put(TITLE, LANG.get("sysAccount.title.list"));
            Account acc = accountService.getAccountLogin(request);
            model.addAttribute(MODEL_NAME, acc);
            return "account";
        } else {
            ResponResult result = new UserActionLog(TABLE, 0, ACTION.VIEW, RESULT.NO_RIGHT, "Bạn không có quyền truy cập Quản lý tài khoản hệ thống").logAction(request);
            redrAtt.addFlashAttribute(RESP_NAME, result);
            return INDEX_PAGE;
        }

    }

    @PostMapping("/rest/data")
    @Override
    public ResponseEntity<AngularModel<Account>> getData(HttpServletRequest request) {
        AngularModel<Account> ngModel = new AngularModel<>();
        if (accountService.checkLogin(request)) {
            String key = HttpUtil.getString(request, "key");
            String phone = HttpUtil.getString(request, "phone");
            String email = HttpUtil.getString(request, "email");
            int maxRow = HttpUtil.getInt(request, "maxRow", MyConfig.MAX_ROW);
            int crPage = HttpUtil.getInt(request, "crPage", 1);
            int status = HttpUtil.getInt(request, "status", MyConfig.STATUS.ALL.val);
            ArrayList<Account> listData = accountService.view(crPage, maxRow, key, phone, email, status, !MyConfig.ISDEL);
            int totalRow = accountService.count(key, phone, email, status, !MyConfig.ISDEL);

            ngModel.setDatas(listData);
            ngModel.setTotalRow(totalRow);

            if (listData == null || listData.isEmpty()) {
                ngModel.setResult(new ResponResult(RESULT.FAIL, "Danh sách tài khoản trống"));
            } else {
                ngModel.setResult(new ResponResult(RESULT.SUCCESS, ""));
            }
        } else {
            ResponResult result = new UserActionLog(TABLE, 0, ACTION.VIEW, RESULT.NO_LOGIN, "Bạn chưa đăng nhập").logAction(request);
            ngModel.setResult(result);
        }
        return new ResponseEntity<>(ngModel, HttpStatus.OK);
    }

    @GetMapping("/recycle")
    public String recycleView(ModelMap model, HttpServletRequest request, RedirectAttributes redrAtt) {
        if (accountService.checkAccess(request)) {
            model.put(TITLE, LANG.get("sysAccount.title.recycle"));
            return "account-recycle";
        } else {
            ResponResult result = new UserActionLog(TABLE, 0, ACTION.VIEW, RESULT.NO_RIGHT, "Bạn không được quyền truy cập dữ liệu đã xóa của tài khoản hệ thống").logAction(request);
            redrAtt.addFlashAttribute(RESP_NAME, result);
            return INDEX_PAGE;
        }
    }

    @PostMapping("/rest/recycle-data")
    public ResponseEntity<AngularModel<Account>> recycleData(HttpServletRequest request) {
        AngularModel<Account> ngModel = new AngularModel<>();
        if (accountService.checkLogin(request)) {
            String key = HttpUtil.getString(request, "key");
            String phone = HttpUtil.getString(request, "phone");
            String email = HttpUtil.getString(request, "email");
            int maxRow = HttpUtil.getInt(request, "maxRow", MyConfig.MAX_ROW);
            int crPage = HttpUtil.getInt(request, "crPage", 1);
            int status = HttpUtil.getInt(request, "status", MyConfig.STATUS.ALL.val);
            ArrayList<Account> listAcc = accountService.view(crPage, maxRow, key, phone, email, status, MyConfig.ISDEL);
            int totalRow = accountService.count(key, phone, email, status, !MyConfig.ISDEL);

            ngModel.setDatas(listAcc);
            ngModel.setTotalRow(totalRow);

            if (listAcc == null || listAcc.isEmpty()) {
                ngModel.setResult(new ResponResult(RESULT.FAIL, "Danh sách tài khoản trống"));
            } else {
                ngModel.setResult(new ResponResult(RESULT.SUCCESS, ""));
            }
        } else {
            ResponResult result = new UserActionLog(TABLE, 0, ACTION.VIEW, RESULT.NO_LOGIN, "Bạn chưa đăng nhập").logAction(request);
            ngModel.setResult(result);
        }
        return new ResponseEntity<>(ngModel, HttpStatus.OK);
    }

    @GetMapping("/create")
    @Override
    public String createView(ModelMap modelMap, HttpServletRequest request, RedirectAttributes redrAtt) {
        if (accountService.checkAccess(request)) {
            Account account = new Account();
            modelMap.put(MODEL_NAME, account);
            modelMap.put(TITLE, LANG.get("sysAccount.title.add"));
            return "account-add";
        } else {
            ResponResult result = new UserActionLog(TABLE, 0, ACTION.CREATE, RESULT.NO_RIGHT, "Bạn không có quyền thêm mới tài khoản hệ thống").logAction(request);
            redrAtt.addFlashAttribute(RESP_NAME, result);
            return INDEX_PAGE;
        }
    }

    @PostMapping("/create")
    @Override
    public String createProcess(ModelMap model, HttpServletRequest request, RedirectAttributes redrAtt) {
        if (accountService.checkAccess(request)) {
            String username = HttpUtil.getString(request, "username");
            String password = HttpUtil.getString(request, "password");
            String fullName = HttpUtil.getString(request, "fullName");
            String phone = HttpUtil.getString(request, "phone");
            String email = HttpUtil.getString(request, "email");
            String address = HttpUtil.getString(request, "address");
            int status = HttpUtil.getInt(request, "status", MyConfig.STATUS.LOCK.val);
            String desc = HttpUtil.getString(request, "desc", "");

            Account account = new Account();
            account.setUsername(username);
            account.setPassword(password);
            account.setFullname(fullName);
            account.setPhone(phone);
            account.setEmail(email);
            account.setAddress(address);
            account.setStatus(status);
            account.setDesc(desc);

            model.addAttribute(MODEL_NAME, account);

            if (accountService.existsByUsername(username)) {
                ResponResult result = new UserActionLog(TABLE, 0, ACTION.CREATE, RESULT.FAIL, "Username đã tồn tại, không tạo mới được!").logAction(request);
                model.put(RESP_NAME, result);
                model.put(MODEL_NAME, account);
                return "account-add";
            }

            account.setCreatedBy(accountService.getUserName(request));
            int id = accountService.create(account);
            if (id > 0) {
                ResponResult result = new UserActionLog(TABLE, id, ACTION.CREATE, RESULT.SUCCESS, "Thêm mới thành công").logAction(request);
                redrAtt.addFlashAttribute(RESP_NAME, result);
                return REDIRECT_VIEW;
            } else {
                ResponResult result = new UserActionLog(TABLE, id, ACTION.CREATE, RESULT.FAIL, "Thêm mới tài khoản thất bại id=" + id).logAction(request);
                model.put(RESP_NAME, result);
                model.put(MODEL_NAME, account);
                return "account-add";
            }
        } else {
            ResponResult result = new UserActionLog(TABLE, 0, ACTION.CREATE, RESULT.NO_RIGHT, "Bạn không có quyền thêm mới tài khoản hệ thống").logAction(request);
            redrAtt.addFlashAttribute(RESP_NAME, result);
            return INDEX_PAGE;
        }
    }

    @GetMapping("/edit")
    @Override
    public String editView(ModelMap model, HttpServletRequest request, RedirectAttributes redrAtt) {
        int id = HttpUtil.getInt(request, "id");
        if (accountService.checkAccess(request)) {
            Account acc = accountService.findById(id);
            model.put(TITLE, LANG.get("sysAccount.title.edit"));
            if (acc != null) {
                model.addAttribute(MODEL_NAME, acc);
                return "account-edit";
            } else {
                ResponResult result = new UserActionLog(TABLE, id, ACTION.UPDATE, RESULT.FAIL, "Không tìm thấy tài khoản cần sửa").logAction(request);
                redrAtt.addFlashAttribute(RESP_NAME, result);
                return REDIRECT_VIEW;
            }
        } else {
            ResponResult result = new UserActionLog(TABLE, id, ACTION.UPDATE, RESULT.NO_RIGHT, "Bạn không có quyền cập nhật tài khoản hệ thống id=" + id).logAction(request);
            redrAtt.addFlashAttribute(RESP_NAME, result);
            return INDEX_PAGE;
        }
    }

    @PostMapping("/edit")
    @Override
    public String editProcess(ModelMap model, HttpServletRequest request, RedirectAttributes redrAtt) {
        int id = HttpUtil.getInt(request, "id");
        if (accountService.checkAccess(request)) {
            Account account = accountService.findById(id);
            if (account != null) {
                String username = HttpUtil.getString(request, "username");
                String password = HttpUtil.getString(request, "password");
                String fullName = HttpUtil.getString(request, "fullName");
                String phone = HttpUtil.getString(request, "phone");
                String email = HttpUtil.getString(request, "email");
                String address = HttpUtil.getString(request, "address");
                int status = HttpUtil.getInt(request, "status", MyConfig.STATUS.LOCK.val);
                String desc = HttpUtil.getString(request, "desc", "");

                account.setUsername(username);
                account.setPassword(password);
                account.setFullname(fullName);
                account.setPhone(phone);
                account.setEmail(email);
                account.setAddress(address);
                account.setStatus(status);
                account.setDesc(desc);
                model.addAttribute(MODEL_NAME, account);
                if (!Tool.checkNull(password)) {
                    if (Tool.stringIsSpace(password) || !Tool.Password_Validation(password)) {
                        new UserActionLog(TABLE, account.getId(), ACTION.UPDATE, RESULT.FAIL, "Password phải có ít nhất 1 ký tự là số \"0123456789\" và 1 ký tự đặc biệt như \"!@#$%&*()_+=|<>?{}[]~-\"!").logAction(request);
                        model.put(RESP_NAME, new ResponResult(RESULT.FAIL, "Password phải có ít nhất 1 ký tự là số \"0123456789\" và 1 ký tự đặc biệt như \"!@#$%&*()_+=|<>?{}[]~-\"!"));
                        model.put(MODEL_NAME, account);
                        return "account-edit";
                    }
                }
                Account oldVal = accountService.update(account);
                if (oldVal != null) {
                    new UserActionLog(TABLE, account.getId(), ACTION.UPDATE, RESULT.SUCCESS, "Cập nhật tài khoản thành công", oldVal).logAction(request);
                    redrAtt.addFlashAttribute(RESP_NAME, new ResponResult(RESULT.SUCCESS, "Cập nhật tài khoản thành công"));
                } else {
                    new UserActionLog(TABLE, account.getId(), ACTION.UPDATE, RESULT.FAIL, "Cập nhật tài khoản thất bại").logAction(request);
                    model.put(RESP_NAME, new ResponResult(RESULT.FAIL, "Cập nhật tài khoản thất bại"));
                    model.put(MODEL_NAME, account);
                    return "account-edit";
                }
            } else {
                new UserActionLog(TABLE, 0, ACTION.CREATE, RESULT.FAIL, "Không tìm thấy tài khoản cần sửa").logAction(request);
                redrAtt.addFlashAttribute(RESP_NAME, new ResponResult(RESULT.FAIL, "Không tìm thấy tài khoản cần sửa"));
            }
            return REDIRECT_VIEW;
        } else {
            redrAtt.addFlashAttribute(RESP_NAME, new ResponResult(RESULT.NO_RIGHT, "Bạn không có quyền cập nhật tài khoản hệ thống"));
            new UserActionLog(TABLE, 0, ACTION.CREATE, RESULT.NO_RIGHT, "Bạn không có quyền cập nhật tài khoản hệ thống").logAction(request);
            return INDEX_PAGE;
        }
    }

    @PostMapping("/rest/delete")
    @Override
    public ResponseEntity<AngularModel<ResponResult>> delete(HttpServletRequest request) {
        int id = HttpUtil.getInt(request, "id");

        AngularModel<ResponResult> ngmodel = new AngularModel<>();
        if (accountService.checkAccess(request)) {
            Account account = accountService.findById(id);
            Account oldVal = accountService.delete(id);
            if (oldVal != null) {
                ResponResult result = new UserActionLog(TABLE, id, ACTION.DEL, RESULT.SUCCESS, "Xóa tải khoản --> " + account.getUsername() + " <-- thành công", account).logAction(request);
                ngmodel.setResult(result);
                return new ResponseEntity<>(ngmodel, HttpStatus.OK);
            } else {
                ResponResult result = new UserActionLog(TABLE, id, ACTION.DEL, RESULT.FAIL, "Xóa tài khoản --> " + account.getUsername() + " <-- thất bại").logAction(request);
                ngmodel.setResult(result);
                return new ResponseEntity<>(ngmodel, HttpStatus.OK);
            }
        } else {
            ResponResult result = new UserActionLog(TABLE, id, ACTION.DEL, RESULT.NO_RIGHT, "Bạn không có quyền xóa tài khoản hệ thống").logAction(request);
            ngmodel.setResult(result);
            return new ResponseEntity<>(ngmodel, HttpStatus.OK);
        }
    }

    @PostMapping("/rest/undo-account")
    public ResponseEntity<AngularModel<ResponResult>> undoDelAccount(HttpServletRequest request) {
        int id = HttpUtil.getInt(request, "id");
        AngularModel<ResponResult> ngmodel = new AngularModel<>();
        if (accountService.checkAccess(request)) {
            Account account = accountService.findById(id);
            Account oldVal = accountService.undoDelete(id);
            if (oldVal != null) {
                ResponResult result = new UserActionLog(TABLE, id, ACTION.DEL, RESULT.SUCCESS, "Khôi phục tải khoản --> " + account.getUsername() + " <-- thành công", account).logAction(request);
                ngmodel.setResult(result);
                return new ResponseEntity<>(ngmodel, HttpStatus.OK);
            } else {
                ResponResult result = new UserActionLog(TABLE, id, ACTION.DEL, RESULT.FAIL, "Khôi phục tài khoản --> " + account.getUsername() + " <-- thất bại").logAction(request);
                ngmodel.setResult(result);
                return new ResponseEntity<>(ngmodel, HttpStatus.OK);
            }
        } else {
            ResponResult result = new UserActionLog(TABLE, id, ACTION.DEL, RESULT.NO_RIGHT, "Bạn không có quyền khôi phục tài khoản hệ thống").logAction(request);
            ngmodel.setResult(result);
            return new ResponseEntity<>(ngmodel, HttpStatus.OK);
        }
    }

    @PostMapping("/rest/deleteForEver")
    public ResponseEntity<AngularModel<ResponResult>> deleteForEver(HttpServletRequest request) {
        int id = HttpUtil.getInt(request, "id");
        AngularModel<ResponResult> ngmodel = new AngularModel<>();
        if (accountService.checkAccess(request)) {
            Account account = accountService.findById(id);
            Account oldVal = accountService.deleteForEver(id);
            if (oldVal != null) {
                ResponResult result = new UserActionLog(TABLE, id, ACTION.DEL, RESULT.SUCCESS, "Xóa tải khoản --> " + account.getUsername() + " <-- thành công", account).logAction(request);
                ngmodel.setResult(result);
                return new ResponseEntity<>(ngmodel, HttpStatus.OK);
            } else {
                ResponResult result = new UserActionLog(TABLE, id, ACTION.DEL, RESULT.FAIL, "Xóa tài khoản --> " + account.getUsername() + " <-- thất bại").logAction(request);
                ngmodel.setResult(result);
                return new ResponseEntity<>(ngmodel, HttpStatus.OK);
            }
        } else {
            ResponResult result = new UserActionLog(TABLE, id, ACTION.DEL, RESULT.NO_RIGHT, "Bạn không có quyền xóa tài khoản hệ thống").logAction(request);
            ngmodel.setResult(result);
            return new ResponseEntity<>(ngmodel, HttpStatus.OK);
        }
    }

}
