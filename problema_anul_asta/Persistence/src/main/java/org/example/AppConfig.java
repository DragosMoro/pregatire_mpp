package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class AppConfig {
    @Bean
    public JdbcUtils jdbcUtils() {
        Properties props = new Properties();
        props.setProperty("jdbc.url","jdbc:sqlite:game.db");
        return new JdbcUtils(props);
    }
    @Bean
    public Properties dbProperties() {
        Properties props = new Properties();
        props.setProperty("jdbc.url","jdbc:sqlite:game.db");
        return props;
    }


}
