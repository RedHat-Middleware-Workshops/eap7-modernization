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

import org.arquillian.recorder.reporter.ReportMessage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.kitchensink.test.page.RegistrationForm;
import org.jboss.as.quickstarts.kitchensink.test.page.RegistrationItem;
import org.jboss.as.quickstarts.kitchensink.test.page.RegistrationList;
import org.jboss.as.quickstarts.kitchensink.test.page.RegistrationPage;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;


@RunWith(Arquillian.class)
public class RegistrationUITestCase {

    @Deployment(testable = false)
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
            .addPackages(true, "org.jboss.as.quickstarts.kitchensink")
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsWebInfResource("test-ds.xml")
            .merge(ShrinkWrap.create(ExplodedImporter.class).importDirectory("src/main/webapp").as(GenericArchive.class));
    }
    
    @Drone
    private WebDriver driver;
    
    @Test
    @ReportMessage("Verify Registration complete with valid data")
    public void shouldBeAbleToRegister(@InitialPage RegistrationPage page) throws Exception {
    
        String name = "Aslak Knutsen";

        RegistrationForm form = page.getForm();
        form.setName(name);
        form.setEMail("xx@email.com");
        form.setPhoneNumber("5550056789");
        
        form.register();
        
        Assert.assertTrue(form.isValid());
        
        RegistrationList list = page.getList();
        Assert.assertEquals(1, list.getRegistrationCount());

        RegistrationItem item = list.getRegistraion(0);
        Assert.assertEquals(name, item.getName());
    }

    @Test
    @ReportMessage("Verify Registration form validate PhoneNumber input")
    public void shouldValidatePhoneNumber(@InitialPage RegistrationPage page) throws Exception {
    
        RegistrationForm form = page.getForm();
        form.setName("Aslak Knutsen");
        form.setEMail("xx@email.com");
        form.setPhoneNumber("555-56789-999");
        
        form.register();
        
        Assert.assertFalse(form.isValid());
    }

}
