package cz.muni.fi.dictatetrainer.commontests.utils;

import org.junit.Ignore;

@Ignore
public enum ResourceDefinitions {
    CATEGORY("categories"),
    USER("users"),
    DICTATE("dictates"),
    TRIAL("trials");

    private String resourceName;

    private ResourceDefinitions(final String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
