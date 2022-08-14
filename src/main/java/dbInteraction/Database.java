package dbInteraction;

import com.google.gson.Gson;
import customer.*;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author Gerard
 *
 * This class is used to interact with the database.
 * It is used to load the data from the database and to update the database.
 * You can update the database by calling the updateDatabase methods. These methods are:
 * - updateAccountsDatabase(accountList)
 * - updateContactsDatabase(contactList)
 * - updateLeadsDatabase(leadList)
 * - updateOpportunitiesDatabase(opportunityList)
 *
 * You can load the data from the database by using the Database class.
 * Ex:
 *  Database database = new Database();
 *  List<Account> accountList = database.getAccountList();
 *
 *  This program should load all data from the database right after it is started and
 *  should update the database everytime we add information to a classList.
 */

public class Database {
    private List<Account> accountList;
    private List<Contact> contactList;
    private List<Lead> leadList;
    private List<Opportunity> opportunityList;

    public Database() {
        this.accountList = Account.loadAccountsFromDatabase();
        this.contactList = Contact.loadContactsFromDatabase();
        this.leadList = Lead.loadLeadsFromDatabase();
        this.opportunityList = Opportunity.loadOpportunitiesFromDatabase();
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public List<Lead> getLeadList() {
        return leadList;
    }

    public void setLeadList(List<Lead> leadList) {
        this.leadList = leadList;
    }

    public List<Opportunity> getOpportunityList() {
        return opportunityList;
    }

    public void setOpportunityList(List<Opportunity> opportunityList) {
        this.opportunityList = opportunityList;
    }

    public Lead getLeadByID (UUID id) {
        for (Lead lead : leadList) {
            if (lead.getId().equals(id)) {
                return lead;
            }
        }

        return null;
    }

    public boolean addOpportunity(Opportunity newOpportunity) {
        opportunityList.add(newOpportunity);
        return true;
    }

    public boolean addContact(Contact newContact) {
        contactList.add(newContact);
        return true;
    }

    public Opportunity convertFromLeadToOpportunity(UUID id, ProductType prodType, int truckNumber) {
        Lead leadFound = getLeadByID(id);

        Contact newContact = new Contact(leadFound.getName(), leadFound.getPhoneNumber(), leadFound.getEmail(), leadFound.getCompanyName());
        Opportunity newOpportunity = new Opportunity(newContact, prodType, truckNumber, Status.OPEN);

        addContact(newContact);
        addOpportunity(newOpportunity);

        leadList.remove(leadFound);

        return newOpportunity;
    }

    public void createAndAddLead(String name, String phone, String email, String company) {
        Lead newLead = new Lead(name, phone, email, company);
        leadList.add(newLead);
    }

    public void removeLeadByID(UUID id) {
        Lead leadToRemove = getLeadByID(id);
        leadList.remove(leadToRemove);
    }

    public void createAndAddAccount(Activity industry, String city, String country) {
        Account newAccount = new Account(industry, city, country, this.getOpportunityList());
        accountList.add(newAccount);
    }

    public void exportClassToJSON(Object object) {
        Gson gson = new Gson();
        String objToJSON = gson.toJson(object);
        System.out.println(objToJSON);
    }
}
