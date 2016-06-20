package org.jboss.as.quickstarts.kitchensink.test.page;

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegistrationItem {

    @Root
    private WebElement root;

    @FindBy(xpath = "tr/td[2]")
    private WebElement name;
    
    @FindBy(xpath = "tr/td[3]")
    private WebElement email;

    @FindBy(xpath = "tr/td[4]")
    private WebElement phone;
    
    public String getName() {
        return name.getText();
    }
    
    public String getEmail() {
        return email.getText();
    }
    
    public String getPhone() {
        return phone.getText();
    }
}
