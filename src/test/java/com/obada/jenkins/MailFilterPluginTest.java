package com.obada.jenkins;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.obada.jenkins.mailfilterplugin.MailFilterPluginConfiguration;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.jvnet.hudson.test.RestartableJenkinsRule;

public class MailFilterPluginTest {

    @Rule
    public RestartableJenkinsRule rr = new RestartableJenkinsRule();

    @Test
    public void uiAndStorage() {
        rr.then(r -> {
            assertNull("not set initially", MailFilterPluginConfiguration.get().getFilterText());
            HtmlForm config = r.createWebClient().goTo("configure").getFormByName("config");
            HtmlTextArea textbox = config.getTextAreaByName("_.filterText");
            textbox.setText("user1");
            r.submit(config);
            assertEquals("global config page let us edit it", "user1", MailFilterPluginConfiguration.get().getFilterText());
        });
        rr.then(r -> {
            assertEquals("still there after restart of Jenkins", "user1", MailFilterPluginConfiguration.get().getFilterText());
        });
    }

}
