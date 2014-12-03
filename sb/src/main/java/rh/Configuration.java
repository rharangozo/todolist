package rh;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@org.springframework.context.annotation.Configuration
@ComponentScan("rh.web")
@EnableAutoConfiguration
public class Configuration {
}
