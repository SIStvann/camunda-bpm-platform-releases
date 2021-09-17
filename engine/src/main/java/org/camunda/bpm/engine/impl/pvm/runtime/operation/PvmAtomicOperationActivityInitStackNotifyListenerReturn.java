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

import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.pvm.runtime.ExecutionStartContext;
import org.camunda.bpm.engine.impl.pvm.runtime.InstantiationStack;
import org.camunda.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

/**
 * @author Thorben Lindhauer
 *
 */
public class PvmAtomicOperationActivityInitStackNotifyListenerReturn extends PvmAtomicOperationActivityInstanceStart {

  public String getCanonicalName() {
    return "activity-init-stack-notify-listener-return";
  }

  protected ScopeImpl getScope(PvmExecutionImpl execution) {
    ActivityImpl activity = execution.getActivity();

    if (activity!=null) {
      return activity;
    } else {
      PvmExecutionImpl parent = execution.getParent();
      if (parent != null) {
        return getScope(execution.getParent());
      }
      return execution.getProcessDefinition();
    }
  }

  protected String getEventName() {
    return ExecutionListener.EVENTNAME_START;
  }

  protected void eventNotificationsCompleted(PvmExecutionImpl execution) {
    super.eventNotificationsCompleted(execution);

    ExecutionStartContext startContext = execution.getExecutionStartContext();
    InstantiationStack instantiationStack = startContext.getInstantiationStack();

    // if the stack has been instantiated
    if (instantiationStack.getActivities().isEmpty()) {
      // done
      return;
    }
    else {
      // else instantiate the activity stack further
      execution.setActivity(null);
      execution.performOperation(ACTIVITY_INIT_STACK_AND_RETURN);

    }

  }

}
