package cz.muni.fi.dictatetrainer.commontests.utils;

import org.junit.Ignore;

@Ignore
public enum ResourceDefinitions {
    CATEGORY("categories"),
    USER("users"),
    DICTATE("dictates"),
    TRIAL("trials"),
    ERROR("errors"),
    SCHOOL("schools"),
    SCHOOLCLASS("schoolclasses");

    private String resourceName;

    private ResourceDefinitions(final String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
