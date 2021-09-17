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
package org.camunda.bpm.engine.impl.persistence.entity;

import org.camunda.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.camunda.bpm.engine.impl.core.variable.scope.VariableInstanceLifecycleListener;

/**
 * @author Thorben Lindhauer
 *
 */
public class VariableInstanceConcurrentLocalInitializer
  implements VariableInstanceLifecycleListener<VariableInstanceEntity> {

  protected ExecutionEntity execution;

  public VariableInstanceConcurrentLocalInitializer(ExecutionEntity execution) {
    this.execution = execution;
  }

  @Override
  public void onCreate(VariableInstanceEntity variableInstance, AbstractVariableScope sourceScope) {
    variableInstance.setConcurrentLocal(!execution.isScope() || execution.isExecutingScopeLeafActivity());
  }

  @Override
  public void onDelete(VariableInstanceEntity variableInstance, AbstractVariableScope sourceScope) {

  }

  @Override
  public void onUpdate(VariableInstanceEntity variableInstance, AbstractVariableScope sourceScope) {

  }

}
