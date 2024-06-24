package dacd.torrealba.project.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class DataLakeBuilder implements DataLake {
    private final String path;
    private final String topic;

    public DataLakeBuilder(String path, String topic) {
        this.path = path;
        this.topic = topic;
    }


    @Override
    public void save(String message) {
        String ss = getJsonMessage(message, "ss");
        String pathToDirectory = path + File.separator + topic + File.separator + ss;
        createDirectory(pathToDirectory);

        String date = getJsonMessage(message, "ts");
        Instant dateToInstant = Instant.parse(date);
        String dateFormat = new SimpleDateFormat("yyyyMMdd").format(Date.from(dateToInstant));

        String eventPath = pathToDirectory + File.separator + dateFormat + ".events";

        try {
            FileWriter writer = new FileWriter(eventPath, true);
            writer.write(message + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + eventPath);
            e.printStackTrace();
        }
    }
    public String getJsonMessage (String message, String field) {
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        return jsonObject.get(field).getAsString();
    }
    public void createDirectory(String path) {
        File newDirectory = new File(path);
        if (!newDirectory.exists()) {
            boolean created = newDirectory.mkdirs();
            if (created) {
                System.out.println("Directorio creado: " + path);
            } else {
                System.err.println("Error al crear el directorio: " + path);
            }
        }
    }
}
