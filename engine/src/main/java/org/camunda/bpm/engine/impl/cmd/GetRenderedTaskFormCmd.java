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

package org.camunda.bpm.engine.impl.cmd;

import java.io.Serializable;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.form.engine.FormEngine;
import org.camunda.bpm.engine.impl.form.handler.TaskFormHandler;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;

import static org.camunda.bpm.engine.impl.util.EnsureUtil.ensureNotNull;


/**
 * @author Tom Baeyens
 */
public class GetRenderedTaskFormCmd  implements Command<Object>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String taskId;
  protected String formEngineName;
  
  public GetRenderedTaskFormCmd(String taskId, String formEngineName) {
    this.taskId = taskId;
    this.formEngineName = formEngineName;
  }


  public Object execute(CommandContext commandContext) {
    TaskEntity task = Context
      .getCommandContext()
      .getTaskManager()
      .findTaskById(taskId);
    ensureNotNull("Task '" + taskId + "' not found", "task", task);

    ensureNotNull("Task form definition for '" + taskId + "' not found", "task.getTaskDefinition()", task.getTaskDefinition());

    TaskFormHandler taskFormHandler = task.getTaskDefinition().getTaskFormHandler();
    if (taskFormHandler == null) {
      return null;
    }

    FormEngine formEngine = Context
      .getProcessEngineConfiguration()
      .getFormEngines()
      .get(formEngineName);

    ensureNotNull("No formEngine '" + formEngineName + "' defined process engine configuration", "formEngine", formEngine);

    TaskFormData taskForm = taskFormHandler.createTaskForm(task);

    return formEngine.renderTaskForm(taskForm);
  }
}
