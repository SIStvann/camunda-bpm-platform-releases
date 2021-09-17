package org.camunda.bpm.integrationtest.functional.ejb.remote.bean;

import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * A SLSB with a remote business interface 
 * 
 * @author Daniel Meyer
 *
 */
@Stateless
@Remote(BusinessInterface.class)
public class RemoteSLSBean implements BusinessInterface {

  public boolean doBusiness() {
    return true;
  }

}
