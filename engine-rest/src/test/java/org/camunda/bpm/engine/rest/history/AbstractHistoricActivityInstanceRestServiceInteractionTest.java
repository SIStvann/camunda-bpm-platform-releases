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
package org.camunda.bpm.engine.rest.history;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response.Status;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricActivityInstanceQuery;
import org.camunda.bpm.engine.rest.AbstractRestServiceTest;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.helper.MockProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public abstract class AbstractHistoricActivityInstanceRestServiceInteractionTest extends AbstractRestServiceTest {

  protected static final String HISTORIC_ACTIVITY_INSTANCE_URL = TEST_RESOURCE_ROOT_PATH + "/history/activity-instance";
  protected static final String HISTORIC_SINGLE_ACTIVITY_INSTANCE_URL = HISTORIC_ACTIVITY_INSTANCE_URL + "/{id}";

  protected HistoryService historyServiceMock;
  protected HistoricActivityInstance historicInstanceMock;
  protected HistoricActivityInstanceQuery historicQueryMock;

  @Before
  public void setUpRuntimeData() {
    historyServiceMock = mock(HistoryService.class);

    // runtime service
    when(processEngine.getHistoryService()).thenReturn(historyServiceMock);

    historicInstanceMock = MockProvider.createMockHistoricActivityInstance();
    historicQueryMock = mock(HistoricActivityInstanceQuery.class);

    when(historyServiceMock.createHistoricActivityInstanceQuery()).thenReturn(historicQueryMock);
    when(historicQueryMock.activityInstanceId(anyString())).thenReturn(historicQueryMock);
    when(historicQueryMock.singleResult()).thenReturn(historicInstanceMock);
  }

  @Test
  public void testGetSingleHistoricCaseInstance() {
    Response response = given()
        .pathParam("id", MockProvider.EXAMPLE_HISTORIC_ACTIVITY_INSTANCE_ID)
      .then().expect()
        .statusCode(Status.OK.getStatusCode())
      .when()
        .get(HISTORIC_SINGLE_ACTIVITY_INSTANCE_URL);

    String content = response.asString();

    String returnedId = from(content).getString("id");
    String returnedParentActivityInstanceId = from(content).getString("parentActivityInstanceId");
    String returnedActivityId = from(content).getString("activityId");
    String returnedActivityName = from(content).getString("activityName");
    String returnedActivityType = from(content).getString("activityType");
    String returnedProcessDefinitionId = from(content).getString("processDefinitionId");
    String returnedProcessInstanceId = from(content).getString("processInstanceId");
    String returnedExecutionId = from(content).getString("executionId");
    String returnedTaskId = from(content).getString("taskId");
    String returnedCalledProcessInstanceId = from(content).getString("calledProcessInstanceId");
    String returnedAssignee = from(content).getString("assignee");
    String returnedStartTime = from(content).getString("startTime");
    String returnedEndTime = from(content).getString("endTime");
    long returnedDurationInMillis = from(content).getLong("durationInMillis");
    boolean canceled = from(content).getBoolean("canceled");
    boolean completeScope = from(content).getBoolean("completeScope");

    Assert.assertEquals(MockProvider.EXAMPLE_HISTORIC_ACTIVITY_INSTANCE_ID, returnedId);
    Assert.assertEquals(MockProvider.EXAMPLE_HISTORIC_ACTIVITY_INSTANCE_PARENT_ACTIVITY_INSTANCE_ID, returnedParentActivityInstanceId);
    Assert.assertEquals(MockProvider.EXAMPLE_ACTIVITY_ID, returnedActivityId);
    Assert.assertEquals(MockProvider.EXAMPLE_ACTIVITY_NAME, returnedActivityName);
    Assert.assertEquals(MockProvider.EXAMPLE_ACTIVITY_TYPE, returnedActivityType);
    Assert.assertEquals(MockProvider.EXAMPLE_PROCESS_DEFINITION_ID, returnedProcessDefinitionId);
    Assert.assertEquals(MockProvider.EXAMPLE_PROCESS_INSTANCE_ID, returnedProcessInstanceId);
    Assert.assertEquals(MockProvider.EXAMPLE_EXECUTION_ID, returnedExecutionId);
    Assert.assertEquals(MockProvider.EXAMPLE_TASK_ID, returnedTaskId);
    Assert.assertEquals(MockProvider.EXAMPLE_HISTORIC_ACTIVITY_INSTANCE_CALLED_PROCESS_INSTANCE_ID, returnedCalledProcessInstanceId);
    Assert.assertEquals(MockProvider.EXAMPLE_TASK_ASSIGNEE_NAME, returnedAssignee);
    Assert.assertEquals(MockProvider.EXAMPLE_HISTORIC_ACTIVITY_INSTANCE_START_TIME, returnedStartTime);
    Assert.assertEquals(MockProvider.EXAMPLE_HISTORIC_ACTIVITY_INSTANCE_END_TIME, returnedEndTime);
    Assert.assertEquals(MockProvider.EXAMPLE_HISTORIC_ACTIVITY_INSTANCE_DURATION, returnedDurationInMillis);
    Assert.assertEquals(MockProvider.EXAMPLE_HISTORIC_ACTIVITY_INSTANCE_IS_CANCELED, canceled);
    Assert.assertEquals(MockProvider.EXAMPLE_HISTORIC_ACTIVITY_INSTANCE_IS_COMPLETE_SCOPE, completeScope);
  }

  @Test
  public void testGetNonExistingHistoricCaseInstance() {
    when(historicQueryMock.singleResult()).thenReturn(null);

    given()
      .pathParam("id", MockProvider.NON_EXISTING_ID)
    .then().expect()
      .statusCode(Status.NOT_FOUND.getStatusCode())
      .contentType(ContentType.JSON)
      .body("type", equalTo(InvalidRequestException.class.getSimpleName()))
      .body("message", equalTo("Historic activity instance with id '" + MockProvider.NON_EXISTING_ID + "' does not exist"))
    .when()
      .get(HISTORIC_SINGLE_ACTIVITY_INSTANCE_URL);
  }

}
