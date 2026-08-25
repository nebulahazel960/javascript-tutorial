package org.dromara.fai.config;

import java.io.Serializable;

public class FaiMessageConfiguration implements Serializable {
    
    private String system;
    
    private String user;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
