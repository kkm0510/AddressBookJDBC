package org.bridgelabz.controller;

import org.bridgelabz.database.DatabaseOperations;
import org.bridgelabz.database.SQLOperations;
import org.bridgelabz.exception.AddressBookException;
import org.bridgelabz.service.AddressBook;
import java.util.Scanner;
import static  org.bridgelabz.util.Util.*;

public class AddressBookController {

    public static void main(String[] args) {
        DatabaseOperations db=SQLOperations.getInstance();
        db.initializeDatabase();
        AddressBook ab = new AddressBook();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Main menu -> \n(1)Load data from File (2)Create database " +
                    "(3)Delete database (4)Search (5)Edit (6)Add contacts (7)Delete (8)Sort \n(9)Count " +
                    "(10)Print all tables (11)Print address book (12)Use database " +
                    "(13)Print contacts in particular date range (0)Exit : ");
            try {
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case FILE_IO -> ab.loadDataFromFile(db);
                    case INITIALIZE_DATABASE -> db.initializeDatabase();
                    case DROP_DATABASE -> db.deleteDatabase();
                    case SEARCH -> ab.searchMenu(db);
                    case EDIT -> ab.edit(db);
                    case ADD -> ab.addContacts(db);
                    case DELETE -> ab.deleteContact(db);
                    case SORT -> ab.sort(db);
                    case COUNT -> ab.count(db);
                    case PRINT_TABLES -> db.printAllTables();
                    case PRINT_ADDRESS_BOOK -> db.printAddressBookContacts();
                    case USE_DATABASE -> db.connectToDatabase();
                    case PRINT_DATE_WISE -> db.printContactsBetweenGivenDates();
                    case EXIT -> {
                        db.closeConnection();
                        return;
                    }
                    default -> throw new AddressBookException("Wrong input");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}


