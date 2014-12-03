package rh;

import org.springframework.boot.SpringApplication;


public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(Configuration.class, args);
    }
}
