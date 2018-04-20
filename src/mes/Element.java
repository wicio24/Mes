package mes;

import java.io.FileNotFoundException;

public class Element {



    public Node[] ND; // wezly w elemencie
    public Surface POW[]; //powierzchnie elementu

    private int numberOfSurface; // liczba zewnetrznych powierzchni
    private final int[] a_pow; // lokalne numery powierzchni kontaktowych elementu

    public int[] globalNodeID;

    GlobalData globalData;
    double alfa; //wspolczynnik wymiany ciepla
    double c; //cieplo wlasciwe
    double k; //wspolczynnik przewodzenia ciepla
    double ro; //gestosc
    public Element(int i, int j, Node[] NDs, double alfa, double c, double k, double ro) throws FileNotFoundException {


        
        this.alfa=alfa;
        this.c=c;
        this.k=k;
        this.ro=ro;
        
        ND = new Node[4];
        POW = new Surface[4];
        globalNodeID = new int[4];
        globalData = GlobalData.getInstance();


        
        
        //wyznaczamy wspolrzedne wezlow w elemencie
        ND[0] = NDs[0];
        ND[1] = NDs[1];
        ND[2] = NDs[2];
        ND[3] = NDs[3];

        //wyznaczamy globalne id wezlow w elemencie
        globalNodeID[0] = globalData.getnH() * i + j;
        globalNodeID[1] = globalData.getnH() * (i + 1) + j;
        globalNodeID[2] = globalData.getnH() * (i + 1) + (j + 1);
        globalNodeID[3] = globalData.getnH() * i + (j + 1);

        //wezly na powierzchniach
        POW[0] = new Surface(ND[3], ND[0]);
        POW[1] = new Surface(ND[0], ND[1]);
        POW[2] = new Surface(ND[1], ND[2]);
        POW[3] = new Surface(ND[2], ND[3]);

        numberOfSurface = 0;
        for (int l = 0; l < 4; l++) {
            if (POW[l].getNodes()[0].getStatus() == 1 && POW[l].getNodes()[1].getStatus() == 1) {
                numberOfSurface++;
            }
        }
        a_pow = new int[numberOfSurface];

        int counter = 0;
            for (int l = 0; l < 4; l++) {
                if (POW[l].getNodes()[0].getStatus() == 1 && POW[l].getNodes()[1].getStatus() == 1) {
                a_pow[counter++] = l;
            }
        }
    }


    public int getN_pow() {
        return numberOfSurface;
    }

    public int[] getA_pow() {
        return a_pow;
    }

    public Node[] getND() {
        return ND;
    }
}
