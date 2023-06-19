package org.example;

import java.io.Serializable;

public class Configuration extends Entity<Integer> implements Serializable {

    private String configurationPattern;

    public Configuration(String configurationPattern) {
        this.configurationPattern = configurationPattern;
    }

    public Configuration() {
    }

    public String getConfigurationPattern() {
        return configurationPattern;
    }

    public void setConfigurationPattern(String configurationPattern) {
        this.configurationPattern = configurationPattern;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "configurationPattern='" + configurationPattern + '\'' +
                '}';
    }
}

