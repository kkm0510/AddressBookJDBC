package org.bridgelabz.service;

import org.bridgelabz.database.SQLOperations;
import org.bridgelabz.fileinput.CSVOperations;
import org.bridgelabz.fileinput.JSONOperations;
import org.bridgelabz.model.Contact;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class AddressBook {


    public void loadDataFromCSV() throws SQLException {
        List<Contact> listOfContacts=new CSVOperations().getData();
        SQLOperations.getInstance().insertDataInAllTables(listOfContacts);

    }

    public void loadDataFromJson() throws SQLException {
        List<Contact> listOfContacts=new JSONOperations().getData();
        SQLOperations.getInstance().insertDataInAllTables(listOfContacts);
    }

    public void addContacts() throws SQLException {
        Scanner sc=new Scanner(System.in);
        System.out.println("How many contacts do you want to add? ");
        int numberOfContacts=sc.nextInt();
        sc.nextLine();
        List<Contact> listOfContacts=new LinkedList<>();
        int count=0;
        while(count<numberOfContacts){
            Contact c=new Contact();
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
        Scanner sc=new Scanner(System.in);
        while (true) {
            System.out.print("Search menu -> \nEnter choice : (1)Search by name (2)Search by city (3)Search by state (0)Go back to main menu : ");
            int choice=sc.nextInt();
            switch(choice){
                case 1 -> searchByName();
                case 2 -> searchByCity();
                case 3 -> searchByState();
                case 0 -> {
                    return;
                }
            }
        }
    }

    public void searchByName() throws SQLException {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter first name : ");
        String firstName=sc.next();
        System.out.print("Enter last name : ");
        String lastName=sc.next();
        sc.nextLine();
        System.out.println(SQLOperations.getInstance().searchByName(firstName, lastName));
    }

    public void searchByCity() throws SQLException {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter city : ");
        String city=sc.next();
        sc.nextLine();
        System.out.println(SQLOperations.getInstance().searchByCity(city));
    }

    public void searchByState() throws SQLException {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter state : ");
        String state=sc.next();
        sc.nextLine();
        System.out.println(SQLOperations.getInstance().searchByState(state));
    }

    public void edit() throws SQLException {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter first name : ");
        String firstName=sc.nextLine();
        System.out.println("Enter last name : ");
        String lastName=sc.nextLine();
        System.out.print("What do you want to edit? \n " +
                "(1)First name (2)Last name (3)Address (4)City (5)State (6)Pin (7)Phone number (8)Email : ");
        int choice=sc.nextInt();
        sc.nextLine();
        switch (choice){
            case 1->{
                System.out.println("Enter new firstName : ");
                String newFirstName=sc.nextLine();
                SQLOperations.getInstance().edit("firstName", newFirstName, firstName, lastName);
            }
            case 2->{
                System.out.println("Enter new lastName : ");
                String newLastName=sc.nextLine();
                SQLOperations.getInstance().edit("lastName", newLastName, firstName, lastName);
            }
            case 3->{
                System.out.println("Enter new address : ");
                String address=sc.nextLine();
                SQLOperations.getInstance().edit("address", address, firstName, lastName);
            }
            case 4->{
                System.out.println("Enter new city : ");
                String city=sc.nextLine();
                SQLOperations.getInstance().edit("city", city, firstName, lastName);
            }
            case 5-> {
                System.out.println("Enter new state : ");
                String state=sc.nextLine();
                SQLOperations.getInstance().edit("state", state, firstName, lastName);
            }
            case 6->{
                System.out.println("Enter new pin : ");
                String pin=sc.nextLine();
                SQLOperations.getInstance().edit("pin", pin, firstName, lastName);
            }
            case 7 -> {
                System.out.println("Enter new phone number : ");
                String phoneNumber=sc.nextLine();
                SQLOperations.getInstance().edit("phoneNumber", phoneNumber, firstName, lastName);
            }
            case 8 -> {
                System.out.println("Enter new email : ");
                String email=sc.nextLine();
                SQLOperations.getInstance().edit("email", email, firstName, lastName);
            }
            default -> System.out.println("Wrong input!!!");
        }
    }


}
