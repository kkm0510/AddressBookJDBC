package org.bridgelabz.fileinput;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bridgelabz.model.Contact;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONOperations implements FileIO {

    private static final String INPUT_PATH = "src/main/resources/input/ABDataIn.json";

    public List<Contact> getData() {
        Map<String, List<Contact>> map = getMapData();
        return map == null ? null : map.entrySet()
                .stream()
                .flatMap(entry -> entry.getValue()
                        .stream()
                        .peek(contact -> contact.setBookName(entry.getKey())))
                .toList();
    }

    private Map<String, List<Contact>> getMapData() {
        try (Reader reader = Files.newBufferedReader(Paths.get(INPUT_PATH))) {
            Gson gson = new Gson();
            return gson.fromJson(reader, new TypeToken<Map<String, List<Contact>>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
