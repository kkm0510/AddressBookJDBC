package org.bridgelabz.database;

import org.bridgelabz.model.Contact;

import java.util.List;
import java.util.Map;

public interface DatabaseOperations {

    void initializeDatabase();
    void deleteDatabase();
    void connectToServer();
    int createDatabase();
    void connectToDatabase();
    void closeConnection();
    void insertDataInTables(List<Contact> list);
    List<Contact> searchByName(String firstName, String lastName);
    List<Contact> searchByCity(String city);
    List<Contact> searchByState(String state);
    void delete(int bookId, String firstName, String lastName);
    List<Contact> sort(String parameter);
    Map<String, Integer> count(String parameter);
    void edit(String whatToEdit, String newValue, int id);
    int getContactId(int bookId, String firstName, String lastName);
    void printAllTables();
    void printBooksTable();
    void printAddressBookContacts();
    void printContactsBetweenGivenDates();
}
