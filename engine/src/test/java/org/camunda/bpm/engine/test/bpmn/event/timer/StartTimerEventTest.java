/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.test.bpmn.event.timer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cmd.DeleteJobsCmd;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.impl.test.PluggableProcessEngineTestCase;
import org.camunda.bpm.engine.impl.util.ClockUtil;
import org.camunda.bpm.engine.impl.util.IoUtil;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.JobQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.test.Deployment;

/**
 * @author Joram Barrez
 */
public class StartTimerEventTest extends PluggableProcessEngineTestCase {

  @Deployment
  public void testDurationStartTimerEvent() throws Exception {

    // Set the clock fixed
    Date startTime = new Date();

    // After process start, there should be timer created
    JobQuery jobQuery = managementService.createJobQuery();
    assertEquals(1, jobQuery.count());

    // After setting the clock to time '50minutes and 5 seconds', the second timer should fire
    ClockUtil.setCurrentTime(new Date(startTime.getTime() + ((50 * 60 * 1000) + 5000)));
    
    executeAllJobs();   
    
    executeAllJobs();

    List<ProcessInstance> pi = runtimeService.createProcessInstanceQuery().processDefinitionKey("startTimerEventExample")
        .list();
    assertEquals(1, pi.size());

    assertEquals(0, jobQuery.count());


  }

  @Deployment
  public void testFixedDateStartTimerEvent() throws Exception {

    // After process start, there should be timer created
    JobQuery jobQuery = managementService.createJobQuery();
    assertEquals(1, jobQuery.count());

    ClockUtil.setCurrentTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("15/11/2036 11:12:30"));
    executeAllJobs();

    List<ProcessInstance> pi = runtimeService.createProcessInstanceQuery().processDefinitionKey("startTimerEventExample")
        .list();
    assertEquals(1, pi.size());

