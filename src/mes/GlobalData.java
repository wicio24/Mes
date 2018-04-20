package mes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class GlobalData {

    private double H, B; //wysokosc, szerokosc
    private int nH, nB; //liczba wezlow po wysokosci i po szerokosci

    private int nh; //liczba wezlow
    private int ne; //liczba elementow


    private double dB; // odlegosc miedzy wezlami
    private double dH; // odlegosc miedzy wezlami


    private double t_begin; //temperatura poczatkowa
    private double tau; //czas procesu
    private double dtau; //poczatkowa wartosc przyrostu czasu
    private double t_otoczenia; //temperatura otoczenia
    private double t_otoczenia2; //temperatura otoczenia 2
    private double c; //cieplo wlasciwe
    private double alfa; //wspolczynnik wymiany ciepla
    private double k; //wspolczynnik przewodzenia ciepla
    private double ro; //gestosc

    private double cPow; //cieplo wlasciwe
    private double alfaPow; //wspolczynnik wymiany ciepla
    private double kPow; //wspolczynnik przewodzenia ciepla
    private double roPow; //gestosc


    private ElementLocal elementLocal;
    private double[][] H_element;
    private double[] P_element;
    private double[][] H_global;
    private double[] P_global;

    private static GlobalData globalData;



    public GlobalData() throws FileNotFoundException
    {

        Scanner scanner = new Scanner(new File("data.txt"));
        scanner.hasNextDouble();

        this.H = scanner.nextDouble();             scanner.findInLine(";");
        this.B = scanner.nextDouble();             scanner.findInLine(";");
        this.nH = scanner.nextInt();               scanner.findInLine(";");
        this.nB = scanner.nextInt();               scanner.findInLine(";");
        this.t_begin = scanner.nextDouble();       scanner.findInLine(";");
        this.tau = scanner.nextDouble();           scanner.findInLine(";");
        this.dtau = scanner.nextDouble();          scanner.findInLine(";");
        this.t_otoczenia = scanner.nextDouble();   scanner.findInLine(";");
        this.t_otoczenia2 = scanner.nextDouble();  scanner.findInLine(";");
        this.alfa = scanner.nextDouble();          scanner.findInLine(";");
        this.c = scanner.nextDouble();             scanner.findInLine(";");
        this.k = scanner.nextDouble();             scanner.findInLine(";");
        this.ro = scanner.nextDouble();            scanner.findInLine(";");
        this.alfaPow = scanner.nextDouble();       scanner.findInLine(";");
        this.cPow = scanner.nextDouble();          scanner.findInLine(";");
        this.kPow = scanner.nextDouble();          scanner.findInLine(";");
        this.roPow = scanner.nextDouble();         scanner.close();


        dB = B / ( nB - 1 );  // odlegosc miedzy wezlami
        dH = H / ( nH - 1 );  // odlegosc miedzy wezlami



        ne = (nH - 1) * (nB - 1); // liczba elementów w siatce
        nh = nH * nB;            // liczba wezłów w siatce

        elementLocal = ElementLocal.getInstance();
        H_element = new double[4][4];
        P_element = new double[4];
        H_global = new double[nh][nh];
        P_global = new double[nh];

    }

    public void compute() throws FileNotFoundException {

        for (int i = 0; i < nh; i++) {
            for (int j = 0; j < nh; j++) {
                H_global[i][j] = 0;
            }
            P_global[i] = 0;
        }

        Grid grid = Grid.getInstance();
        Jakobian jakobian;
        double[] dNdx = new double[4];   //wektor pochodnych funkcji kształtu po x
        double[] dNdy = new double[4];   //wektor pochodnych funkcji kształtu po x
        double[] x = new double[4];
        double[] y = new double[4];
        double[] temp_0 = new double[4];
        double t0p, C;
        int id;
        double detj = 0;

        for (int el_nr = 0; el_nr < ne; el_nr++) { //petla po wszytkich elementach w siatce

            for (int i = 0; i < 4; i++) { //petla po macierzach 1 elementu
                for (int j = 0; j < 4; j++) {
                    H_element[i][j] = 0;
                }
                P_element[i] = 0;
            }

            for (int i = 0; i < 4; i++) {     //zapis danych z elementu siatki do tablic x[], y[] temp_0[], ktore wykorzystuje do dalszych obliczen
                id = grid.elements[el_nr].globalNodeID[i];
                x[i] = grid.nodes[id].getX();
                y[i] = grid.nodes[id].getY();
                temp_0[i] = grid.nodes[id].getT();
            }


            //calkownie 2 - punktowe
            for (int punktCalkowania = 0; punktCalkowania < 4; punktCalkowania++) // 4 - liczba punktow calkowania po powierzchni w elemencie
            {
                jakobian = new Jakobian(punktCalkowania, x, y);  // jakobian liczony osobno dla kazdego punktu całkowania
                t0p = 0;


                // petla po wszystkich wezłach w elemencie
                for (int j = 0; j < 4; j++)  // 4 - liczba wezlow w wykorzystywanym elemencie skonczonym
                {
                    dNdx[j] = 1.0 / jakobian.getDet() * (jakobian.getJ_inverted()[0][0] * elementLocal.getdN_dKsi()[punktCalkowania][j]
                            + jakobian.getJ_inverted()[0][1] * elementLocal.getdN_dEta()[punktCalkowania][j]);

                    dNdy[j] = 1.0 / jakobian.getDet() * (jakobian.getJ_inverted()[1][0] * elementLocal.getdN_dKsi()[punktCalkowania][j]
                            + jakobian.getJ_inverted()[1][1] * elementLocal.getdN_dEta()[punktCalkowania][j]);

                    t0p += temp_0[j] * elementLocal.getN()[punktCalkowania][j];
                }


                // Math.abs() - wartosc bezwzględna
                detj = Math.abs(jakobian.getDet());

                // 2 petle aby przejsc po macierzy 4x4, ktora jest tworzona z wektorow 4 elementowych
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        C = grid.elements[el_nr].c * grid.elements[el_nr].ro * elementLocal.getN()[punktCalkowania][i] * elementLocal.getN()[punktCalkowania][j] * detj;
                        H_element[i][j] += grid.elements[el_nr].k * (dNdx[i] * dNdx[j] + dNdy[i] * dNdy[j]) * detj + C / dtau;
                        P_element[i] += C / dtau * t0p;
                    }
                }
            }




            //warunki brzegowe
            //petla po powierzchniach majacych kontakt z otoczeniem
            for (int ipow = 0; ipow < grid.elements[el_nr].getN_pow(); ipow++) {
                id = grid.elements[el_nr].getA_pow()[ipow];    //zapis id powierzchni lokalnej

//                /*
//                switch (id) {
//                    case 0:
//                        detj = Math.sqrt(Math.pow(grid.elements[el_nr].ND[3].getX() - grid.elements[el_nr].ND[0].getX(), 2)
//                                + Math.pow(grid.elements[el_nr].ND[3].getY() - grid.elements[el_nr].ND[0].getY(), 2)) / 2.0;
//                        break;
//                    case 1:
//                        detj = Math.sqrt(Math.pow(grid.elements[el_nr].ND[0].getX() - grid.elements[el_nr].ND[1].getX(), 2)
//                                + Math.pow(grid.elements[el_nr].ND[0].getY() - grid.elements[el_nr].ND[1].getY(), 2)) / 2.0;
//                        break;
//                    case 2:
//                        detj = Math.sqrt(Math.pow(grid.elements[el_nr].ND[1].getX() - grid.elements[el_nr].ND[2].getX(), 2)
//                                + Math.pow(grid.elements[el_nr].ND[1].getY() - grid.elements[el_nr].ND[2].getY(), 2)) / 2.0;
//                        break;
//                    case 3:
//                        detj = Math.sqrt(Math.pow(grid.elements[el_nr].ND[2].getX() - grid.elements[el_nr].ND[3].getX(), 2)
//                                + Math.pow(grid.elements[el_nr].ND[2].getY() - grid.elements[el_nr].ND[3].getY(), 2)) / 2.0;
//                        break;
//                }
//                */

                switch (id) {
                    case 0:
                        detj = Math.sqrt(Math.pow(grid.elements[el_nr].ND[3].getX() - grid.elements[el_nr].ND[0].getX(), 2)
                                + Math.pow(grid.elements[el_nr].ND[3].getY() - grid.elements[el_nr].ND[0].getY(), 2)) / 2.0;

                        // 2 punkty calkowania po powierzchni
                        for (int p = 0; p < 2; p++)
                        {
                            // 2 petle aby przejsc po macierzy 4x4
                            // dodanie warunku brzegowego na powierzchni
                            for (int n = 0; n < 4; n++)
                            {
                                for (int i = 0; i < 4; i++)
                                {
                                    H_element[n][i] += grid.elements[el_nr].alfa * elementLocal.getPOW()[id].N[p][n] * elementLocal.getPOW()[id].N[p][i] * detj;
                                }
                                P_element[n] += globalData.alfa * t_otoczenia * elementLocal.getPOW()[id].N[p][n] * detj;
                            }
                        }
                        break;


                    case 2:
                        detj = Math.sqrt(Math.pow(grid.elements[el_nr].ND[1].getX() - grid.elements[el_nr].ND[2].getX(), 2)
                                + Math.pow(grid.elements[el_nr].ND[1].getY() - grid.elements[el_nr].ND[2].getY(), 2)) / 2.0;

                        // 2 punkty calkowania po powierzchni
                        for (int p = 0; p < 2; p++)
                        {
                            // 2 petle aby przejsc po macierzy 4x4
                            // dodanie warunku brzegowego na powierzchni
                            for (int n = 0; n < 4; n++)
                            {
                                for (int i = 0; i < 4; i++)
                                {
                                    H_element[n][i] += grid.elements[el_nr].alfa * elementLocal.getPOW()[id].N[p][n] * elementLocal.getPOW()[id].N[p][i] * detj;
                                }
                                P_element[n] += globalData.alfa * t_otoczenia2 * elementLocal.getPOW()[id].N[p][n] * detj;
                            }
                        }
                        break;

                }


               /* // 2 punkty calkowania po powierzchni
                for (int p = 0; p < 2; p++)
                {
                    // 2 petle aby przejsc po macierzy 4x4
                    // dodanie warunku brzegowego na powierzchni
                    for (int n = 0; n < 4; n++)
                    {
                        for (int i = 0; i < 4; i++)
                        {
                            H_element[n][i] += grid.elements[el_nr].alfa * elementLocal.getPOW()[id].N[p][n] * elementLocal.getPOW()[id].N[p][i] * detj;
                        }
                        P_element[n] += grid.elements[el_nr].alfa * t_otoczenia * elementLocal.getPOW()[id].N[p][n] * detj;
                    }
                }*/
            }


            //agregacja (złożenie) macierzy sztywności poszczegolnych elementów w globalną macierz sztywności
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    H_global[grid.elements[el_nr].globalNodeID[i]][grid.elements[el_nr].globalNodeID[j]] += H_element[i][j];
                }
                P_global[grid.elements[el_nr].globalNodeID[i]] += P_element[i];
            }


           /* if (el_nr == ne - 1) {
                for (int i = 0; i < nh; i++) {
                    for (int j = 0; j < nh; j++) {
                        System.out.printf("%.5f\t", H_global[i][j]);
                    }
                    System.out.println("");
                }
                System.out.println("");
                for (int i = 0; i < nh; i++)
                {
                    System.out.printf("%.4f\t", P_global[i]);
                }
                System.out.println("");
            }*/










        }
    }

    public static GlobalData getInstance() throws FileNotFoundException {
        if (globalData == null) {
            globalData = new GlobalData();
        }
        return globalData;
    }

    public double getH() {
        return H;
    }

    public double getB() {
        return B;
    }

    public double getdB() {
        return dB;
    }

    public void setdB(double dB) {
        this.dB = dB;
    }

    public double getdH() {
        return dH;
    }

    public void setdH(double dH) {
        this.dH = dH;
    }

    public int getnH() {
        return nH;
    }

    public int getnB() {
        return nB;
    }

    public int getNh() {
        return nh;
    }

    public int getNe() {
        return ne;
    }

    public double getT_begin() {
        return t_begin;
    }

    public double getTau() {
        return tau;
    }

    public double getDtau() {
        return dtau;
    }

    public double getT_otoczenia() {
        return t_otoczenia;
    }

    public double getAlfa() {
        return alfa;
    }

    public double getC() {
        return c;
    }

    public double getK() {
        return k;
    }

    public double getRo() {
        return ro;
    }

    public ElementLocal getEl_lok() {
        return elementLocal;
    }

    public double[][] getH_element() {
        return H_element;
    }

    public double[] getP_element() {
        return P_element;
    }

    public double[][] getH_global() {
        return H_global;
    }

    public double[] getP_global() {
        return P_global;
    }

    public void setDtau(double dtau) {
        this.dtau = dtau;
    }

    public double getcPow()
    {
        return cPow;
    }

    public void setcPow(double cPow)
    {
        this.cPow = cPow;
    }

    public double getAlfaPow()
    {
        return alfaPow;
    }

    public void setAlfaPow(double alfaPow)
    {
        this.alfaPow = alfaPow;
    }

    public double getkPow()
    {
        return kPow;
    }

    public void setkPow(double kPow)
    {
        this.kPow = kPow;
    }

    public double getRoPow()
    {
        return roPow;
    }

    public void setRoPow(double roPow)
    {
        this.roPow = roPow;
    }
}