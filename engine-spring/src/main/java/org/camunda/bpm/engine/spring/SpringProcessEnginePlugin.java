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
package org.camunda.bpm.engine.spring;

import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.springframework.beans.factory.BeanNameAware;

/**
 * This is bean-name-aware extension of the {@link AbstractProcessEnginePlugin} allowing anonymous
 * classes get logged correctly when processed.
 */
public class SpringProcessEnginePlugin extends AbstractProcessEnginePlugin implements BeanNameAware {

  protected String beanName;

  @Override
  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  @Override
  public String toString() {
    return beanName;
  }
}
