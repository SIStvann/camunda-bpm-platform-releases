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
package org.camunda.bpm.engine.rest.sub.history.impl;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.rest.dto.history.HistoricProcessInstanceDto;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.sub.history.HistoricProcessInstanceResource;

import javax.ws.rs.core.Response.Status;

public class HistoricProcessInstanceResourceImpl implements HistoricProcessInstanceResource {

  private ProcessEngine engine;
  private String processInstanceId;

  public HistoricProcessInstanceResourceImpl(ProcessEngine engine, String processInstanceId) {
    this.engine = engine;
    this.processInstanceId = processInstanceId;
  }

  @Override
  public HistoricProcessInstanceDto getHistoricProcessInstance() {
    HistoryService historyService = engine.getHistoryService();
    HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

    if (instance == null) {
      throw new InvalidRequestException(Status.NOT_FOUND, "Historic process instance with id " + processInstanceId + " does not exist");
    }

    return HistoricProcessInstanceDto.fromHistoricProcessInstance(instance);
  }

}
