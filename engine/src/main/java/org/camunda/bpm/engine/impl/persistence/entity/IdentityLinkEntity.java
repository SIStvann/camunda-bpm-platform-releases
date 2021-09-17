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

import java.io.Serializable;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.db.DbEntity;
import org.camunda.bpm.engine.task.IdentityLink;


/**
 * @author Joram Barrez
 */
public class IdentityLinkEntity implements Serializable, IdentityLink, DbEntity {
  
  private static final long serialVersionUID = 1L;
  
  protected String id;
  
  protected String type;
  
  protected String userId;
  
  protected String groupId;
  
  protected String taskId;
  
  protected String processDefId;
  
  protected TaskEntity task;
  
  protected ProcessDefinitionEntity processDef;

  public Object getPersistentState() {
    return this.type;
  }
  
  public static IdentityLinkEntity createAndInsert() {
    IdentityLinkEntity identityLinkEntity = new IdentityLinkEntity();
    Context
      .getCommandContext()
      .getDbEntityManager()
      .insert(identityLinkEntity);
    return identityLinkEntity;
  }
  
  public boolean isUser() {
    return userId != null;
  }
  
  public boolean isGroup() {
    return groupId != null;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }

  public String getUserId() {
    return userId;
  }
  
  public void setUserId(String userId) {
    if (this.groupId != null && userId != null) {
      throw new ProcessEngineException("Cannot assign a userId to a task assignment that already has a groupId");
    }
    this.userId = userId;
  }
  
  public String getGroupId() {
    return groupId;
  }
  
  public void setGroupId(String groupId) {
    if (this.userId != null && groupId != null) {
      throw new ProcessEngineException("Cannot assign a groupId to a task assignment that already has a userId");
    }
    this.groupId = groupId;
  }
  
  public String getTaskId() {
    return taskId;
  }

  void setTaskId(String taskId) {
    this.taskId = taskId;
  }
    
  public String getProcessDefId() {
    return processDefId;
  }
  
  public void setProcessDefId(String processDefId) {
    this.processDefId = processDefId;
  }

  public TaskEntity getTask() {
    if ( (task==null) && (taskId!=null) ) {
      this.task = Context
        .getCommandContext()
        .getTaskManager()
        .findTaskById(taskId);
    }
    return task;
  }
  
  public void setTask(TaskEntity task) {
    this.task = task;
    this.taskId = task.getId();
  }

  public ProcessDefinitionEntity getProcessDef() {
    if ((processDef == null) && (processDefId != null)) {
      this.processDef = Context
              .getCommandContext()
              .getProcessDefinitionManager()
              .findLatestProcessDefinitionById(processDefId);
    }
    return processDef;
  }
  
  public void setProcessDef(ProcessDefinitionEntity processDef) {
    this.processDef = processDef;
    this.processDefId = processDef.getId();
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName()
           + "[id=" + id
           + ", type=" + type
           + ", userId=" + userId
           + ", groupId=" + groupId
           + ", taskId=" + taskId
           + ", processDefId=" + processDefId
           + ", task=" + task
           + ", processDef=" + processDef
           + "]";
  }
}