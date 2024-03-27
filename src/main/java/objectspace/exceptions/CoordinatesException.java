package objectspace.exceptions;
/**
 * Класс, расширяющий ArgumentVehicleException, обозначающий ошибку в работе с координатами
 * @see objectspace.Coordinates
 * @author Piromant
 */
public class CoordinatesException extends ArgumentVehicleException{
    public CoordinatesException(String msg, int argumentNumber){
        super(msg, argumentNumber);
    }
    public CoordinatesException(String msg, Throwable cause, int argumentNumber){
        super(msg, cause, argumentNumber);
    }
}
