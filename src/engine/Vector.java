package engine;

public class Vector {
    public double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector normalize() {
        double magnitude = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return new Vector(this.x / magnitude, this.y / magnitude, this.z / magnitude);
    }

    public double dot(Vector v2) {
        double x = this.x * v2.x;
        double y = this.y * v2.y;
        double z = this.z * v2.z;

        return x + y + z;
    }
}