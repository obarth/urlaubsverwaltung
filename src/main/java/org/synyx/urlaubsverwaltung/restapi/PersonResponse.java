package org.synyx.urlaubsverwaltung.restapi;

import org.synyx.urlaubsverwaltung.core.person.Person;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
class PersonResponse {

    private String ldapName;

    private String pnumber;

    private String email;

    private String firstName;

    private String lastName;

    private String niceName;

    PersonResponse(Person person) {

        this.ldapName = person.getLoginName();
        this.pnumber = person.getPnumber();
        this.email = person.getEmail();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.niceName = person.getNiceName();
    }

    public String getLdapName() {

        return ldapName;
    }


    public void setLdapName(String ldapName) {

        this.ldapName = ldapName;
    }


    public String getPnumber() {

        return pnumber;
    }


    public String getEmail() {

        return email;
    }


    public void setEmail(String email) {

        this.email = email;
    }


    public String getFirstName() {

        return firstName;
    }


    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }


    public String getLastName() {

        return lastName;
    }


    public void setLastName(String lastName) {

        this.lastName = lastName;
    }


    public String getNiceName() {

        return niceName;
    }


    public void setNiceName(String niceName) {

        this.niceName = niceName;
    }
}
