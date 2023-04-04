package org.bridgelabz.controller;

import org.bridgelabz.database.SQLOperations;
import org.bridgelabz.exception.AddressBookException;
import org.bridgelabz.service.AddressBook;
import java.sql.SQLException;
import java.util.Scanner;

public class AddressBookController {

    public static void main(String[] args) throws SQLException {
        AddressBook ab = new AddressBook();
        try {
            SQLOperations.getInstance().initializeSQLDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            SQLOperations.getInstance().endProgram();
        }
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Main menu -> \nEnter choice : (1)Load data from CSV (2)Load data from JSON " +
                    "(3)Search (4)Edit (5)Add contacts (6)Delete (0)Exit : ");
            try {
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1 -> ab.loadDataFromCSV();
                    case 2 -> ab.loadDataFromJson();
                    case 3 -> ab.searchMenu();
                    case 4 -> ab.edit();
                    case 5 -> ab.addContacts();
                    case 0 -> {
                        SQLOperations.getInstance().endProgram();
                        return;
                    }
                    default -> throw new AddressBookException("Wrong input");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


