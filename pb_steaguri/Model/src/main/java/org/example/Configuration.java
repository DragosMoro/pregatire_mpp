package org.example;

import java.io.Serializable;

public class Configuration extends Entity<Integer> implements Serializable {
    private String flagConfiguration;


    public Configuration(){}

    public Configuration(String flagConfiguration){
        this.flagConfiguration = flagConfiguration;
    }

    public String getFlagConfiguration(){
        return flagConfiguration;
    }

    public void setFlagConfiguration(String flagConfiguration){
        this.flagConfiguration = flagConfiguration;
    }


    @Override
    public String toString() {
        return "Configuration{" +
                "flagConfiguration='" + flagConfiguration + '\'' +
                '}';
    }
}
