package org.jboss.as.quickstarts.kitchensink.test.page;

import java.util.List;

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegistrationList {

    @Root
    private WebElement root;
    
    @FindBy(tagName = "tbody")
    private List<RegistrationItem> items;
    
    public int getRegistrationCount() {
        return items.size();
    }
    
    public RegistrationItem getRegistraion(int index) {
        return items.get(index);
    }
}
