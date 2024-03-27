package objectspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Класс координат в формате (x, y)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates implements Serializable {
    private Double x; //Поле не может быть null
    private Long y; //Поле не может быть null

    @Override
    public String toString(){
        return "(" + x.toString() + ", " + y.toString() + ")";
    }
}