package objectspace;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import objectspace.exceptions.*;
import server.utilities.IDGenerator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * Класс средства передвижения, объекты которого являются элементами коллекции Storage
 * @see server.database.Storage
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "Vehicle")
public class Vehicle implements Comparable<Vehicle>, Serializable {
    private @NonNull Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private @NonNull String name; //Поле не может быть null, Строка не может быть пустой
    /**
     * @see Coordinates
     */
    private @NonNull Coordinates coordinates; //Поле не может быть null
    /**
     * Дата создания объекта
     */
    private @NonNull java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    /**
     * Сила двигателя
     */
    private @NonNull Long enginePower; //Поле не может быть null, Значение поля должно быть больше 0
    /**
     * @see VehicleType
     */
    private @NonNull VehicleType type; //Поле не может быть null
    /**
     * @see FuelType
     */
    private @NonNull FuelType fuelType; //Поле не может быть null



    public Vehicle(@NonNull String name, @NonNull Coordinates coordinates, @NonNull Long enginePower, @NonNull VehicleType type, @NonNull FuelType fuelType) throws VehicleException {
        this.id = IDGenerator.generateID();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.enginePower = enginePower;
        this.type = type;
        this.fuelType = fuelType;

        if(enginePower <= 0){
            throw new VehicleException("enginePower должен быть больше 0", new IllegalArgumentException(enginePower.toString()));
        }
    }


    public Vehicle(int id){
        this.id = id;
    }

    @Override
    public int compareTo(Vehicle o) {
        int res = this.enginePower.compareTo(o.getEnginePower()) ;
        if(res == 0)
            res = this.id.compareTo(o.getId());
        return res;
    }

    @Override
    public boolean equals(Object v){
        if(!(v instanceof Vehicle))
            return false;
        return this.id.equals(((Vehicle) v).getId());
    }

    @Override
    public int hashCode(){
        return id;
    }


    /**
     * Метод, создающий объект из аргументов (имени, координат, силы двигатея, типа, типа топлива)
     * и выбрасывающий исключения в случае невалидных аргументов
     * @param args агрументы - поля нового объекта
     * @return Объект типа Vehicle
     * @throws VehicleException
     */
    public static Vehicle parseVehicle(String[] args) throws VehicleException{

        if(args.length < 5 || Arrays.asList(args).contains(null)){
            throw new VehicleException("Недостаточно аргументов для создания объекта", new IllegalArgumentException(Integer.valueOf(args.length).toString()));
        }

        try {

            String name = args[0].trim();
            Coordinates coordinates = null;
            VehicleType type = null;
            FuelType fuelType = null;
            Long enginePower = null;

            if(name.equals(""))
                throw new VehicleNameException("Имя не может быть пустой строкой", 1);

            try {
                String[] coord = args[1].split(" ");
                Double x = Double.parseDouble(coord[0]);
                Long y = Long.parseLong(coord[1]);
                coordinates = new Coordinates(x, y);
            }
            catch (Exception e){
                throw new CoordinatesException("Неверный формат ввода координат. Правильно: (x, y) x - дробное, y - целое меньше 2^63", e, 2);
            }

            try {
                enginePower = Long.parseLong(args[2].trim());
            }
            catch (Exception e){
                throw new EnginePowerException("Сила двигателя должна быть целым числом большим 0 и меньшим 2^63", e, 3);
            }

            try {
                type = VehicleType.valueOf(args[3].trim());
            }
            catch (Exception e){
                throw new VehicleTypeException("Несуществующий тип средства передвижения.", e, 4);
            }

            try {
                fuelType = FuelType.valueOf(args[4].trim());
            }
            catch (Exception e){
                throw new FuelTypeException("Несуществующий тип топлива", e, 5);
            }

            return new Vehicle(name, coordinates, enginePower, type, fuelType);
        }
        catch (Exception e){
            throw new VehicleException("Неверный формат ввода аргументов объекта.", e);
        }

    }
}