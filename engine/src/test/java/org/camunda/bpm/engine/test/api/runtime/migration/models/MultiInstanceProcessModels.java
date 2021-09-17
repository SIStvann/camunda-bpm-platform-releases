/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
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
package org.camunda.bpm.engine.test.api.runtime.migration.models;

import static org.camunda.bpm.engine.test.api.runtime.migration.ModifiableBpmnModelInstance.modify;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;

/**
 * @author Thorben Lindhauer
 *
 */
public class MultiInstanceProcessModels {

  public static final BpmnModelInstance PAR_MI_ONE_TASK_PROCESS =
      modify(ProcessModels.ONE_TASK_PROCESS)
        .activityBuilder("userTask")
        .multiInstance()
        .parallel()
        .cardinality("3")
        .done();

  public static final BpmnModelInstance PAR_MI_SUBPROCESS_PROCESS =
      modify(ProcessModels.SUBPROCESS_PROCESS)
        .activityBuilder("subProcess")
        .multiInstance()
        .parallel()
        .cardinality("3")
        .done();

  public static final BpmnModelInstance PAR_MI_DOUBLE_SUBPROCESS_PROCESS =
      modify(ProcessModels.DOUBLE_SUBPROCESS_PROCESS)
      .activityBuilder("outerSubProcess")
      .multiInstance()
      .parallel()
      .cardinality("3")
      .done();

  public static final BpmnModelInstance SEQ_MI_ONE_TASK_PROCESS =
      modify(ProcessModels.ONE_TASK_PROCESS)
        .activityBuilder("userTask")
        .multiInstance()
        .sequential()
        .cardinality("3")
        .done();

  public static final BpmnModelInstance SEQ_MI_SUBPROCESS_PROCESS =
      modify(ProcessModels.SUBPROCESS_PROCESS)
        .activityBuilder("subProcess")
        .multiInstance()
        .sequential()
        .cardinality("3")
        .done();

}
