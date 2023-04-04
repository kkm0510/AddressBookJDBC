package org.bridgelabz.controller;

import org.bridgelabz.database.SQLOperations;
import org.bridgelabz.exception.AddressBookException;
import org.bridgelabz.service.AddressBook;
import java.sql.SQLException;
import java.util.Scanner;
import static  org.bridgelabz.util.Util.*;

public class AddressBookController {

    public static void main(String[] args) throws SQLException {
        AddressBook ab = new AddressBook();
        try {
            SQLOperations.getInstance().initializeSQLDatabase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            SQLOperations.getInstance().closeConnection();
        }
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Main menu -> \nEnter choice : (1)Load data from CSV (2)Load data from JSON " +
                    "(3)Search (4)Edit (5)Add contacts (6)Delete (7)Sort (8)Count (0)Exit : ");
            try {
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case CSV -> ab.loadDataFromCSV();
                    case JSON -> ab.loadDataFromJson();
                    case SEARCH -> ab.searchMenu();
                    case EDIT -> ab.edit();
                    case ADD -> ab.addContacts();
                    case DELETE -> ab.deleteContact();
                    case SORT -> ab.sort();
                    case COUNT -> ab.count();
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


