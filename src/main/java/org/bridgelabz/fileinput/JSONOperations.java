package org.bridgelabz.fileinput;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bridgelabz.model.Contact;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JSONOperations {

    public static final String INPUT_PATH = "src/main/resources/input/ABDataIn.json";

    public List<Contact> getData() {
        try (Reader reader = Files.newBufferedReader(Paths.get(INPUT_PATH))) {
            Gson gson = new Gson();
            return gson.fromJson(reader, new TypeToken<List<Contact>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
