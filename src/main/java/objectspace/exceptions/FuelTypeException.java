package objectspace.exceptions;

/**
 * Класс, расширяющий ArgumentVehicleException, обозначающий ошибку в работе с типом топлива (FuelType)
 * @see objectspace.FuelType
 * @author Piromant
 */
public class FuelTypeException extends ArgumentVehicleException{
    public FuelTypeException(String msg, int argumentNumber){
        super(msg, argumentNumber);
    }
    public FuelTypeException(String msg, Throwable cause, int argumentNumber){
        super(msg, cause, argumentNumber);
    }
}
