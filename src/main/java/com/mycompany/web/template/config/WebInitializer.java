package com.mycompany.web.template.config;

import com.mycompany.web.template.commons.DateProc;
import com.mycompany.web.template.db.DBPool;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class WebInitializer implements WebApplicationInitializer {

    static Logger logger = Logger.getLogger(WebInitializer.class);
    public static String configDir;
    public static String rootDir;
    public static ServletContext context;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(WebConfig.class);
        ctx.setServletContext(servletContext);
        Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
        dispatcher.addMapping("/");
        dispatcher.setLoadOnStartup(1);

        FilterRegistration.Dynamic fr = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);

        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/*");

        context = servletContext;
        servletContext.addListener(new RequestContextListener());
        rootDir = servletContext.getRealPath("/");
        rootDir = rootDir.replaceAll("\\\\", "/");

        configDir = rootDir + "WEB-INF/classes/";
        configDir = configDir.replaceAll("\\\\", "/");
        DBPool.init();

        System.out.println("----On End [" + DateProc.createTimestamp() + "]------");
    }
}
