package org.bridgelabz.fileinput;

import org.apache.poi.ss.usermodel.*;
import org.bridgelabz.model.Contact;

import java.io.File;
import java.util.*;


public class EXCELOperations implements FileIO {

    private static final String INPUT_PATH = "src/main/resources/input/ABDataIn.xlsx";

    public List<Contact> getData() {
        List<Contact> list = new LinkedList<>();
        try (Workbook workbook = WorkbookFactory.create(new File(INPUT_PATH))) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Contact c = new Contact();
                c.setBookName(dataFormatter.formatCellValue(row.getCell(0)));
                c.setFirstName(dataFormatter.formatCellValue(row.getCell(1)));
                c.setLastName(dataFormatter.formatCellValue(row.getCell(2)));
                c.setAddress(dataFormatter.formatCellValue(row.getCell(3)));
                c.setCity(dataFormatter.formatCellValue(row.getCell(4)));
                c.setState(dataFormatter.formatCellValue(row.getCell(5)));
                c.setPin(dataFormatter.formatCellValue(row.getCell(6)));
                c.setPhoneNumber(dataFormatter.formatCellValue(row.getCell(7)));
                c.setEmail(dataFormatter.formatCellValue(row.getCell(8)));
                list.add(c);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}


