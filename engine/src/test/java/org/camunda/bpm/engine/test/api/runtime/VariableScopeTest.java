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
package org.camunda.bpm.engine.test.api.runtime;

import java.util.Map;

import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.test.api.runtime.util.TestVariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Daniel Meyer
 *
 */
public class VariableScopeTest {

  private static final String VAR_NAME = "foo";

  private static final String VAR_VALUE_STRING = "bar";

  private VariableScope variableScope;

  @Before
  public void setUp() {
    this.variableScope = new TestVariableScope();
    variableScope.setVariable(VAR_NAME, VAR_VALUE_STRING);
  }

  @Test
  public void testGetVariables() {
    Map<String, Object> variables = variableScope.getVariables();
    assertNotNull(variables);
    assertEquals(VAR_VALUE_STRING, variables.get(VAR_NAME));
  }

  @Test
  public void testGetVariablesTyped() {
    VariableMap variables = variableScope.getVariablesTyped();
    assertNotNull(variables);
    assertEquals(VAR_VALUE_STRING, variables.get(VAR_NAME));
    assertEquals(variables, variableScope.getVariablesTyped(true));
  }

  @Test
  public void testGetVariablesLocal() {
    Map<String, Object> variables = variableScope.getVariablesLocal();
    assertNotNull(variables);
    assertEquals(VAR_VALUE_STRING, variables.get(VAR_NAME));
  }

  @Test
  public void testGetVariablesLocalTyped() {
    Map<String, Object> variables = variableScope.getVariablesLocalTyped();
    assertNotNull(variables);
    assertEquals(VAR_VALUE_STRING, variables.get(VAR_NAME));
    assertEquals(variables, variableScope.getVariablesLocalTyped(true));
  }

}
