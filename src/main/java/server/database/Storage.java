package server.database;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
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
public class Storage<T extends Vehicle> extends LinkedHashSet<T> {

    /**
     * ДАта создания коллекции
     */
    @JacksonXmlProperty(localName = "creationDate", isAttribute = false)
    private Date creationDate;

    public Storage(Collection<T> collection) {
        super(collection);
        this.creationDate = new Date();
    }



    public Storage() {
        super();
        this.creationDate = new Date();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date date) {
        this.creationDate = creationDate;
    }
}