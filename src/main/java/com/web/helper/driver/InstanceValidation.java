package com.web.helper.driver;

/**
 * Created by Navya on 23-08-2020.
 */
public enum  InstanceValidation {
    INSTANCE;

    public int getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(int instanceCount) {
        this.instanceCount = instanceCount;
    }

    private int instanceCount=999;
}
