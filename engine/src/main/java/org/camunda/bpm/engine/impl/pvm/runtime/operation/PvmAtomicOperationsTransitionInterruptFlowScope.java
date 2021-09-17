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
package org.camunda.bpm.engine.impl.pvm.runtime.operation;

import org.camunda.bpm.engine.impl.pvm.PvmActivity;
import org.camunda.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

/**
 * In BPMN this is used for terminate end events
 *
 * @author Daniel Meyer
 *
 */
public class PvmAtomicOperationsTransitionInterruptFlowScope extends PvmAtomicOperationInterruptScope {

  public String getCanonicalName() {
    return "transition-interrupt-scope";
  }

  protected void scopeInterrupted(PvmExecutionImpl execution) {
    execution.dispatchDelayedEventsAndPerformOperation(TRANSITION_CREATE_SCOPE);
  }

  protected PvmActivity getInterruptingActivity(PvmExecutionImpl execution) {
    return execution.getTransition().getDestination();
  }

}
