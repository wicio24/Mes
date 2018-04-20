package mes;

public class Node {
    double x, y, t;
    int status;

    public Node ( double x, double y, double t ,GlobalData globalData) {

        double B = globalData.getB();
        double H = globalData.getH();

        this.x = x;
        this.y = y;
        this.t = t;


            if (this.x == 0.0 || this.x == B )
                status = 1;
        else status = 0;
    }

    public double getX () {
        return x;
    }

    public double getY () {
        return y;
    }

    public double getT () {
        return t;
    }

    public int getStatus () {
        return status;
    }

    public void setT ( double t ) {
        this.t = t;
    }
}
