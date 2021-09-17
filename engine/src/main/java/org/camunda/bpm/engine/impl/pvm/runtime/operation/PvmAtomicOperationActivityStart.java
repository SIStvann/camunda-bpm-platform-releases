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

package org.camunda.bpm.engine.impl.pvm.runtime.operation;

import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.pvm.runtime.ExecutionStartContext;
import org.camunda.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;


/**
 * @author Tom Baeyens
 */
public class PvmAtomicOperationActivityStart extends PvmAtomicOperationActivityInstanceStart {

  @Override
  protected void eventNotificationsCompleted(PvmExecutionImpl execution) {
    super.eventNotificationsCompleted(execution);

    ExecutionStartContext executionStartContext = execution.getExecutionStartContext();
    if (executionStartContext != null) {
      executionStartContext.executionStarted(execution);
      execution.disposeExecutionStartContext();
    }

    execution.performOperation(ACTIVITY_EXECUTE);
  }

  protected String getEventName() {
    return ExecutionListener.EVENTNAME_START;
  }

  protected ScopeImpl getScope(PvmExecutionImpl execution) {
    return execution.getActivity();
  }

  public String getCanonicalName() {
    return "activity-start";
  }

}
