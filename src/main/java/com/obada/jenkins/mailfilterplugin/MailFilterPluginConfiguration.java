package com.obada.jenkins.mailfilterplugin;

import hudson.Extension;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

@Extension
public class MailFilterPluginConfiguration extends GlobalConfiguration {

    private String filterText;

    /** @return the singleton instance */
    public static MailFilterPluginConfiguration get() {

        return GlobalConfiguration.all().get(MailFilterPluginConfiguration.class);
    }

    public MailFilterPluginConfiguration() {

        // When Jenkins is restarted, load any saved configuration from disk.
        load();
    }

    public String getFilterText() {

        return filterText;
    }

    @DataBoundSetter
    public void setFilterText(String filterText) {

        this.filterText = filterText;
        save();
    }

    public FormValidation doCheckFilterText(@QueryParameter String value) {

        if (StringUtils.isEmpty(value)) {
            return FormValidation.warning("Please specify a filterText.");
        }
        return FormValidation.ok();
    }

}
