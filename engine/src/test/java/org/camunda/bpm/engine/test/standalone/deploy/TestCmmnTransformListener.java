/* Licensed under the Apache License, Version 2.0 (the "License");
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

package org.camunda.bpm.engine.test.standalone.deploy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.camunda.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.camunda.bpm.engine.impl.cmmn.model.CmmnSentryDeclaration;
import org.camunda.bpm.engine.impl.cmmn.transformer.CmmnTransformListener;
import org.camunda.bpm.model.cmmn.impl.instance.CasePlanModel;
import org.camunda.bpm.model.cmmn.instance.Case;
import org.camunda.bpm.model.cmmn.instance.CaseTask;
import org.camunda.bpm.model.cmmn.instance.CmmnModelElementInstance;
import org.camunda.bpm.model.cmmn.instance.Definitions;
import org.camunda.bpm.model.cmmn.instance.EventListener;
import org.camunda.bpm.model.cmmn.instance.HumanTask;
import org.camunda.bpm.model.cmmn.instance.Milestone;
import org.camunda.bpm.model.cmmn.instance.PlanItem;
import org.camunda.bpm.model.cmmn.instance.ProcessTask;
import org.camunda.bpm.model.cmmn.instance.Sentry;
import org.camunda.bpm.model.cmmn.instance.Stage;
import org.camunda.bpm.model.cmmn.instance.Task;

/**
 * @author Sebastian Menski
 */
public class TestCmmnTransformListener implements CmmnTransformListener {

  public static Set<CmmnModelElementInstance> modelElementInstances = new HashSet<CmmnModelElementInstance>();
  public static Set<CmmnActivity> cmmnActivities = new HashSet<CmmnActivity>();
  public static Set<CmmnSentryDeclaration> sentryDeclarations = new HashSet<CmmnSentryDeclaration>();

  public void transformRootElement(Definitions definitions, List<? extends CmmnCaseDefinition> caseDefinitions) {
    modelElementInstances.add(definitions);
    for (CmmnCaseDefinition caseDefinition : caseDefinitions) {
      CaseDefinitionEntity entity = (CaseDefinitionEntity) caseDefinition;
      entity.setKey(entity.getKey().concat("-modified"));
    }
  }

  public void transformCase(Case element, CmmnCaseDefinition caseDefinition) {
    modelElementInstances.add(element);
    cmmnActivities.add(caseDefinition);
  }

  public void transformCasePlanModel(CasePlanModel casePlanModel, CmmnActivity activity) {
    modelElementInstances.add(casePlanModel);
    cmmnActivities.add(activity);
  }

  public void transformHumanTask(PlanItem planItem, HumanTask humanTask, CmmnActivity activity) {
    modelElementInstances.add(planItem);
    modelElementInstances.add(humanTask);
    cmmnActivities.add(activity);
  }

  public void transformProcessTask(PlanItem planItem, ProcessTask processTask, CmmnActivity activity) {
    modelElementInstances.add(planItem);
    modelElementInstances.add(processTask);
    cmmnActivities.add(activity);
  }

  public void transformCaseTask(PlanItem planItem, CaseTask caseTask, CmmnActivity activity) {
    modelElementInstances.add(planItem);
    modelElementInstances.add(caseTask);
    cmmnActivities.add(activity);
  }

  public void transformTask(PlanItem planItem, Task task, CmmnActivity activity) {
    modelElementInstances.add(planItem);
    modelElementInstances.add(task);
    cmmnActivities.add(activity);
  }

  public void transformStage(PlanItem planItem, Stage stage, CmmnActivity activity) {
    modelElementInstances.add(planItem);
    modelElementInstances.add(stage);
    cmmnActivities.add(activity);
  }

  public void transformMilestone(PlanItem planItem, Milestone milestone, CmmnActivity activity) {
    modelElementInstances.add(planItem);
    modelElementInstances.add(milestone);
    cmmnActivities.add(activity);
  }

  public void transformEventListener(PlanItem planItem, EventListener eventListener, CmmnActivity activity) {
    modelElementInstances.add(planItem);
    modelElementInstances.add(eventListener);
    cmmnActivities.add(activity);
  }

  public void transformSentry(Sentry sentry, CmmnSentryDeclaration sentryDeclaration) {
    modelElementInstances.add(sentry);
    sentryDeclarations.add(sentryDeclaration);
  }

  protected String getNewName(String name) {
    if (name.endsWith("-modified")) {
      return name + "-again";
    }
    else {
      return name + "-modified";
    }
  }

  public static void reset() {
    modelElementInstances = new HashSet<CmmnModelElementInstance>();
    cmmnActivities = new HashSet<CmmnActivity>();
    sentryDeclarations = new HashSet<CmmnSentryDeclaration>();
  }

  public static int numberOfRegistered(Class<? extends CmmnModelElementInstance> modelElementInstanceClass) {
    int count = 0;
    for (CmmnModelElementInstance element : modelElementInstances) {
      if (modelElementInstanceClass.isInstance(element)) {
        count++;
      }
    }
    return count;
  }

}
