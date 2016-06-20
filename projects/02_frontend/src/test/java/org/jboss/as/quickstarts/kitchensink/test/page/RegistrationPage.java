package org.jboss.as.quickstarts.kitchensink.test.page;

import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.support.FindBy;

@Location("index.jsf")
public class RegistrationPage {

    @FindBy(id = "reg")
    private RegistrationForm form;

    @FindBy(css = ".simpletablestyle")
    private RegistrationList list;

    public RegistrationForm getForm() {
        return form;
    }

    public RegistrationList getList() {
        return list;
    }
}
