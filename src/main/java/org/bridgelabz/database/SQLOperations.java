package org.bridgelabz.database;

import org.bridgelabz.model.Contact;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SQLOperations {

    private static SQLOperations sql;
    public Connection con;
    private Statement s;
    private PreparedStatement ps;

    private SQLOperations() throws SQLException {
        connectToServer();
    }

    public static SQLOperations getInstance() throws SQLException {
        if (sql == null) {
            sql = new SQLOperations();
        }
        return sql;
    }

    public void initializeSQLDatabase() throws SQLException {
        con.setAutoCommit(false);
        try {
            connectToServer();
            createDatabase();
            connectToDatabase();
            createContactsTable();
            createBooksTable();
            createContactBookRegister();
            con.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            con.rollback();
        }
    }

    public void closeConnection() throws SQLException {
        con.close();
    }

    public void connectToServer() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "kkm0510");
    }

    public void createDatabase() throws SQLException {
        String query = "CREATE DATABASE addressbook";
        s = con.createStatement();
        s.executeUpdate(query);
    }

    public void connectToDatabase() throws SQLException {
        String query = "USE addressbook";
        s = con.createStatement();
        s.executeUpdate(query);
        System.out.println("connected to database");
    }

    public void createTable(String name, String description) throws SQLException {
        String query = "CREATE TABLE " + name + " (" + description + ")";
        s = con.createStatement();
        s.executeUpdate(query);
        System.out.println("table created");
    }

    public void createContactsTable() throws SQLException {
        String description = "id int AUTO_INCREMENT PRIMARY KEY, firstName varchar(30) not null, lastName varchar(30) not null, address varchar(150) not null, city varchar(30) not null, state varchar(30) not null, pin varchar(6) not null, phoneNumber varchar(10) not null, email varchar(150) not null";
        createTable("contacts", description);
    }

    public void createBooksTable() throws SQLException {
        String description = "bookId int auto_increment primary key, bookName varchar(30) not null";
        createTable("Books", description);
    }

    private void createContactBookRegister() throws SQLException {
        String description = "contactId int not null, bookId int not null, primary key (contactId, bookId), foreign key (bookId) references books (bookId), foreign key (contactId) references contacts (id)";
        createTable("contactBookRegister", description);
    }

    public void insertDataInAllTables(List<Contact> list) throws SQLException {
        insertDataInContactsTable(list);
        insertDataInBooksTable(list);
        insertDataInContactBookTable(list);
        System.out.println("Data added in all tables");
    }

    public void insertDataInContactsTable(List<Contact> list) throws SQLException {
        String parameters = "firstName, lastName, address, city, state, pin, phoneNumber, email";
        for (Contact c : list) {
            String queryForCheck = "SELECT COUNT(*) FROM contacts WHERE phoneNumber = ?";
            ps = con.prepareStatement(queryForCheck);
            ps.setString(1, c.getPhoneNumber());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) continue;
            String queryForInsert = "INSERT INTO contacts (" + parameters + ") VALUES (?,?,?,?,?,?,?,?)";
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

    public void endProgram() throws SQLException {
        String query = "DROP DATABASE addressbook";
        s = con.createStatement();
        s.executeUpdate(query);
        closeConnection();
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

    public void edit(String whatToEdit, String newValue, String firstName, String lastName) throws SQLException {
        String query = "UPDATE contacts SET ? = ? WHERE firstname = ? AND lastName = ?";
        ps = con.prepareStatement(query);
        ps.setString(1, whatToEdit);
        ps.setString(2, newValue);
        ps.setString(3, firstName);
        ps.setString(4, lastName);
        ps.executeUpdate();
    }

    public void delete(String bookName, String firstName, String lastName) throws SQLException {
        String query = "DELETE FROM contactBookRegister WHERE contactId IN (SELECT id FROM contacts WHERE firstname=? AND lastName=?) AND bookId (SELECT bookId FROM books WHERE bookName=?)";
        ps = con.prepareStatement(query);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setString(3, bookName);
        ps.executeUpdate();

        query = "DELETE from BOOKS where bookId NOT IN (SELECT bookId FROM contactBookRegister)";
        s = con.createStatement();
        s.executeUpdate(query);

        query = "DELETE FROM contacts WHERE firstName=? AND lastName=?";
        ps = con.prepareStatement(query);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.executeUpdate();
    }



}
