package objectspace.exceptions;

/**
 * Класс, расширяющий RuntimeException, обозначающий ошибку в работе с Vehicle
 * @see objectspace.Vehicle
 * @author Piromant
 */
public class VehicleException extends RuntimeException{
    public VehicleException(String msg){
        super(msg);
    }
    public VehicleException(String msg, Throwable cause){
        super(msg, cause);
    }
}
