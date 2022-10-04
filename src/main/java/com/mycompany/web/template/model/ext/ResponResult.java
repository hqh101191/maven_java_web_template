/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.template.model.ext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.log4j.Logger;
import com.mycompany.web.template.commons.AbstractConst.RESULT;
import static com.mycompany.web.template.commons.Tool.getLogMessage;

/**
 *
 * @author Company
 */
@XmlRootElement
public class ResponResult implements Serializable {

    static Logger logger = Logger.getLogger(ResponResult.class);
    public static final String NAME = "result";

    public ResponResult(RESULT code, String mess) {
        this.code = code.val;
        this.message = mess;
    }
    int code;
    String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String toJson() {
        String str = "";
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            str = gson.toJson(this);
        } catch (Exception e) {
            logger.error(getLogMessage(e));
        }
        return str;
    }
}