    assertEquals(0, jobQuery.count());


  }

  // FIXME: This test likes to run in an endless loop when invoking the waitForJobExecutorOnCondition method
  @Deployment
  public void FAILING_testCycleDateStartTimerEvent() throws Exception {
    ClockUtil.setCurrentTime(new Date());

    // After process start, there should be timer created
    JobQuery jobQuery = managementService.createJobQuery();    
    assertEquals(1, jobQuery.count());
    
    final ProcessInstanceQuery piq = runtimeService.createProcessInstanceQuery().processDefinitionKey("startTimerEventExample");
    
    assertEquals(0, piq.count());
    
    moveByMinutes(5);
    executeAllJobs();
    assertEquals(1, piq.count());    
    assertEquals(1, jobQuery.count());

    moveByMinutes(5);
    executeAllJobs();
    assertEquals(1, piq.count());
    
    assertEquals(1, jobQuery.count());
    //have to manually delete pending timer
    cleanDB();

  }

  
  private void moveByMinutes(int minutes) throws Exception {
    ClockUtil.setCurrentTime(new Date(ClockUtil.getCurrentTime().getTime() + ((minutes * 60 * 1000) + 5000)));
  }

  @Deployment
  public void testCycleWithLimitStartTimerEvent() throws Exception {
    ClockUtil.setCurrentTime(new Date());

    // After process start, there should be timer created
    JobQuery jobQuery = managementService.createJobQuery();
    assertEquals(1, jobQuery.count());

    final ProcessInstanceQuery piq = runtimeService.createProcessInstanceQuery().processDefinitionKey("startTimerEventExampleCycle");
    
    assertEquals(0, piq.count());
    
    moveByMinutes(5);
    executeAllJobs();
    assertEquals(1, piq.count());
    assertEquals(1, jobQuery.count());

    moveByMinutes(5);
    executeAllJobs();
    assertEquals(2, piq.count());
    assertEquals(0, jobQuery.count());

  }
  
  @Deployment
  public void testExpressionStartTimerEvent() throws Exception {
    // ACT-1415: fixed start-date is an expression
    JobQuery jobQuery = managementService.createJobQuery();
    assertEquals(1, jobQuery.count());

    ClockUtil.setCurrentTime(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("15/11/2036 11:12:30"));
    executeAllJobs();

    List<ProcessInstance> pi = runtimeService.createProcessInstanceQuery().processDefinitionKey("startTimerEventExample")
        .list();
    assertEquals(1, pi.size());

    assertEquals(0, jobQuery.count());
  }
  
  @Deployment
  public void testVersionUpgradeShouldCancelJobs() throws Exception {
    ClockUtil.setCurrentTime(new Date());

    // After process start, there should be timer created
    JobQuery jobQuery = managementService.createJobQuery();
    assertEquals(1, jobQuery.count());

    //we deploy new process version, with some small change
    InputStream in = getClass().getResourceAsStream("StartTimerEventTest.testVersionUpgradeShouldCancelJobs.bpmn20.xml");
    String process = new String(IoUtil.readInputStream(in, "")).replaceAll("beforeChange","changed");
    IoUtil.closeSilently(in);
    in = new ByteArrayInputStream(process.getBytes());
    String id = repositoryService.createDeployment().addInputStream("StartTimerEventTest.testVersionUpgradeShouldCancelJobs.bpmn20.xml",
        in).deploy().getId();
    IoUtil.closeSilently(in);

    assertEquals(1, jobQuery.count());

    moveByMinutes(5);
    executeAllJobs();
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("startTimerEventExample").singleResult();
    String pi = processInstance.getProcessInstanceId();
    assertEquals("changed", runtimeService.getActiveActivityIds(pi).get(0));

    assertEquals(1, jobQuery.count());

    cleanDB();
    repositoryService.deleteDeployment(id, true);
  }
  
  @Deployment
  public void testTimerShouldNotBeRecreatedOnDeploymentCacheReboot() {
    
    // Just to be sure, I added this test. Sounds like something that could easily happen
    // when the order of deploy/parsing is altered.
    
    // After process start, there should be timer created
    JobQuery jobQuery = managementService.createJobQuery();
    assertEquals(1, jobQuery.count());
    
    // Reset deployment cache
    ((ProcessEngineConfigurationImpl) processEngineConfiguration).getDeploymentCache().discardProcessDefinitionCache();
    
    // Start one instance of the process definition, this will trigger a cache reload
    runtimeService.startProcessInstanceByKey("startTimer");
    
    // No new jobs should have been created
    assertEquals(1, jobQuery.count());
  }
  
  // Test for ACT-1533
  public void testTimerShouldNotBeRemovedWhenUndeployingOldVersion() throws Exception {
    // Deploy test process
    InputStream in = getClass().getResourceAsStream("StartTimerEventTest.testTimerShouldNotBeRemovedWhenUndeployingOldVersion.bpmn20.xml");
    String process = new String(IoUtil.readInputStream(in, ""));
    IoUtil.closeSilently(in);
    
    in = new ByteArrayInputStream(process.getBytes());
    String firstDeploymentId = repositoryService.createDeployment().addInputStream("StartTimerEventTest.testVersionUpgradeShouldCancelJobs.bpmn20.xml",
            in).deploy().getId();
    IoUtil.closeSilently(in);
    
    // After process start, there should be timer created
    JobQuery jobQuery = managementService.createJobQuery();
    assertEquals(1, jobQuery.count());

    //we deploy new process version, with some small change
    String processChanged = process.replaceAll("beforeChange","changed");
    in = new ByteArrayInputStream(processChanged.getBytes());
    String secondDeploymentId = repositoryService.createDeployment().addInputStream("StartTimerEventTest.testVersionUpgradeShouldCancelJobs.bpmn20.xml",
        in).deploy().getId();
    IoUtil.closeSilently(in);
    assertEquals(1, jobQuery.count());

    // Remove the first deployment
    repositoryService.deleteDeployment(firstDeploymentId, true);
    
    // The removal of an old version should not affect timer deletion
    // ACT-1533: this was a bug, and the timer was deleted!
    assertEquals(1, jobQuery.count());
    
    // Cleanup
    cleanDB();
    repositoryService.deleteDeployment(secondDeploymentId, true);
  }
  
  // util methods ////////////////////////////////////////
  
  /** executes all jobs in this threads until they are either done or retries are exhausted. */
  protected void executeAllJobs() {
    String nextJobId = getNextExecutableJobId();
    
    while(nextJobId != null) {      
      try {
        managementService.executeJob(nextJobId);
      } catch(Throwable t) { /* ignore */ }      
      nextJobId = getNextExecutableJobId();
    }
    
  }

  protected String getNextExecutableJobId() {
    List<Job> jobs = managementService.createJobQuery().executable().listPage(0, 1);
    if(jobs.size() == 1) {
      return jobs.get(0).getId();      
    } else {
      return null;      
    }
  }
      
  private void cleanDB() {
    String jobId = managementService.createJobQuery().singleResult().getId();
    CommandExecutor commandExecutor = processEngineConfiguration.getCommandExecutorTxRequired();
    commandExecutor.execute(new DeleteJobsCmd(jobId));
  }

}