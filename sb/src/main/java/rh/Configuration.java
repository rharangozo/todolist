package rh;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//TODO: configure the rest to return with json and xml representation as well

@org.springframework.context.annotation.Configuration
@ComponentScan("rh.web")
@EnableAutoConfiguration
public class Configuration {
}
