package org.bridgelabz.service;

import org.bridgelabz.database.DatabaseOperations;
import org.bridgelabz.database.SQLOperations;
import org.bridgelabz.exception.AddressBookException;
import org.bridgelabz.fileinput.CSVOperations;
import org.bridgelabz.fileinput.EXCELOperations;
import org.bridgelabz.fileinput.FileIO;
import org.bridgelabz.fileinput.JSONOperations;
import org.bridgelabz.model.Contact;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static org.bridgelabz.util.Util.*;

public class AddressBook {

    private final DatabaseOperations DB;

    public AddressBook(DatabaseOperations db){
        DB=db;
    }

    public void loadDataFromFile() throws AddressBookException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("File Reader menu -> \nEnter choice : (1)Read CSV " +
                    "(2)Read JSON (3)Read Excel (0)Go back to main menu : ");
            int choice = sc.nextInt();
            FileIO file;
            switch (choice) {
                case CSV -> file = new CSVOperations();
                case JSON -> file = new JSONOperations();
                case EXCEL -> file = new EXCELOperations();
                case EXIT -> {
                    return;
                }
                default -> throw new AddressBookException("Wrong input!!!");
            }
            DB.insertDataInTables(file.getData());
        }
    }

    public void addContacts() {
        Scanner sc = new Scanner(System.in);
        System.out.println("How many contacts do you want to add? ");
        int numberOfContacts = sc.nextInt();
        sc.nextLine();
        List<Contact> listOfContacts = new LinkedList<>();
        int count = 0;
        while (count < numberOfContacts) {
            Contact c = new Contact();
            System.out.println("Enter book name : ");
            c.setBookName(sc.nextLine());
            System.out.println("Enter first name : ");
            c.setFirstName(sc.nextLine());
            System.out.println("Enter last name : ");
            c.setLastName(sc.nextLine());
            System.out.println("Enter address : ");
            c.setAddress(sc.nextLine());
            System.out.println("Enter city : ");
            c.setCity(sc.nextLine());
            System.out.println("Enter state : ");
            c.setState(sc.nextLine());
            System.out.println("Enter pin : ");
            c.setPin(sc.nextLine());
            System.out.println("Enter phone number : ");
            c.setPhoneNumber(sc.nextLine());
            System.out.println("Enter email : ");
            c.setEmail(sc.nextLine());
            listOfContacts.add(c);
            count++;
        }
        DB.insertDataInTables(listOfContacts);
    }

    public void searchMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Search menu -> \nEnter choice : (1)Search by name (2)Search by city (3)Search by state (0)Go back to main menu : ");
                int choice = sc.nextInt();
                switch (choice) {
                    case NAME -> searchByName();
                    case CITY -> searchByCity();
                    case STATE -> searchByState();
                    case EXIT -> {
                        return;
                    }
                    default -> throw new AddressBookException("Wrong input");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void searchByName() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter first name : ");
        String firstName = sc.next();
        System.out.print("Enter last name : ");
        String lastName = sc.next();
        sc.nextLine();
        System.out.println(DB.searchByName(firstName, lastName));
    }

    public void searchByCity() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter city : ");
        String city = sc.next();
        sc.nextLine();
        System.out.println(DB.searchByCity(city));
    }

    public void searchByState() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter state : ");
        String state = sc.next();
        sc.nextLine();
        System.out.println(DB.searchByState(state));
    }

    public void edit() {
        Scanner sc = new Scanner(System.in);
        DB.printBooksTable();
        System.out.print("Enter book id from which you want to delete : ");
        try {
            int bookId = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter first name : ");
            String firstName = sc.nextLine();
            System.out.print("Enter last name : ");
            String lastName = sc.nextLine();
            int id = DB.getContactId(bookId, firstName, lastName);
            System.out.print("What do you want to edit? \n" +
                    "(1)First name (2)Last name (3)Address (4)City (5)State (6)Pin (7)Phone number (8)Email : ");
            int choice = sc.nextInt();
            String whatToEdit = null, newValue = null;
            sc.nextLine();
            switch (choice) {
                case FIRST_NAME -> {
                    System.out.print("Enter new firstName : ");
                    newValue = sc.nextLine();
                    whatToEdit = "firstName";
                }
                case LAST_NAME -> {
                    System.out.print("Enter new lastName : ");
                    newValue = sc.nextLine();
                    whatToEdit = "lastName";
                }
                case ADDRESS -> {
                    System.out.print("Enter new address : ");
                    newValue = sc.nextLine();
                    whatToEdit = "address";
                }
                case CITY_NAME -> {
                    System.out.print("Enter new city name : ");
                    newValue = sc.nextLine();
                    whatToEdit = "city";
                }
                case STATE_NAME -> {
                    System.out.print("Enter new state name : ");
                    newValue = sc.nextLine();
                    whatToEdit = "state";
                }
                case PIN_NUM -> {
                    System.out.print("Enter new pin : ");
                    newValue = sc.nextLine();
                    whatToEdit = "pin";
                }
                case PHONE_NUM -> {
                    System.out.print("Enter new phone number : ");
                    newValue = sc.nextLine();
                    whatToEdit = "phoneNumber";
                }
                case EMAIL -> {
                    System.out.print("Enter new email : ");
                    newValue = sc.nextLine();
                    whatToEdit = "email";
                }
                default -> System.out.println("Wrong input!!!");
            }
            DB.edit(whatToEdit, newValue, id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteContact() throws AddressBookException {
        Scanner sc = new Scanner(System.in);
        DB.printBooksTable();
        System.out.print("Enter book id from which you want to delete : ");
        try {
            int bookId = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter first name : ");
            String firstName = sc.nextLine();
            System.out.print("Enter last name : ");
            String lastName = sc.nextLine();
            DB.delete(bookId, firstName, lastName);
        } catch (InputMismatchException e) {
            throw new AddressBookException("Wrong Input");
        }
    }

    public void sort() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Sort menu -> \nEnter choice : (1)Sort by name (2)Sort by city (3)Sort by state " +
                        "(4)Sort by pin (0)Go back to main menu : ");
                int choice = sc.nextInt();
                String parameter = null;
                switch (choice) {
                    case NAME -> parameter = "name";
                    case CITY -> parameter = "city";
                    case STATE -> parameter = "state";
                    case PIN -> parameter = "pin";
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Wrong input!!!");
                }
                if (parameter != null)
                    DB.sort(parameter).forEach(Contact::print);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void count() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Count menu -> \nEnter choice : (1)Count by city (2)Count by state (0)Go back to main menu : ");
            try {
                int choice = sc.nextInt();
                String parameter;
                switch (choice) {
                    case COUNT_BY_CITY -> parameter = "city";
                    case COUNT_BY_STATE -> parameter = "state";
                    case EXIT -> {
                        return;
                    }
                    default -> throw new AddressBookException("Wrong input!!!");
                }
                System.out.println(DB.count(parameter));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
