package org.bridgelabz.service;

import org.bridgelabz.database.SQLOperations;
import org.bridgelabz.fileinput.CSVOperations;
import org.bridgelabz.fileinput.JSONOperations;
import org.bridgelabz.model.Contact;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import static  org.bridgelabz.util.Util.*;

public class AddressBook {

    public void loadDataFromCSV() throws SQLException {
        List<Contact> listOfContacts = new CSVOperations().getData();
        SQLOperations.getInstance().insertDataInAllTables(listOfContacts);
    }

    public void loadDataFromJson() throws SQLException {
        List<Contact> listOfContacts = new JSONOperations().getData();
        SQLOperations.getInstance().insertDataInAllTables(listOfContacts);
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
        SQLOperations.getInstance().insertDataInAllTables(listOfContacts);
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
        System.out.print("Enter first name : ");
        String firstName = sc.nextLine();
        System.out.print("Enter last name : ");
        String lastName = sc.nextLine();
        System.out.print("What do you want to edit? \n" +
                "(1)First name (2)Last name (3)Address (4)City (5)State (6)Pin (7)Phone number (8)Email : ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case FIRST_NAME -> {
                System.out.print("Enter new firstName : ");
                String newFirstName = sc.nextLine();
                SQLOperations.getInstance().edit("firstName", newFirstName, firstName, lastName);
            }
            case LAST_NAME -> {
                System.out.print("Enter new lastName : ");
                String newLastName = sc.nextLine();
                SQLOperations.getInstance().edit("lastName", newLastName, firstName, lastName);
            }
            case ADDRESS -> {
                System.out.print("Enter new address : ");
                String address = sc.nextLine();
                SQLOperations.getInstance().edit("address", address, firstName, lastName);
            }
            case CITY_NAME -> {
                System.out.print("Enter new city : ");
                String city = sc.nextLine();
                SQLOperations.getInstance().edit("city", city, firstName, lastName);
            }
            case STATE_NAME -> {
                System.out.print("Enter new state : ");
                String state = sc.nextLine();
                SQLOperations.getInstance().edit("state", state, firstName, lastName);
            }
            case PIN_NUM -> {
                System.out.print("Enter new pin : ");
                String pin = sc.nextLine();
                SQLOperations.getInstance().edit("pin", pin, firstName, lastName);
            }
            case PHONE_NUM -> {
                System.out.print("Enter new phone number : ");
                String phoneNumber = sc.nextLine();
                SQLOperations.getInstance().edit("phoneNumber", phoneNumber, firstName, lastName);
            }
            case EMAIL -> {
                System.out.print("Enter new email : ");
                String email = sc.nextLine();
                SQLOperations.getInstance().edit("email", email, firstName, lastName);
            }
            default -> System.out.println("Wrong input!!!");
        }
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
            SQLOperations.getInstance().sort(parameter).forEach(c ->
                System.out.println("firstName = " + c.getFirstName() + " | " + "lastName = " + c.getLastName() + " | " + "address = " + c.getAddress() + " | " +
                        "city = " + c.getCity() + " | " + "state = " + c.getState() + " | " + "pin = " + c.getPin() + " | " +
                        "phoneNumber = " + c.getPhoneNumber() + " | " + "email = " + c.getEmail()));
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
