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
package org.camunda.bpm.engine.rest.hal;

import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;

/**
 * A link resolver is capable of resolving a set of resources by Id.
 * Link resolvers are used for embedding relations.
 *
 * @author Daniel Meyer
 *
 */
public interface HalLinkResolver {

  public List<HalResource<?>> resolveLinks(String[] linkedIds, ProcessEngine processEngine);

}
