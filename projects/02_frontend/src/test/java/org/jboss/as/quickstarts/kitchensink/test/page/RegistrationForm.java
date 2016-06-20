package org.jboss.as.quickstarts.kitchensink.test.page;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegistrationForm {

	@Root
	private WebElement root;
	
	@FindBy(id = "reg:name")
	private WebElement name;
	
	@FindBy(id = "reg:email")
	private WebElement email;
	
    @FindBy(id = "reg:phoneNumber")
    private WebElement phone;
    

	@FindBy(id = "reg:register")
	private WebElement register;
	
    @FindBy(css = ".invalid")
    private GrapheneElement invalid;

	public RegistrationForm setName(String name) {
		this.name.sendKeys(name);
		return this;
	}

	public RegistrationForm setEMail(String email) {
		this.email.sendKeys(email);
		return this;
	}

	public RegistrationForm setPhoneNumber(String phone) {
		this.phone.sendKeys(phone);
		return this;
	}

	public void register() {
		register.click();
	}
	
	public boolean isValid() {
	    return !invalid.isPresent();
	}
	
	public String getInvalidMessage() {
	    return invalid.getText();
	}
}
