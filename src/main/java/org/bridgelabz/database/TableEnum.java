package org.bridgelabz.database;

public enum TableEnum {

    CONTACTS ("contacts", "id int AUTO_INCREMENT PRIMARY KEY, " +
            "firstName varchar(30) not null, " +
            "lastName varchar(30) not null, " +
            "address varchar(150) not null, " +
            "city varchar(30) not null, " +
            "state varchar(30) not null, " +
            "pin varchar(6) not null, " +
            "phoneNumber varchar(10) not null, " +
            "email varchar(150) not null"),

    BOOKS ("books","bookId int auto_increment primary key, bookName varchar(30) not null"),

    CONTACT_BOOK_REGISTER ("contactBookRegister","contactId int not null, " +
            "bookId int not null, " +
            "primary key (contactId, bookId), " +
            "foreign key (bookId) references books (bookId), " +
            "foreign key (contactId) references contacts (id)");

    private final String NAME;
    private final String DESCRIPTION;

    TableEnum(String name, String description){
        DESCRIPTION =description;
        NAME=name;
    }

    public String getNAME() {
        return NAME;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

}
