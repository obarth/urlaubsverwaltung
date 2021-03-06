package org.synyx.urlaubsverwaltung.web.application;

/**
 * Represents the person that should decide about an application for leave.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class ReferredPerson {

    private String loginName;

    public String getLoginName() {

        return loginName;
    }


    public void setLoginName(String loginName) {

        this.loginName = loginName;
    }
}
