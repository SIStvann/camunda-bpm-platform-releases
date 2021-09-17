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
package org.camunda.bpm.engine.impl.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.servlet.ServletContextEvent;

/**
 * @author Daniel Meyer
 *
 */
public class ClassLoaderUtil {

  public static ClassLoader getContextClassloader() {
    if(System.getSecurityManager() != null) {
      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
        public ClassLoader run() {
          return Thread.currentThread().getContextClassLoader();
        }
      });
    } else {
      return Thread.currentThread().getContextClassLoader();
    }
  }

  public static ClassLoader getClassloader(final Class<?> clazz) {
    if(System.getSecurityManager() != null) {
      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
        public ClassLoader run() {
          return clazz.getClassLoader();
        }
      });
    } else {
      return clazz.getClassLoader();
    }
  }

  public static void setContextClassloader(final ClassLoader classLoader) {
    if(System.getSecurityManager() != null) {
      AccessController.doPrivileged(new PrivilegedAction<Void>() {
        public Void run() {
          Thread.currentThread().setContextClassLoader(classLoader);
          return null;
        }
      });
    } else {
      Thread.currentThread().setContextClassLoader(classLoader);
    }
  }

  public static ClassLoader getServletContextClassloader(final ServletContextEvent sce) {
    if(System.getSecurityManager() != null) {
      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
        public ClassLoader run() {
          return sce.getServletContext().getClassLoader();
        }
      });
    } else {
      return sce.getServletContext().getClassLoader();
    }
  }

}
