package server.filework;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import objectspace.Coordinates;
import objectspace.FuelType;
import objectspace.Vehicle;
import objectspace.VehicleType;
import server.database.Storage;
import com.fasterxml.jackson.dataformat.xml.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Класс, реализующий StorageLoader. Служит для загрузки коллекции из XML-файла
 * @see StorageLoader
 * @author Piromant
 */
public class XMLLoader implements StorageLoader{
    /**
     * @see FileReader
     */
    private FileReader fileReader;
    /**
     * Имя файла, из которого загружается коллекция
     */
    private String fileName;
    public XMLLoader(FileReader fileReader, String fileName){
        this.fileReader = fileReader;
        this.fileName = fileName;
    }

    @Override
    public Storage<Vehicle> loadStorage() throws IOException, ParseException, NullPointerException {

        Storage<Vehicle> storage = new Storage<>();
        XmlMapper objectMapper = new XmlMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new StorageModule());
        String fileContent = this.fileReader.readWholeFile(fileName);
        storage = objectMapper.readValue(fileContent, Storage.class);
        return storage;
    }

    class StorageModule extends SimpleModule {
        public StorageModule() {
            addDeserializer(Storage.class, new StorageDeserializer());
        }
    }

    class StorageDeserializer extends StdDeserializer<Storage> {

        private ObjectMapper objectMapper;

        public StorageDeserializer() {
            this(null);
        }

        public StorageDeserializer(Class<?> vc) {
            super(vc);
            this.objectMapper = new XmlMapper();
        }

        @Override
        public Storage deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonNode node = jp.getCodec().readTree(jp);
            //Date creationDate = objectMapper.convertValue(node.get("creationDate"), Date.class);
            Storage storage = new Storage();

            JsonNode itemsNode = node.get("item");
            if (itemsNode != null && itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {

                    try {
                        Vehicle item = objectMapper.treeToValue(itemNode, Vehicle.class);

                        storage.add(item);
                    } catch (Exception e) {
                        System.out.println("Ошикба при чтении элемента, элемент был пропущен: " + e.getMessage());
                    }
                }
            } else if(itemsNode != null){
                storage.add(objectMapper.treeToValue(itemsNode, Vehicle.class));
            }
            return storage;
        }
    }

}
