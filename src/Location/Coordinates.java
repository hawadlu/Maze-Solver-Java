package Location;

import java.util.Objects;

/**
 * Very simple class for storing coordinates
 */
public class Coordinates {
    final int x;
    final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x and y
     * @return x/y coordinates
     */
    public int getX() {return this.x;}
    public int getY() {return  this.y;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
