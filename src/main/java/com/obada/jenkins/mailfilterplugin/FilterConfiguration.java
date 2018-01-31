package com.obada.jenkins.mailfilterplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilterConfiguration {

    private Set<String> filteredUsers = new HashSet<String>();

    FilterConfiguration(String filterText) {

        try {
            final Reader configReader;
            if (isFilename(filterText)) {
                configReader = new FileReader(filterText);
            } else {
                configReader = new StringReader(filterText);
            }

            loadConfiguration(configReader);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected boolean isFilename(String filterText) {

        return filterText.startsWith(File.separator);

    }

    protected void loadConfiguration(final Reader filterConfiguration) throws IOException {

        final BufferedReader reader = new BufferedReader(filterConfiguration);
        try {
            String s;
            while ((s = reader.readLine()) != null) {
                if (isComment(s)) {
                    continue;
                }
                final String address = s.trim();
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Email disabled for '" + address + "'");
                }
                filteredUsers.add(address);
            }
        } finally {
            reader.close();
        }

    }

    private boolean isComment(String line) {

        String s = line.trim();

        return s.isEmpty() || s.startsWith("#");

    }

    public boolean isFiltered(String address) {

        return filteredUsers.contains(address);
    }

    private static final Logger LOGGER = Logger.getLogger(FilterConfiguration.class.getName());

}
