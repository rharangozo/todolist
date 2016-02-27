package rh;

import javax.sql.DataSource;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import rh.web.handler.AuthorizationInterceptor;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

//TODO 3: configure the rest to return with json and xml representation as well

@org.springframework.context.annotation.Configuration
@ComponentScan({"rh.web", "rh.persistence"})
@EnableAutoConfiguration
@EnableTransactionManagement
@PropertySource("classpath:/application.properties") //TODO 3 : I am not sure that it is ok to mix the app configs with property placeholder resolution logic...
public class Configuration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthHandler()).
            addPathPatterns("/**").
            excludePathPatterns("/login");
    }

    @Bean
    public HandlerInterceptor getAuthHandler() {
        return new AuthorizationInterceptor();
    }

    @Bean
    public ViewResolver getJspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");

        return resolver;
    }

    @Bean
    public DataSource getDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:database-schema.sql")
                .addScript("classpath:insert-test-tasks.sql")
                .build();
    }

    @Bean
    public HttpTransport getHttpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    public JsonFactory getJsonFactory() {
        return JacksonFactory.getDefaultInstance();
    }

    @Bean
    public GoogleIdTokenVerifier getGoogleIdTokenVerifier() throws GeneralSecurityException, IOException {
        return new GoogleIdTokenVerifier.Builder(getHttpTransport(), getJsonFactory())
                //TODO: load it from property
            .setAudience(Arrays.asList("651917160024-8lmki27a1rkjpbkctbrd28jcigoojebf.apps.googleusercontent.com"))
            .setIssuer("accounts.google.com")
            .build();
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
