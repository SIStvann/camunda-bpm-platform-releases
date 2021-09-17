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
package org.camunda.bpm.container.impl.spi;

import java.util.logging.Logger;

/**
 * <p>An atomic step that is part of a composite {@link DeploymentOperation}.</p>
 * 
 * @author Daniel Meyer
 *
 */
public abstract class DeploymentOperationStep {

  protected final Logger LOGGER = Logger.getLogger(getClass().getName());

  public abstract String getName();
  
  public abstract void performOperationStep(DeploymentOperation operationContext);
  
  public void cancelOperationStep(DeploymentOperation operationContext){
    // default behavior is to to nothing if the step fails
  }

  
}
