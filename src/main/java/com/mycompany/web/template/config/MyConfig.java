/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.template.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.mycompany.web.template.commons.AbstractConst;
import com.mycompany.web.template.commons.Tool;
import static com.mycompany.web.template.config.WebInitializer.context;
import com.mycompany.web.template.model.ext.ModelEnum;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *
 * @author TUANPLA
 */
@Component
@PropertySource(value = {"classpath:application.properties"})
public class MyConfig {

    static Logger logger = Logger.getLogger(MyConfig.class);

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        try {
            DE_BUG = getBoolean("project.main.debug", DE_BUG);
            DEFAULT_LANG = getString("project.main.defaultLang", DEFAULT_LANG);
            USER_SESSION_NAME = getString("project.main.usersessionName", USER_SESSION_NAME);
            loadLang(DEFAULT_LANG);
            Map<String, Integer> hasMap = new HashMap<>();
            for (MyConfig.STATUS one : MyConfig.STATUS.values()) {
                hasMap.put(one.toString(), one.val);
            }
            context.setAttribute("status", hasMap);
        } catch (IOException e) {
            Tool.out(e.getMessage());
        }
    }

    public static boolean DE_BUG;
    public static String DEFAULT_LANG = "vi";
    public static String USER_SESSION_NAME = "userlogin";
    public static boolean running = false;
    public static int MAX_ROW = 20;
    public static final boolean ISDEL = true;
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    int getInt(String properties, int defaultVal) {
        try {
            return Integer.parseInt(environment.getProperty(properties, defaultVal + ""));
        } catch (NumberFormatException e) {
            logger.error(e);
            return defaultVal;
        }
    }

    long getLong(String properties, long defaultVal) {
        try {
            return Long.parseLong(environment.getProperty(properties, defaultVal + ""));
        } catch (NumberFormatException e) {
            logger.error(e);
            return defaultVal;
        }
    }

    Double getDouble(String properties, Double defaultVal) {
        try {
            return Double.parseDouble(environment.getProperty(properties, defaultVal + ""));
        } catch (NumberFormatException e) {
            logger.error(e);
            return defaultVal;
        }
    }

    String getString(String properties, String defaultVal) {
        try {
            return environment.getProperty(properties, defaultVal);
        } catch (Exception e) {
            logger.error(e);
            return defaultVal;
        }
    }

    boolean getBoolean(String properties, boolean defaultVal) {
        try {
            return environment.getProperty(properties, "0").equals("1") || environment.getProperty(properties, "false").equals("true");
        } catch (Exception e) {
            logger.error(e);
            return defaultVal;
        }
    }

    public static enum STATUS {
        ALL(127, AbstractConst.LANG.get("generic.status.all")),
        UNACTIVE(0, AbstractConst.LANG.get("generic.status.unactive")),
        ACTIVE(1, AbstractConst.LANG.get("generic.status.active")),
        LOCK(2, AbstractConst.LANG.get("generic.status.lock")),;
        //--
        public int val;
        public String desc;

        private STATUS(int val, String desc) {
            this.val = val;
            this.desc = desc;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }

        public String getName() {
            return desc;
        }

        public void setName(String name) {
            this.desc = name;
        }
    }

    public static ArrayList<ModelEnum> getStatus() {
        ArrayList<ModelEnum> _mystatus = new ArrayList<>();
        for (MyConfig.STATUS one : MyConfig.STATUS.values()) {
            ModelEnum _status = new ModelEnum(one.val, one.desc);
            _mystatus.add(_status);
        }
        return _mystatus;
    }

    public static void loadLang(String lang) throws IOException {
        JsonObject json_lang = readLangConf(lang);
        Set<String> entries = json_lang.keySet();
        entries.forEach((mainKey) -> {
            JsonElement objVal = json_lang.get(mainKey);
            if (objVal.isJsonObject()) {
                JsonObject one_conf = json_lang.getAsJsonObject(mainKey);
                Set<String> sub_entries = one_conf.keySet();
                sub_entries.forEach((subKey) -> {
                    String oneValue = one_conf.get(subKey).getAsString();
                    AbstractConst.LANG.put(mainKey + "." + subKey, oneValue);
                });
            }
        });
        context.setAttribute("Lang", AbstractConst.LANG);
    }

    public static JsonObject readLangConf(String lang) throws IOException {
        JsonObject json = null;
        try {
            String path = WebInitializer.rootDir + "/res/lang/" + lang + ".json";
            try (FileInputStream inStream = new FileInputStream(path)) {
                InputStreamReader reader = new InputStreamReader(inStream, "UTF-8");
                Gson gson = new Gson();
                json = gson.fromJson(reader, JsonObject.class);
            }
        } catch (JsonIOException e) {
            Tool.out(e.getMessage());
        }
        return json;
    }
}
