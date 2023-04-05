package org.bridgelabz.controller;

import org.bridgelabz.database.SQLOperations;
import org.bridgelabz.exception.AddressBookException;
import org.bridgelabz.service.AddressBook;
import java.util.Scanner;
import static  org.bridgelabz.util.Util.*;

public class AddressBookController {

    public static void main(String[] args) {
        AddressBook ab = new AddressBook();
        try {
            SQLOperations.getInstance().initializeDatabase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Main menu -> \n(1)Load data from File (2)Create database " +
                    "(3)Delete database (4)Search (5)Edit (6)Add contacts \n(7)Delete (8)Sort (9)Count " +
                    "(10)Print all tables (11)Print address book (12)Use database " +
                    "(13)Print contacts in particular date range (0)Exit : ");
            try {
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case FILE_IO -> ab.loadDataFromFile();
                    case INITIALIZE_DATABASE -> SQLOperations.getInstance().initializeDatabase();
                    case DROP_DATABASE -> SQLOperations.getInstance().deleteDatabase();
                    case SEARCH -> ab.searchMenu();
                    case EDIT -> ab.edit();
                    case ADD -> ab.addContacts();
                    case DELETE -> ab.deleteContact();
                    case SORT -> ab.sort();
                    case COUNT -> ab.count();
                    case PRINT_TABLES -> SQLOperations.getInstance().printAllTables();
                    case PRINT_ADDRESS_BOOK -> SQLOperations.getInstance().printAddressBookContacts();
                    case USE_DATABASE -> SQLOperations.getInstance().connectToDatabase();
                    case PRINT_DATE_WISE -> SQLOperations.getInstance().printContactsBetweenGivenDates();
                    case EXIT -> {
                        SQLOperations.getInstance().closeConnection();
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


