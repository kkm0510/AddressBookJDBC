package org.bridgelabz.controller;

import org.bridgelabz.database.SQLOperations;
import org.bridgelabz.database.TableEnum;
import org.bridgelabz.exception.AddressBookException;
import org.bridgelabz.service.AddressBook;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
import static  org.bridgelabz.util.Util.*;

public class AddressBookController {

    public static void main(String[] args) throws SQLException {
        AddressBook ab = new AddressBook();
        try {
            SQLOperations.getInstance().initializeSQLDatabase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Arrays.stream(TableEnum.values()).forEach(table-> System.out.println(table.toString()));
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Main menu -> \nEnter choice : (1)Load data from File (2)Create database " +
                    "(3)Delete database (4)Search (5)Edit (6)Add contacts (7)Delete (8)Sort (9)Count (0)Exit : ");
            try {
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case FILE_IO -> ab.loadDataFromFile();
                    case INITIALIZE_DATABASE -> SQLOperations.getInstance().createDatabase();
                    case DROP_DATABASE -> SQLOperations.getInstance().deleteDatabase();
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


