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
package org.camunda.bpm.engine.impl.batch;

import org.camunda.bpm.engine.impl.persistence.entity.ByteArrayEntity;

/**
 * @author Askar Akhmerov
 */
public class BatchJobContext {
  public BatchJobContext(BatchEntity batchEntity, ByteArrayEntity configuration) {
    this.batch = batchEntity;
    this.configuration = configuration;
  }

  protected BatchEntity batch;
  protected ByteArrayEntity configuration;

  public BatchEntity getBatch() {
    return batch;
  }

  public void setBatch(BatchEntity batch) {
    this.batch = batch;
  }

  public ByteArrayEntity getConfiguration() {
    return configuration;
  }

  public void setConfiguration(ByteArrayEntity configuration) {
    this.configuration = configuration;
  }
}
