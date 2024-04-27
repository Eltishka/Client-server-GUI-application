package server.utilities;

import objectspace.Vehicle;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class VehicleOwnerPair<X extends Vehicle, Y> extends Pair<X, Y> implements Comparable, Serializable {

    public VehicleOwnerPair(X first, Y second) {
        super(first, second);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this.getFirst().compareTo((Vehicle) ((Pair)o).getFirst());
    }

    @Override
    public boolean equals(Object o){
        if(!o.getClass().equals(VehicleOwnerPair.class))
            return false;
        return this.getFirst().equals(((Pair)o).getFirst());
    }

    @Override
    public String toString(){
        return this.getFirst() + ", Владелец: " + this.getSecond();
    }

}
