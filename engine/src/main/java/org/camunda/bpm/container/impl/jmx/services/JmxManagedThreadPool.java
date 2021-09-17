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
package org.camunda.bpm.container.impl.jmx.services;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.camunda.bpm.container.impl.jmx.kernel.MBeanService;
import org.camunda.bpm.container.impl.jmx.kernel.MBeanServiceContainer;
import org.camunda.bpm.container.impl.threading.se.SeExecutorService;

/**
 * @author Daniel Meyer
 *
 */
public class JmxManagedThreadPool extends SeExecutorService implements JmxManagedThreadPoolMBean, MBeanService<JmxManagedThreadPool> {
  
  private Logger LOGGER = Logger.getLogger(JmxManagedThreadPool.class.getName());

  protected final BlockingQueue<Runnable> threadPoolQueue;
  
  public JmxManagedThreadPool(BlockingQueue<Runnable> queue, ThreadPoolExecutor executor) {
    super(executor);
    threadPoolQueue = queue;
  }
  
  public void start(MBeanServiceContainer mBeanServiceContainer) {
    // nothing to do
  }

  public void stop(MBeanServiceContainer mBeanServiceContainer) {
    
    // clear the queue
    threadPoolQueue.clear();
    
    // Ask the thread pool to finish and exit
    threadPoolExecutor.shutdown();

    // Waits for 1 minute to finish all currently executing jobs
    try {
      if(!threadPoolExecutor.awaitTermination(60L, TimeUnit.SECONDS)) {
        LOGGER.log(Level.WARNING, "Timeout during shutdown of managed thread pool. "
                + "The current running tasks could not end within 60 seconds after shutdown operation.");        
      }              
    } catch (InterruptedException e) {
      LOGGER.log(Level.WARNING, "Interrupted while shutting down the thread pool. ", e);
    }
    
  }
  
  public JmxManagedThreadPool getValue() {
    return this;
  }

  public void setCorePoolSize(int corePoolSize) {
    threadPoolExecutor.setCorePoolSize(corePoolSize);
  }

  public void setMaximumPoolSize(int maximumPoolSize) {
    threadPoolExecutor.setMaximumPoolSize(maximumPoolSize);
  }

  public int getMaximumPoolSize() {
    return threadPoolExecutor.getMaximumPoolSize();
  }

  public void setKeepAliveTime(long time, TimeUnit unit) {
    threadPoolExecutor.setKeepAliveTime(time, unit);
  }

  public void purgeThreadPool() {
    threadPoolExecutor.purge();
  }

  public int getPoolSize() {
    return threadPoolExecutor.getPoolSize();
  }

  public int getActiveCount() {
    return threadPoolExecutor.getActiveCount();
  }

  public int getLargestPoolSize() {
    return threadPoolExecutor.getLargestPoolSize();
  }

  public long getTaskCount() {
    return threadPoolExecutor.getTaskCount();
  }

  public long getCompletedTaskCount() {
    return threadPoolExecutor.getCompletedTaskCount();
  }
  
  public int getQueueCount() {
    return threadPoolQueue.size();
  }

  public ThreadPoolExecutor getThreadPoolExecutor() {
    return threadPoolExecutor;
  }
}
