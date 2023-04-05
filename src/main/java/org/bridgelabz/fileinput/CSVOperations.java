package org.bridgelabz.fileinput;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.bridgelabz.model.Contact;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CSVOperations implements FileIO {

    private static final String INPUT_PATH = "src/main/resources/input/ABDataIn.csv";

    public List<Contact> getData() {
        try (Reader reader = Files.newBufferedReader(Paths.get(INPUT_PATH))) {
            CsvToBeanBuilder<Contact> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(Contact.class);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            csvToBeanBuilder.withSkipLines(1);
            CsvToBean<Contact> csvToBean = csvToBeanBuilder.build();
            return csvToBean.parse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
