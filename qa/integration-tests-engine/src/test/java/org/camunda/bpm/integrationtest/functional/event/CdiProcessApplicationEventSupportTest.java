/*
 * Copyright © 2012 - 2018 camunda services GmbH and various authors (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.integrationtest.functional.event;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.integrationtest.functional.event.beans.CdiEventSupportProcessApplication;
import org.camunda.bpm.integrationtest.functional.event.beans.EventObserverCdiBean;
import org.camunda.bpm.integrationtest.functional.event.beans.ExecutionListenerProcessApplication;
import org.camunda.bpm.integrationtest.util.AbstractFoxPlatformIntegrationTest;
import org.camunda.bpm.integrationtest.util.DeploymentHelper;
import org.camunda.bpm.integrationtest.util.TestContainer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Daniel Meyer
 *
 */
@RunWith(Arquillian.class)
public class CdiProcessApplicationEventSupportTest extends AbstractFoxPlatformIntegrationTest {

  @Deployment
  public static WebArchive createDeployment() {
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
        .addAsLibraries(DeploymentHelper.getEngineCdi())
        .addAsResource("META-INF/processes.xml", "META-INF/processes.xml")
        .addClass(AbstractFoxPlatformIntegrationTest.class)
        .addClass(CdiEventSupportProcessApplication.class)
        .addClass(EventObserverCdiBean.class)
        .addAsResource("org/camunda/bpm/integrationtest/functional/event/ProcessApplicationEventSupportTest.testExecutionListener.bpmn20.xml");

    TestContainer.addContainerSpecificResourcesForNonPa(archive);

    return archive;

  }

  @Test
  public void testEventListener() {
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testProcess");

    Integer listenerInvocationCount = (Integer) runtimeService.getVariable(processInstance.getId(), ExecutionListenerProcessApplication.LISTENER_INVOCATION_COUNT);
    Assert.assertNotNull(listenerInvocationCount);
    Assert.assertEquals(6, listenerInvocationCount.intValue());

    Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
    taskService.setAssignee(task.getId(), "demo");
    listenerInvocationCount = (Integer) runtimeService.getVariable(processInstance.getId(), ExecutionListenerProcessApplication.LISTENER_INVOCATION_COUNT);
    Assert.assertEquals(7, listenerInvocationCount.intValue());
  }

}
