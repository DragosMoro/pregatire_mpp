package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Properties;

@Configuration
public class AppConfig {
    @Bean
    public JdbcUtils jdbcUtils() {
        Properties props = new Properties();
        props.setProperty("jdbc.url","jdbc:sqlite:Server/game.db");
        return new JdbcUtils(props);
    }
    @Bean
    @Primary
    public Properties dbProperties() {
        Properties props = new Properties();
        props.setProperty("jdbc.url","jdbc:sqlite:Server/game.db");
        return props;
    }


}
