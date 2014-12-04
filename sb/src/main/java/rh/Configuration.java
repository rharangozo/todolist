package rh;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

//TODO: configure the rest to return with json and xml representation as well

@org.springframework.context.annotation.Configuration
@ComponentScan("rh.web")
@EnableAutoConfiguration
public class Configuration {
    
    @Bean
    public ViewResolver getJspViewResolver() {
        
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        
        return resolver;
    }
}
