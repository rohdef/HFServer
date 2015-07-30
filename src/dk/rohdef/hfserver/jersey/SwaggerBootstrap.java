package dk.rohdef.hfserver.jersey;

import com.wordnik.swagger.jaxrs.config.BeanConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by rohdef on 7/14/15.
 */
public class SwaggerBootstrap extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Handicapformidlingen Tutorial API");
        beanConfig.setDescription("Webservices for the tutorial project at Handicapformidlingen Aps");
        beanConfig.setLicense("MIT");
        beanConfig.setLicenseUrl("http://opensource.org/licenses/MIT");
        beanConfig.setVersion("0.0.1");
        beanConfig.setBasePath("http://rohdef.dk:8080/rest");
        beanConfig.setResourcePackage("dk.rohdef.hfserver.rest");
        beanConfig.setScan(true);
    }
}
