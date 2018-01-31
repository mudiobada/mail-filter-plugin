package com.obada.jenkins.mailfilterplugin;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import jenkins.plugins.mailer.tasks.MailAddressFilter;
import jenkins.plugins.mailer.tasks.MailAddressFilterDescriptor;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.internet.InternetAddress;

@Extension
public class MailFilterPlugin extends MailAddressFilter implements Serializable {

    private final FilterConfiguration config;

    public MailFilterPlugin() {

        final String filterText = getFilterText();

        this.config = new FilterConfiguration(filterText);

    }

    private String getFilterText() {

        final MailFilterPluginConfiguration configuration = MailFilterPluginConfiguration.get();

        final String filterText = configuration.getFilterText();

        if (filterText != null) {
            return filterText;
        } else {
            return "";
        }

    }

    @Override
    public boolean isFiltered(AbstractBuild<?, ?> build, BuildListener listener, InternetAddress address) {

        boolean isFiltered = false;

        if (config.isFiltered(address.toString())) {
            isFiltered = true;
        } else {
            final String userName = getUserName(address);

            if ((userName != null) && config.isFiltered(userName)) {
                isFiltered = true;
            }
        }

        if (isFiltered) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Filtered recipient: " + address.toString());
            }
        }

        return isFiltered;

    }

    /**
     * Get userName from address.
     */
    private static String getUserName(InternetAddress address) {

        String userName = address.toString();

        String addressTokens[] = address.toString().split("@", 2);
        if (addressTokens.length > 0) {
            userName = addressTokens[0];
        }

        return userName;

    }

    @Extension
    public static class DescriptorImpl extends MailAddressFilterDescriptor {

        @Override
        public String getDisplayName() {

            return "MailFilterPlugin";
        }
    }

    private static final Logger LOGGER = Logger.getLogger(MailFilterPlugin.class.getName());

}
