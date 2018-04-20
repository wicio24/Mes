package mes;


public class Jakobian {

    private final double J[][];
    private final double J_inverted[][];
    private final double det;

    private final int punktCalkowania; // 0 | 1 | 2 | 3
    public static final ElementLocal ELEMENT_LOCAL = ElementLocal.getInstance();

    public Jakobian(int pktCalkowania, double x[], double y[]) {

        this.punktCalkowania = pktCalkowania;

        J = new double[2][2];
        J[0][0] = ELEMENT_LOCAL.getdN_dKsi()[punktCalkowania][0] * x[0] + ELEMENT_LOCAL.getdN_dKsi()[punktCalkowania][1] * x[1] + ELEMENT_LOCAL.getdN_dKsi()[punktCalkowania][2] * x[2] + ELEMENT_LOCAL.getdN_dKsi()[punktCalkowania][3] * x[3];
        J[0][1] = ELEMENT_LOCAL.getdN_dKsi()[punktCalkowania][0] * y[0] + ELEMENT_LOCAL.getdN_dKsi()[punktCalkowania][1] * y[1] + ELEMENT_LOCAL.getdN_dKsi()[punktCalkowania][2] * y[2] + ELEMENT_LOCAL.getdN_dKsi()[punktCalkowania][3] * y[3];
        J[1][0] = ELEMENT_LOCAL.getdN_dEta()[punktCalkowania][0] * x[0] + ELEMENT_LOCAL.getdN_dEta()[punktCalkowania][1] * x[1] + ELEMENT_LOCAL.getdN_dEta()[punktCalkowania][2] * x[2] + ELEMENT_LOCAL.getdN_dEta()[punktCalkowania][3] * x[3];
        J[1][1] = ELEMENT_LOCAL.getdN_dEta()[punktCalkowania][0] * y[0] + ELEMENT_LOCAL.getdN_dEta()[punktCalkowania][1] * y[1] + ELEMENT_LOCAL.getdN_dEta()[punktCalkowania][2] * y[2] + ELEMENT_LOCAL.getdN_dEta()[punktCalkowania][3] * y[3];

        det = J[0][0] * J[1][1] - J[0][1] * J[1][0];

        J_inverted = new double[2][2];

        J_inverted[0][0] = J[1][1];
        J_inverted[0][1] = -J[0][1];
        J_inverted[1][0] = -J[1][0];
        J_inverted[1][1] = J[0][0];
    }



    public void wypiszJakobian() {
        System.out.println("Jakobian punktu calkowania id:" + punktCalkowania);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(+J[i][j] + "\t");
            }
            System.out.println("");
        }
        System.out.println("Det: " + det + "\n");
    }

    public double[][] getJ() {
        return J;
    }

    public double getDet() {
        return det;
    }

    public ElementLocal getEl() {
        return ELEMENT_LOCAL;
    }

    public double[][] getJ_inverted() {
        return J_inverted;
    }
}