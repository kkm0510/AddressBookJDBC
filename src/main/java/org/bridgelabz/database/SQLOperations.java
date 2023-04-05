package org.bridgelabz.database;

import org.bridgelabz.model.Contact;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class SQLOperations {

    private static SQLOperations sql;
    private Connection con;
    private Statement s;
    private PreparedStatement ps;

    private SQLOperations() {
    }

    public static SQLOperations getInstance() {
        if (sql == null) {
            sql = new SQLOperations();
        }
        return sql;
    }

    public void initializeDatabase() throws SQLException {
        if(con==null || con.isClosed()) connectToServer();
        createDatabase();
        connectToDatabase();
        createTable();
    }

    public void deleteDatabase() throws SQLException {
        String query = "DROP DATABASE addressbook";
        s = con.createStatement();
        s.executeUpdate(query);
        System.out.println("Database deleted");
    }

    public void closeConnection() throws SQLException {
        deleteDatabase();
        con.close();
        System.out.println("Disconnected");
    }

    public void connectToServer() throws SQLException {
        String url = System.getenv("dbServerURL");
        String user = System.getenv("dbUser");
        String pass = System.getenv("dbPassword");
        con = DriverManager.getConnection(url, user, pass);
        System.out.println("Connected");
    }

    public void createDatabase() throws SQLException {
        String query = "CREATE DATABASE addressbook";
        s = con.createStatement();
        s.executeUpdate(query);
        System.out.println("Database created");
    }

    public void connectToDatabase() throws SQLException {
        String query = "USE addressbook";
        s = con.createStatement();
        s.executeUpdate(query);
    }

    public void createTable() throws SQLException {
        TableEnum[] arr = TableEnum.values();
        for (TableEnum table : arr) {
            String query = "CREATE TABLE " + table.getNAME() + " (" + table.getDESCRIPTION() + ")";
            s = con.createStatement();
            s.executeUpdate(query);
        }
    }

    public void insertDataInTables(List<Contact> list) throws SQLException {
        insertDataInContactsTable(list);
        insertDataInBooksTable(list);
        insertDataInContactBookTable(list);
        System.out.println("Data added in all tables");
    }

    public void insertDataInContactsTable(List<Contact> list) throws SQLException {
        String parameters = "firstName, lastName, address, city, state, pin, phoneNumber, email, dateAdded";
        for (Contact c : list) {
            String queryForCheck = "SELECT COUNT(*) FROM contacts WHERE phoneNumber = ?";
            ps = con.prepareStatement(queryForCheck);
            ps.setString(1, c.getPhoneNumber());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) continue;
            String queryForInsert = "INSERT INTO contacts (" + parameters + ") VALUES (?,?,?,?,?,?,?,?,CURDATE())";
            ps = con.prepareStatement(queryForInsert);
            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getLastName());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getCity());
            ps.setString(5, c.getState());
            ps.setString(6, c.getPin());
            ps.setString(7, c.getPhoneNumber());
            ps.setString(8, c.getEmail());
            ps.executeUpdate();
        }
    }

    public void insertDataInBooksTable(List<Contact> list) throws SQLException {
        String parameters = "bookName";
        for (Contact c : list) {
            String queryForCheck = "SELECT COUNT(*) FROM books WHERE bookName = ?";
            ps = con.prepareStatement(queryForCheck);
            ps.setString(1, c.getBookName());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) continue;
            String queryForInsert = "INSERT INTO books (" + parameters + ") VALUES (?)";
            ps = con.prepareStatement(queryForInsert);
            ps.setString(1, c.getBookName());
            ps.executeUpdate();
        }
    }

    public void insertDataInContactBookTable(List<Contact> list) throws SQLException {
        String parameters = "contactId, bookId";
        for (Contact c : list) {
            String whereClause = "phoneNumber= '" + c.getPhoneNumber() + "'";
            int contactId = searchContactIdOrBookIdInDatabase("id", "contacts", whereClause);
            whereClause = "bookName= '" + c.getBookName() + "'";
            int bookId = searchContactIdOrBookIdInDatabase("bookId", "books", whereClause);
            String queryForCheck = "SELECT COUNT(*) FROM contactBookRegister WHERE contactId = ? AND bookId = ?";
            ps = con.prepareStatement(queryForCheck);
            ps.setInt(1, contactId);
            ps.setInt(2, bookId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) continue;
            String queryForInsert = "INSERT INTO contactBookRegister (" + parameters + ") VALUES (?,?)";
            ps = con.prepareStatement(queryForInsert);
            ps.setInt(1, contactId);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        }
    }

    public int searchContactIdOrBookIdInDatabase(String selectParameter, String tableName, String whereParameter) throws SQLException {
        String query = "SELECT " + selectParameter + " from " + tableName + " where " + whereParameter;
        s = con.createStatement();
        ResultSet rs = s.executeQuery(query);
        rs.next();
        return rs.getInt(1);
    }

    public List<Contact> toContactsList(ResultSet rs) throws SQLException {
        List<Contact> list = new LinkedList<>();
        while (rs.next()) {
            Contact c = new Contact();
            c.setFirstName(rs.getString(2));
            c.setLastName(rs.getString(3));
            c.setAddress(rs.getString(4));
            c.setCity(rs.getString(5));
            c.setState(rs.getString(6));
            c.setPin(rs.getString(7));
            c.setPhoneNumber(rs.getString(8));
            c.setEmail(rs.getString(9));
            list.add(c);
        }
        return list;
    }

    public List<Contact> searchList(ResultSet rs) throws SQLException {
        List<Contact> list = new LinkedList<>();
        while (rs.next()) {
            Contact c = new Contact();
            c.setBookName(rs.getString(1));
            c.setFirstName(rs.getString(2));
            c.setLastName(rs.getString(3));
            c.setAddress(rs.getString(4));
            c.setCity(rs.getString(5));
            c.setState(rs.getString(6));
            c.setPin(rs.getString(7));
            c.setPhoneNumber(rs.getString(8));
            c.setEmail(rs.getString(9));
            list.add(c);
        }
        return list;
    }

    public List<Contact> searchByName(String firstName, String lastName) throws SQLException {
        String query = "SELECT b.bookName, c.firstName, c.lastName, c.address, c.city, c.state, c.pin, c.phoneNumber, c.email " +
                "FROM contacts c " +
                "JOIN contactBookRegister cb ON c.id = cb.contactId " +
                "JOIN books b ON cb.bookId = b.bookId " +
                "WHERE c.firstName =  ? AND c.lastName = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ResultSet rs = ps.executeQuery();
        return searchList(rs);
    }

    public List<Contact> searchByCity(String city) throws SQLException {
        String query = "SELECT b.bookName, c.firstName, c.lastName, c.address, c.city, c.state, c.pin, c.phoneNumber, c.email " +
                "FROM contacts c " +
                "JOIN contactBookRegister cb ON c.id = cb.contactId " +
                "JOIN books b ON cb.bookId = b.bookId " +
                "WHERE c.city =  ?";
        ps = con.prepareStatement(query);
        ps.setString(1, city);
        ResultSet rs = ps.executeQuery();
        return searchList(rs);
    }

    public List<Contact> searchByState(String state) throws SQLException {
        String query = "SELECT b.bookName, c.firstName, c.lastName, c.address, c.city, c.state, c.pin, c.phoneNumber, c.email " +
                "FROM contacts c " +
                "JOIN contactBookRegister cb ON c.id = cb.contactId " +
                "JOIN books b ON cb.bookId = b.bookId " +
                "WHERE c.state =  ?";
        ps = con.prepareStatement(query);
        ps.setString(1, state);
        ResultSet rs = ps.executeQuery();
        return searchList(rs);
    }

    public void delete(int bookId, String firstName, String lastName) throws SQLException {
        int id=getContactId(bookId, firstName, lastName);
        String query = "DELETE FROM contactBookRegister " +
                "WHERE contactId = ? " +
                "AND bookId = ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ps.setInt(2, bookId);
        ps.executeUpdate();
        query = "DELETE FROM books WHERE bookId NOT IN (SELECT bookId FROM contactBookRegister)";
        s = con.createStatement();
        s.executeUpdate(query);
        query = "DELETE FROM contacts " +
                "WHERE id NOT IN (SELECT contactId FROM contactBookRegister)";
        s = con.createStatement();
        s.executeUpdate(query);
        System.out.println("Deleted Successfully");
    }

    public List<Contact> sort(String parameter) throws SQLException {
        if (parameter.equals("name")) parameter = "CONCAT (firstName, ' ', lastName)";
        String query = "SELECT * FROM CONTACTS ORDER BY " + parameter;
        s = con.createStatement();
        ResultSet rs = s.executeQuery(query);
        return toContactsList(rs);
    }

    public Map<String, Integer> count(String parameter) throws SQLException {
        String query = "SELECT " + parameter + ", COUNT(*) FROM CONTACTS GROUP BY " + parameter;
        s = con.createStatement();
        ResultSet rs = s.executeQuery(query);
        Map<String, Integer> map = new HashMap<>();
        while (rs.next()) map.put(rs.getString(1), rs.getInt(2));
        return map;
    }

    public void edit(String whatToEdit, String newValue, int id) throws SQLException {
        String query = "UPDATE contacts SET " + whatToEdit + " = ? WHERE id = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, newValue);
        ps.setInt(2, id);
        if (ps.executeUpdate() > 0) System.out.println("Edited successfully");
    }

    public int getContactId(int bookId, String firstName, String lastName) throws SQLException {
        String query = "SELECT c.* "  +
                "FROM contacts c " +
                "JOIN contactBookRegister cbr ON c.id = cbr.contactId " +
                "JOIN books b ON cbr.bookId = b.bookId " +
                "WHERE c.firstName = ? " +
                "AND c.lastName = ? " +
                "AND b.bookId = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setInt(3, bookId);
        ResultSet rs = ps.executeQuery();
        printContactsResultSet(rs);
        System.out.print("Select contact id : ");
        return new Scanner(System.in).nextInt();
    }

    public void printContactsResultSet(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.print("Id : " + rs.getInt(1) + "  ");
            for (int i = 2; i <= 9; i++) {
                System.out.print(rs.getString(i) + "  ");
            }
            System.out.println(rs.getDate(10));
            System.out.println();
        }
    }

    public void printAllTables() throws SQLException {
        printContactsTable();
        printBooksTable();
        printContactBookRegisterTable();
    }

    public void printContactsTable() throws SQLException {
        String query = "SELECT * FROM contacts";
        s = con.createStatement();
        ResultSet rs = s.executeQuery(query);
        System.out.println("\nContacts table -> ");
        printContactsResultSet(rs);
    }

    public void printBooksTable() throws SQLException {
        String query = "SELECT * FROM books";
        s = con.createStatement();
        ResultSet rs = s.executeQuery(query);
        System.out.println("\nBooks table -> ");
        while (rs.next()) {
            System.out.print("Book Id : " + rs.getInt(1) + " -> ");
            System.out.println("Book Name : " + rs.getString(2));
        }
    }

    public void printContactBookRegisterTable() throws SQLException {
        String query = "SELECT * FROM contactBookRegister";
        s = con.createStatement();
        ResultSet rs = s.executeQuery(query);
        System.out.println("\nContact Book Register table -> ");
        while (rs.next()) {
            System.out.print("Contact Id : " + rs.getInt(1) + " -> ");
            System.out.println("Book Id : " + rs.getInt(2));
        }
    }

    public void printAddressBookContacts() throws SQLException {
        printBooksTable();
        System.out.print("Select book id : ");
        int bookId=new Scanner(System.in).nextInt();
        String query="SELECT * FROM contacts c " +
                "JOIN contactBookRegister cbr ON cbr.contactId=c.id " +
                "JOIN books b ON b.bookId=cbr.bookId " +
                "WHERE b.bookId=?";
        ps = con.prepareStatement(query);
        ps.setInt(1, bookId);
        ResultSet rs = ps.executeQuery();
        printContactsResultSet(rs);
    }

    public void printContactsBetweenGivenDates() throws SQLException {
        String startDate = "2023-04-5";
        String endDate = "2023-04-10";
        String query = "SELECT * FROM CONTACTS WHERE dateAdded BETWEEN ? AND ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, startDate);
        ps.setString(2, endDate);
        ResultSet rs = ps.executeQuery();
        printContactsResultSet(rs);
    }
}
