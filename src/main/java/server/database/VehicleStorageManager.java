package server.database;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import objectspace.Vehicle;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
/**
 * Класс коллекции, расширяющий LinkedHashSet. Основное отличие в хранение и предоставлении даты создания
 * @author Piromant
 */
@JacksonXmlRootElement(localName = "Storage")
public interface VehicleStorageManager<T extends Vehicle>  {

    boolean add(T el, String owner);
    boolean update(T el, String userName) throws UserPermissionException;
    boolean remove(T el, String userName) throws UserPermissionException;
    Collection getCollection();
    void clear(String userName);
    int size();

}