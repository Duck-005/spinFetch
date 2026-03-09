package engine;

public class Rotation {
    public Vector rotateX(Vector v, double A) {
        double x = v.x;
        double y = v.y * Math.cos(Math.toRadians(A)) - v.z * Math.sin(Math.toRadians(A));
        double z = v.y * Math.sin(Math.toRadians(A)) + v.z * Math.cos(Math.toRadians(A));

        return new Vector(x, y, z);
    }

    public Vector rotateZ(Vector v, double A) {
        double x = v.x * Math.cos(Math.toRadians(A)) - v.y * Math.sin(Math.toRadians(A));
        double y = v.x * Math.sin(Math.toRadians(A)) + v.y * Math.cos(Math.toRadians(A));
        double z = v.z;

        return new Vector(x, y, z);
    }

    public Vector rotateY(Vector v, double A) {
        double x = v.x * Math.cos(Math.toRadians(A)) + v.z * Math.sin(Math.toRadians(A));
        double y = v.y;
        double z = - v.x * Math.sin(Math.toRadians(A)) + v.z * Math.cos(Math.toRadians(A));

        return new Vector(x, y, z);
    }
}