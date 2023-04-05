package org.bridgelabz.service;

import org.bridgelabz.database.SQLOperations;
import org.bridgelabz.fileinput.CSVOperations;
import org.bridgelabz.fileinput.EXCELOperations;
import org.bridgelabz.fileinput.FileIO;
import org.bridgelabz.fileinput.JSONOperations;
import org.bridgelabz.model.Contact;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import static  org.bridgelabz.util.Util.*;

public class AddressBook {

    public void loadDataFromFile() throws SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("File Reader menu -> \nEnter choice : (1)Read CSV " +
                    "(2)Read JSON (3)Read Excel (0)Go back to main menu : ");
            int choice = sc.nextInt();
            FileIO file=null;
            switch (choice) {
                case CSV -> file=new CSVOperations();
                case JSON -> file=new JSONOperations();
                case EXCEL -> file=new EXCELOperations();
                case EXIT -> {
                    return;
                }
                default -> System.out.println("Wrong input!!!");
            }
            if (file != null)
                SQLOperations.getInstance().insertDataInTables(file.getData());
        }
    }

    public void addContacts() throws SQLException {
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
        SQLOperations.getInstance().insertDataInTables(listOfContacts);
    }

    public void searchMenu() throws SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Search menu -> \nEnter choice : (1)Search by name (2)Search by city (3)Search by state (0)Go back to main menu : ");
            int choice = sc.nextInt();
            switch (choice) {
                case NAME -> searchByName();
                case CITY -> searchByCity();
                case STATE -> searchByState();
                case EXIT -> {
                    return;
                }
            }
        }
    }

    public void searchByName() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter first name : ");
        String firstName = sc.next();
        System.out.print("Enter last name : ");
        String lastName = sc.next();
        sc.nextLine();
        System.out.println(SQLOperations.getInstance().searchByName(firstName, lastName));
    }

    public void searchByCity() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter city : ");
        String city = sc.next();
        sc.nextLine();
        System.out.println(SQLOperations.getInstance().searchByCity(city));
    }

    public void searchByState() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter state : ");
        String state = sc.next();
        sc.nextLine();
        System.out.println(SQLOperations.getInstance().searchByState(state));
    }

    public void edit() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter book name : ");
        String bookName = sc.nextLine();
        System.out.print("Enter first name : ");
        String firstName = sc.nextLine();
        System.out.print("Enter last name : ");
        String lastName = sc.nextLine();
        int id=SQLOperations.getInstance().getContactId(bookName, firstName, lastName);
        System.out.print("What do you want to edit? \n" +
                "(1)First name (2)Last name (3)Address (4)City (5)State (6)Pin (7)Phone number (8)Email : ");
        int choice = sc.nextInt();
        String whatToEdit=null, newValue=null;
        sc.nextLine();
        switch (choice) {
            case FIRST_NAME -> {
                System.out.print("Enter new firstName : ");
                newValue = sc.nextLine();
                whatToEdit="firstName";
            }
            case LAST_NAME -> {
                System.out.print("Enter new lastName : ");
                newValue = sc.nextLine();
                whatToEdit="lastName";
            }
            case ADDRESS -> {
                System.out.print("Enter new address : ");
                newValue = sc.nextLine();
                whatToEdit="address";
            }
            case CITY_NAME -> {
                System.out.print("Enter new city name : ");
                newValue = sc.nextLine();
                whatToEdit="city";
            }
            case STATE_NAME -> {
                System.out.print("Enter new state name : ");
                newValue = sc.nextLine();
                whatToEdit="state";
            }
            case PIN_NUM -> {
                System.out.print("Enter new pin : ");
                newValue = sc.nextLine();
                whatToEdit="pin";
            }
            case PHONE_NUM -> {
                System.out.print("Enter new phone number : ");
                newValue = sc.nextLine();
                whatToEdit="phoneNumber";
            }
            case EMAIL -> {
                System.out.print("Enter new email : ");
                newValue = sc.nextLine();
                whatToEdit="email";
            }
            default -> System.out.println("Wrong input!!!");
        }
        SQLOperations.getInstance().edit(whatToEdit, newValue, id);
    }

    public void deleteContact() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter book name from which you want to delete : ");
        String bookName = sc.nextLine();
        System.out.print("Enter first name : ");
        String firstName = sc.nextLine();
        System.out.print("Enter last name : ");
        String lastName = sc.nextLine();
        SQLOperations.getInstance().delete(bookName, firstName, lastName);
    }

    public void sort() throws SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Sort menu -> \nEnter choice : (1)Sort by name (2)Sort by city (3)Sort by state (4)Sort by pin (0)Go back to main menu : ");
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
            if (parameter != null) {
                SQLOperations.getInstance().sort(parameter).forEach(c ->
                    System.out.println("firstName = " + c.getFirstName() + " | " + "lastName = " + c.getLastName() + " | " + "address = " + c.getAddress() + " | " +
                            "city = " + c.getCity() + " | " + "state = " + c.getState() + " | " + "pin = " + c.getPin() + " | " +
                            "phoneNumber = " + c.getPhoneNumber() + " | " + "email = " + c.getEmail())
                );
            }
        }
    }

    public void count() throws SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Count menu -> \nEnter choice : (1)Count by city (2)Count by state (0)Go back to main menu : ");
            int choice = sc.nextInt();
            String parameter = null;
            switch (choice) {
                case COUNT_BY_CITY -> parameter = "city";
                case COUNT_BY_STATE -> parameter = "state";
                case EXIT-> {
                    return;
                }
                default -> System.out.println("Wrong input!!!");
            }
            System.out.println(SQLOperations.getInstance().count(parameter));
        }
    }

}
