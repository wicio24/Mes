package mes;


public class ElementLocal
{

    private NodeLocal[] ID = { //punkty calkowania gaussa
            new NodeLocal(-1.0 / Math.sqrt(3.0), -1.0 / Math.sqrt(3.0)),
            new NodeLocal(1.0 / Math.sqrt(3.0), -1.0 / Math.sqrt(3.0)),
            new NodeLocal(1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0)),
            new NodeLocal(-1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0))
    };

    private SurfaceLocal[] POW = { //punkty calkowania gaussa dla powierzchni
            new SurfaceLocal(new NodeLocal(-1.0, 1.0 / Math.sqrt(3.0)), new NodeLocal(-1.0, -1.0 / Math.sqrt(3.0))),
            new SurfaceLocal(new NodeLocal(-1.0 / Math.sqrt(3.0), -1.0), new NodeLocal(1.0 / Math.sqrt(3.0), -1.0)),
            new SurfaceLocal(new NodeLocal(1.0, -1.0 / Math.sqrt(3.0)), new NodeLocal(1.0, 1.0 / Math.sqrt(3.0))),
            new SurfaceLocal(new NodeLocal(1.0 / Math.sqrt(3.0), 1.0), new NodeLocal(-1.0 / Math.sqrt(3.0), 1.0))
    };

    private double dN_dKsi[][];
    private double dN_dEta[][];

    private double N[][];

    private static ElementLocal lokalElement = null;

    private ElementLocal() {


        N = new double[4][4];        //macierz funkcji kształtu
        dN_dKsi = new double[4][4];  //pochodna macierzy funkcji kształtu po Ksi
        dN_dEta = new double[4][4];  //pochodna macierzy funkcji kształtu po Eta

        for (int i = 0; i < 4; i++) {
            N[i][0] = N1(ID[i].getKsi(), ID[i].getEta());
            N[i][1] = N2(ID[i].getKsi(), ID[i].getEta());
            N[i][2] = N3(ID[i].getKsi(), ID[i].getEta());
            N[i][3] = N4(ID[i].getKsi(), ID[i].getEta());

            dN_dKsi[i][0] = dN1dKsi(ID[i].getKsi(), ID[i].getEta());
            dN_dKsi[i][1] = dN2dKsi(ID[i].getKsi(), ID[i].getEta());
            dN_dKsi[i][2] = dN3dKsi(ID[i].getKsi(), ID[i].getEta());
            dN_dKsi[i][3] = dN4dKsi(ID[i].getKsi(), ID[i].getEta());

            dN_dEta[i][0] = dN1dEta(ID[i].getKsi(), ID[i].getEta());
            dN_dEta[i][1] = dN2dEta(ID[i].getKsi(), ID[i].getEta());
            dN_dEta[i][2] = dN3dEta(ID[i].getKsi(), ID[i].getEta());
            dN_dEta[i][3] = dN4dEta(ID[i].getKsi(), ID[i].getEta());

        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) { //macierze funkcji ksztaltu dla powierzchni
                POW[i].N[j][0] = N1(POW[i].ND[j].getKsi(), POW[i].ND[j].getEta());
                POW[i].N[j][1] = N2(POW[i].ND[j].getKsi(), POW[i].ND[j].getEta());
                POW[i].N[j][2] = N3(POW[i].ND[j].getKsi(), POW[i].ND[j].getEta());
                POW[i].N[j][3] = N4(POW[i].ND[j].getKsi(), POW[i].ND[j].getEta());
            }
        }
    }

    public static ElementLocal getInstance() {
        if (lokalElement == null) {
            lokalElement = new ElementLocal();
        }
        return lokalElement;
    }

    private double N1(double ksi, double eta) {
        return 0.25 * (1 - ksi) * (1 - eta);
    }

    private double N2(double ksi, double eta) {
        return 0.25 * (1 + ksi) * (1 - eta);
    }

    private double N3(double ksi, double eta) {
        return 0.25 * (1 + ksi) * (1 + eta);
    }

    private double N4(double ksi, double eta) {
        return 0.25 * (1 - ksi) * (1 + eta);
    }

    private double dN1dKsi(double ksi, double eta) {
        return -0.25 * (1 - eta);
    }

    private double dN2dKsi(double ksi, double eta) {
        return 0.25 * (1 - eta);
    }

    private double dN3dKsi(double ksi, double eta) {
        return 0.25 * (1 + eta);
    }

    private double dN4dKsi(double ksi, double eta) {
        return -0.25 * (1 + eta);
    }

    private double dN1dEta(double ksi, double eta) {
        return -0.25 * (1 - ksi);
    }

    private double dN2dEta(double ksi, double eta) {
        return -0.25 * (1 + ksi);
    }

    private double dN3dEta(double ksi, double eta) {
        return 0.25 * (1 + ksi);
    }

    private double dN4dEta(double ksi, double eta) {
        return 0.25 * (1 - ksi);
    }

    public double[][] getdN_dKsi() {
        return dN_dKsi;
    }

    public double[][] getdN_dEta() {
        return dN_dEta;
    }

    public double[][] getN() {
        return N;
    }

    public SurfaceLocal[] getPOW() {
        return POW;
    }

}