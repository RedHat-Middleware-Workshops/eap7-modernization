/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.kitchensink.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.test.page.MembersTablePageFragment;
import org.jboss.as.quickstarts.kitchensink.test.page.RegistrationFormPageFragment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * Kitchensink Angular.js quickstart functional test
 *
 * @author Oliver Kiss
 */
@RunAsClient
@RunWith(Arquillian.class)
public class ClientSideMemberRegistrationTest {

    /**
     * Locator for registration form page fragment
     */
    @FindBy(name = "regForm")
    RegistrationFormPageFragment form;

    /**
     * Locator for members table page fragment
     */
    @FindByJQuery("table")
    MembersTablePageFragment table;

    /**
     * Injects browser to our test.
     */
    @Drone
    WebDriver browser;

    /**
     * Injects URL on which application is running.
     */
    @ArquillianResource
    URL contextPath;

    /**
     * Creates deployment which is sent to the container upon test's start.
     *
     * @return war file which is deployed while testing, the whole application in our case
     */
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
        		.addPackages(true, "org.jboss.as.quickstarts.kitchensink")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml")
                .merge(ShrinkWrap.create(ExplodedImporter.class).importDirectory("src/main/webapp").as(GenericArchive.class));
    }

    /**
     * Name of the member to register in the right format.
     */
    private static final String NAME_FORMAT_OK = "Jane Doe";

    /**
     * Name of the member to register in the bad format.
     */
    private static final String NAME_FORMAT_BAD = "Jane1";

    /**
     * Name of the member to register which is too long (1-25)
     */
    private static final String NAME_FORMAT_TOO_LONG = "Jane Doe Jane Doe Jane Doe";

    /**
     * E-mail of the member to register in the right format.
     */
    private static final String EMAIL_FORMAT_OK = "jane@doe.com";

    /**
     * E-mail of the member to register in the bad format.
     */
    private static final String EMAIL_FORMAT_BAD = "joe";

    /**
     * Phone number of the member to register in the right format.
     */
    private static final String PHONE_FORMAT_OK = "0123456789";

    /**
     * Phone number of the member to register in the bad format - illegal
     * characters.
     */
    private static final String PHONE_FORMAT_BAD_ILLEGAL_CHARS = "as/df.123@";

    /**
     * Phone number of the member to register in the bad format - too long.
     */
    private static final String PHONE_FORMAT_BAD_TOO_LONG = "12345678901234567890";

    /**
     * Phone nuber of the member to register in the bad format - too short
     */
    private static final String PHONE_FORMAT_BAD_TOO_SHORT = "123456789";


    @Before
    public void loadPage() {
        browser.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);

        browser.get(contextPath.toString());
        form.waitUntilPresent();
    }

    /**
     * This method tests there is no new member in the registration table when
     * all three input fields are empty.
     */
    @Test
    @InSequence(1)
    public void testEmptyRegistration() {
        form.register(new Member("", "", ""), false);
        assertEquals("Member should not be registered", 1, table.getMemberCount());
    }

    /**
     * This method tests registration of the new member with the name of bad
     * formats.
     */
    @Test
    @InSequence(2)
    public void testRegistrationWithBadNameFormat() {
        form.register(new Member(NAME_FORMAT_BAD, EMAIL_FORMAT_OK, PHONE_FORMAT_OK), false);
        form.waitForNameValidation();
        assertEquals("Member should not be registered", 1, table.getMemberCount());

        browser.get(contextPath.toString());
        form.register(new Member(NAME_FORMAT_TOO_LONG, EMAIL_FORMAT_OK, PHONE_FORMAT_OK), false);
        form.waitForNameValidation();
        assertEquals("Member should not be registered", 1, table.getMemberCount());
    }

    /**
     * This method tests registration of the new member with the email of bad
     * format.
     */
    @Test
    @InSequence(3)
    public void testRegistrationWithBadEmailFormat() {
        form.register(new Member(NAME_FORMAT_OK, EMAIL_FORMAT_BAD, PHONE_FORMAT_OK), false);
        assertFalse(form.emailValidity());
    }

    /**
     * This method tests registration of the new member with the phone of bad
     * format
     */
    @Test
    @InSequence(4)
    public void testRegistrationWithBadPhoneFormat() {
        browser.get(contextPath.toString());
        form.waitUntilPresent();
        form.register(new Member(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_ILLEGAL_CHARS), false);
        assertFalse(form.phoneValidity());

        browser.get(contextPath.toString());
        form.waitUntilPresent();
        form.register(new Member(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_TOO_SHORT), false);
        assertFalse(form.phoneValidity());

        browser.get(contextPath.toString());
        form.waitUntilPresent();
        form.register(new Member(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_TOO_LONG), false);
        assertFalse(form.phoneValidity());
    }

    /**
     * This method tests regular registration process
     */
    @Test
    @InSequence(5)
    public void testRegularRegistration() {
        Member newMember = new Member(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_OK);

        form.register(newMember, true);
        table.waitForNewMember(newMember);
    }

}
