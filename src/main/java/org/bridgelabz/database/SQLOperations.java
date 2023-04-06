package org.bridgelabz.database;

import static org.bridgelabz.util.Util.*;

import org.bridgelabz.model.Contact;

import java.sql.*;
import java.util.*;

public class SQLOperations implements  DatabaseOperations{

    private static SQLOperations sql;
    private Connection con;

    private SQLOperations() {
    }

    public static SQLOperations getInstance() {
        if (sql == null) {
            sql = new SQLOperations();
        }
        return sql;
    }

    @Override
    public void initializeDatabase() {
        try {
            if (con == null || con.isClosed()) connectToServer();
            createDatabase();
            connectToDatabase();
            createTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteDatabase() {
        try {
            String query = "DROP DATABASE addressbook";
            Statement s = con.createStatement();
            s.executeUpdate(query);
            System.out.println("Database deleted");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void closeConnection() {
        try {
            deleteDatabase();
            con.close();
            System.out.println("Disconnected");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void connectToServer() {
        String url = System.getenv("dbServerURL");
        String user = System.getenv("dbUser");
        String pass = System.getenv("dbPassword");
        try {
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int createDatabase() {
        try {
            String query = "CREATE DATABASE addressbook";
            Statement s = con.createStatement();
            int rowsAffected = s.executeUpdate(query);
            System.out.println("Database created");
            return rowsAffected;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public void connectToDatabase() {
        try {
            String query = "USE addressbook";
            Statement s = con.createStatement();
            s.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createTable() {
        TableEnum[] arr = TableEnum.values();
        try {
            for (TableEnum table : arr) {
                String query = "CREATE TABLE " + table.getNAME() + " (" + table.getDESCRIPTION() + ")";
                Statement s = con.createStatement();
                s.executeUpdate(query);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void insertDataInTables(List<Contact> list) {
        try {
            con.setAutoCommit(false);
            insertDataInContactsTable(list);
            insertDataInBooksTable(list);
            insertDataInContactBookTable(list);
            con.commit();
            System.out.println("Data added in all tables");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                con.rollback();
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private void insertDataInContactsTable(List<Contact> list) {
        String parameters = "firstName, lastName, address, city, state, pin, phoneNumber, email, dateAdded";
        try {
            for (Contact c : list) {
                String queryForCheck = "SELECT COUNT(*) FROM contacts WHERE phoneNumber = ?";
                PreparedStatement ps = con.prepareStatement(queryForCheck);
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertDataInBooksTable(List<Contact> list) throws SQLException {
        String parameters = "bookName";
        for (Contact c : list) {
            String queryForCheck = "SELECT COUNT(*) FROM books WHERE bookName = ?";
            PreparedStatement ps = con.prepareStatement(queryForCheck);
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

    private void insertDataInContactBookTable(List<Contact> list) {
        String parameters = "contactId, bookId";
        try {
            for (Contact c : list) {
                String whereClause = "phoneNumber= '" + c.getPhoneNumber() + "'";
                int contactId = searchContactIdOrBookIdInDatabase("id", "contacts", whereClause);
                whereClause = "bookName= '" + c.getBookName() + "'";
                int bookId = searchContactIdOrBookIdInDatabase("bookId", "books", whereClause);
                String queryForCheck = "SELECT COUNT(*) FROM contactBookRegister WHERE contactId = ? AND bookId = ?";
                PreparedStatement ps = con.prepareStatement(queryForCheck);
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private int searchContactIdOrBookIdInDatabase(String selectParameter, String tableName, String whereParameter) {
        try {
            String query = "SELECT " + selectParameter + " from " + tableName + " where " + whereParameter;
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(query);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    private List<Contact> searchList(ResultSet rs, int type) {
        List<Contact> list = new LinkedList<>();
        try {
            while (rs.next()) {
                Contact c = new Contact();
                if (type == LIST_WITH_BOOK_NAME) c.setBookName(rs.getString(1));
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Contact> searchByName(String firstName, String lastName) {
        String query = "SELECT b.bookName, c.firstName, c.lastName, c.address, c.city, c.state, c.pin, c.phoneNumber, c.email " +
                "FROM contacts c " +
                "JOIN contactBookRegister cb ON c.id = cb.contactId " +
                "JOIN books b ON cb.bookId = b.bookId " +
                "WHERE c.firstName =  ? AND c.lastName = ?";
        List<Contact> list = new LinkedList<>();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ResultSet rs = ps.executeQuery();
            list = searchList(rs, LIST_WITH_BOOK_NAME);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Contact> searchByCity(String city) {
        String query = "SELECT b.bookName, c.firstName, c.lastName, c.address, c.city, c.state, c.pin, c.phoneNumber, c.email " +
                "FROM contacts c " +
                "JOIN contactBookRegister cb ON c.id = cb.contactId " +
                "JOIN books b ON cb.bookId = b.bookId " +
                "WHERE c.city =  ?";
        List<Contact> list = new LinkedList<>();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, city);
            ResultSet rs = ps.executeQuery();
            list = searchList(rs, LIST_WITH_BOOK_NAME);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Contact> searchByState(String state) {
        String query = "SELECT b.bookName, c.firstName, c.lastName, c.address, c.city, c.state, c.pin, c.phoneNumber, c.email " +
                "FROM contacts c " +
                "JOIN contactBookRegister cb ON c.id = cb.contactId " +
                "JOIN books b ON cb.bookId = b.bookId " +
                "WHERE c.state =  ?";
        List<Contact> list = new LinkedList<>();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, state);
            ResultSet rs = ps.executeQuery();
            list = searchList(rs, LIST_WITH_BOOK_NAME);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public void delete(int bookId, String firstName, String lastName) {
        try {
            int id = getContactId(bookId, firstName, lastName);
            String query = "DELETE FROM contactBookRegister " +
                    "WHERE contactId = ? " +
                    "AND bookId = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setInt(2, bookId);
            ps.executeUpdate();
            query = "DELETE FROM books WHERE bookId NOT IN (SELECT bookId FROM contactBookRegister)";
            Statement s = con.createStatement();
            s.executeUpdate(query);
            query = "DELETE FROM contacts " +
                    "WHERE id NOT IN (SELECT contactId FROM contactBookRegister)";
            s = con.createStatement();
            s.executeUpdate(query);
            System.out.println("Deleted Successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Contact> sort(String parameter) {
        if (parameter.equals("name")) parameter = "CONCAT (firstName, ' ', lastName)";
        String query = "SELECT * FROM CONTACTS ORDER BY " + parameter;
        List<Contact> list = new LinkedList<>();
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(query);
            list = searchList(rs, LIST_WITHOUT_BOOK_NAME);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public Map<String, Integer> count(String parameter) {
        String query = "SELECT " + parameter + ", COUNT(*) FROM CONTACTS GROUP BY " + parameter;
        Map<String, Integer> map = new HashMap<>();
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(query);
            while (rs.next()) map.put(rs.getString(1), rs.getInt(2));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return map;
    }

    @Override
    public void edit(String whatToEdit, String newValue, int id) {
        String query = "UPDATE contacts SET " + whatToEdit + " = ? WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, newValue);
            ps.setInt(2, id);
            if (ps.executeUpdate() > 0) System.out.println("Edited successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int getContactId(int bookId, String firstName, String lastName) {
        String query = "SELECT c.* " +
                "FROM contacts c " +
                "JOIN contactBookRegister cbr ON c.id = cbr.contactId " +
                "JOIN books b ON cbr.bookId = b.bookId " +
                "WHERE c.firstName = ? " +
                "AND c.lastName = ? " +
                "AND b.bookId = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setInt(3, bookId);
            ResultSet rs = ps.executeQuery();
            printContactsResultSet(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.print("Select contact id : ");
        return new Scanner(System.in).nextInt();
    }

    private void printContactsResultSet(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.print("Id : " + rs.getInt(1) + "  ");
                for (int i = 2; i <= 9; i++) {
                    System.out.print(rs.getString(i) + "  ");
                }
                System.out.print(rs.getDate(10));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void printAllTables() {
        printContactsTable();
        printBooksTable();
        printContactBookRegisterTable();
    }

    private void printContactsTable() {
        String query = "SELECT * FROM contacts";
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(query);
            System.out.println("\nContacts table -> ");
            printContactsResultSet(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void printBooksTable() {
        String query = "SELECT * FROM books";
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(query);
            System.out.println("\nBooks table -> ");
            while (rs.next()) {
                System.out.print("Book Id : " + rs.getInt(1) + " -> ");
                System.out.println("Book Name : " + rs.getString(2));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printContactBookRegisterTable() {
        String query = "SELECT * FROM contactBookRegister";
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(query);
            System.out.println("\nContact Book Register table -> ");
            while (rs.next()) {
                System.out.print("Contact Id : " + rs.getInt(1) + " -> ");
                System.out.println("Book Id : " + rs.getInt(2));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void printAddressBookContacts() {
        printBooksTable();
        System.out.print("Select book id : ");
        int bookId = new Scanner(System.in).nextInt();
        String query = "SELECT * FROM contacts c " +
                "JOIN contactBookRegister cbr ON cbr.contactId=c.id " +
                "JOIN books b ON b.bookId=cbr.bookId " +
                "WHERE b.bookId=?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            printContactsResultSet(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void printContactsBetweenGivenDates() {
        String startDate = "2023-04-5";
        String endDate = "2023-04-10";
        String query = "SELECT * FROM CONTACTS WHERE dateAdded BETWEEN ? AND ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ResultSet rs = ps.executeQuery();
            printContactsResultSet(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
