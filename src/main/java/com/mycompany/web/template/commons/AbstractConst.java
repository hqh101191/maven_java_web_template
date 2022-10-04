/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.template.commons;

import com.mycompany.web.template.model.ext.ResponResult;
import com.mycompany.web.template.service.AccountService;
import java.util.HashMap;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 *
 * @author Private
 */
public abstract class AbstractConst {

    public static final HashMap<String, String> LANG = new HashMap<>();

    protected final String TITLE = "title";
    protected final String INDEX_PAGE = "redirect:/index";
    protected static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
    protected static final String TEXT_HTML_UTF8 = "text/html;charset=UTF-8";
    protected static final String APPLICATION_XML_UTF8 = "application/xml;charset=UTF-8";
    protected static final String RESP_NAME = ResponResult.NAME;
    protected static final int MAX_LIFE_KEY = 5 * 1000;
    protected static final String verifykey = "verifykey";

    @Autowired
    protected AccountService accountService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringtrimmer);
    }

    public enum ACTION {
        VIEW,
        CREATE,
        UPDATE,
        DEL,
        LOGIN,
        LOGOUT;

        public static ACTION getTypeByname(String name) {
            ACTION result = null;
            for (ACTION one : ACTION.values()) {
                if (!Tool.checkNull(name) && one.name().equals(name)) {
                    result = one;
                    break;
                }
            }
            return result;
        }
    }

    public enum RESULT {
        FAIL(0),
        SUCCESS(1),
        NO_LOGIN(-1),
        NO_RIGHT(3),;
        public int val;

        private RESULT(int val) {
            this.val = val;
        }

        public static RESULT getResultByname(String name) {
            RESULT result = null;
            for (RESULT one : RESULT.values()) {
                if (!Tool.checkNull(name) && one.name().equals(name)) {
                    result = one;
                    break;
                }
            }
            return result;
        }
    }

    protected String getUniqueKey() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    protected String getReferrer(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.REFERER);
    }
}
