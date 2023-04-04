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

    public List<Contact> listOfContacts;

    public AddressBook(){
        listOfContacts=new LinkedList<>();
    }

    public void loadDataFromCSV() throws SQLException {
        listOfContacts=new CSVOperations().getData();
        SQLOperations.getInstance().insertDataInAllTables(listOfContacts);

    }

    public void loadDataFromJson() throws SQLException {
        listOfContacts=new JSONOperations().getData();
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

    public void edit() {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter first name : ");
        String firstName=sc.next();
        System.out.println("Enter last name : ");
        String lastName=sc.next();
        sc.nextLine();
    }
}
