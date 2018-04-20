package mes;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;

public class Grid {

    Node[] nodes;
    Element[] elements;
    GlobalData globalData;
    private static Grid grid = null;

    public Grid () throws FileNotFoundException {
        globalData = new GlobalData();

        //tworzenie węzłów
        nodes = new Node[globalData.getNh()];
        int k = 0;
        for ( int i = 0; i < globalData.getnB(); i++ )
            for ( int j = 0; j < globalData.getnH(); j++ )
                nodes[k++] = new Node( i * globalData.getdB(), j * globalData.getdH(), globalData.getT_begin() ,globalData);

        //tworzenie elementów
        elements = new Element[globalData.getNe()];
        k = 0;

        for (int i = 0; i < globalData.getnB() - 1; i++) {
            for (int j = 0; j < globalData.getnH() - 1; j++) {
                /*if(i<((globalData.getnB() - 1)/3.0))
                {
                    elements[k++] = new Element(i, j, new Node[]{nodes[globalData.getnH() * i + j], nodes[globalData.getnH() * (i + 1) + j],
                            nodes[globalData.getnH() * (i + 1) + (j + 1)], nodes[globalData.getnH() * i + (j + 1)]},
                            globalData.getAlfa(),globalData.getC(),globalData.getK(),globalData.getRo());
                }
                else if (i < (2*(globalData.getnB() - 1)/3.0) && i >= ((globalData.getnB()-1)/3.0))
                {
                    elements[k++] = new Element(i, j, new Node[]{nodes[globalData.getnH() * i + j], nodes[globalData.getnH() * (i + 1) + j],
                            nodes[globalData.getnH() * (i + 1) + (j + 1)], nodes[globalData.getnH() * i + (j + 1)]},
                            globalData.getAlfaPow(),globalData.getcPow(),globalData.getkPow(),globalData.getRoPow());

                }
                else
                {
                    elements[k++] = new Element(i, j, new Node[]{nodes[globalData.getnH() * i + j], nodes[globalData.getnH() * (i + 1) + j],
                            nodes[globalData.getnH() * (i + 1) + (j + 1)], nodes[globalData.getnH() * i + (j + 1)]},
                            globalData.getAlfa(),globalData.getC(),globalData.getK(),globalData.getRo());
                }*/




                /*//2 warstwy szyby + 1 warstwa powietrza tej samej grubości
                if ((i < (9*(globalData.getnB() - 1)/10.0)) && (i >= (1*(globalData.getnB()-1)/10.0)))
                {
                    elements[k++] = new Element(i, j, new Node[]{nodes[globalData.getnH() * i + j], nodes[globalData.getnH() * (i + 1) + j],
                            nodes[globalData.getnH() * (i + 1) + (j + 1)], nodes[globalData.getnH() * i + (j + 1)]},
                            globalData.getAlfaPow(),globalData.getcPow(),globalData.getkPow(),globalData.getRoPow());

                }

                else
                {
                    elements[k++] = new Element(i, j, new Node[]{nodes[globalData.getnH() * i + j], nodes[globalData.getnH() * (i + 1) + j],
                            nodes[globalData.getnH() * (i + 1) + (j + 1)], nodes[globalData.getnH() * i + (j + 1)]},
                            globalData.getAlfa(),globalData.getC(),globalData.getK(),globalData.getRo());

                }*/

                // 3 warstwy szyby + 2 warstwy powietrza, 4mm + 14mm + 4 mm + 14 mm + 4 mm
                if ((i < (9*(globalData.getnB() - 1)/20.0)) && (i >= (2*(globalData.getnB()-1)/20.0)))
                {
                    elements[k++] = new Element(i, j, new Node[]{nodes[globalData.getnH() * i + j], nodes[globalData.getnH() * (i + 1) + j],
                            nodes[globalData.getnH() * (i + 1) + (j + 1)], nodes[globalData.getnH() * i + (j + 1)]},
                            globalData.getAlfaPow(),globalData.getcPow(),globalData.getkPow(),globalData.getRoPow());

                }
                else if((i < (18*(globalData.getnB() - 1)/20.0)) && (i >= (11*(globalData.getnB()-1)/20.0)))
                {
                    elements[k++] = new Element(i, j, new Node[]{nodes[globalData.getnH() * i + j], nodes[globalData.getnH() * (i + 1) + j],
                            nodes[globalData.getnH() * (i + 1) + (j + 1)], nodes[globalData.getnH() * i + (j + 1)]},
                            globalData.getAlfaPow(),globalData.getcPow(),globalData.getkPow(),globalData.getRoPow());

                }
                else
                {
                    elements[k++] = new Element(i, j, new Node[]{nodes[globalData.getnH() * i + j], nodes[globalData.getnH() * (i + 1) + j],
                            nodes[globalData.getnH() * (i + 1) + (j + 1)], nodes[globalData.getnH() * i + (j + 1)]},
                            globalData.getAlfa(),globalData.getC(),globalData.getK(),globalData.getRo());

                }


            }
        }


    }

    public static Grid getInstance() throws FileNotFoundException {
        if (grid == null) {
            grid = new Grid();
        }
        return grid;
    }


    public void showElementsProperties()
    {
        DecimalFormat dec = new DecimalFormat( "#0.0000" );
        System.out.println( "Wartosci danych \n" );
        int k = 0;
        for ( int i = 0; i < globalData.getnB()-1; i++ ) {
            for ( int j = 0; j < globalData.getnH()-1; j++ ) {
                System.out.print( "[" + elements[k].alfa  + "]" );
                k++;
            }
            System.out.println( "\n" );
        }

    }

    public void showEStatus()
    {
        DecimalFormat dec = new DecimalFormat( "#0.0000" );
        System.out.println( "Wartosci status \n" );
        int k = 0;
        for ( int i = 0; i < globalData.getnB(); i++ ) {
            for ( int j = 0; j < globalData.getnH(); j++ ) {
                System.out.print( "[" + nodes[k].status + "]" );
                k++;
            }
            System.out.println( "\n" );
        }

    }


    public void showElements () {
        System.out.println( "\n\nELEMENTY\n\n" );

        DecimalFormat dec = new DecimalFormat( "#0.0000" );

        Node[] n;

        int k=0;
        System.out.println("Element 1");
        n = elements[0].getND();
        for ( int j = 3; j >1; j-- ) {
                System.out.print(n[j].getStatus() + "   ");
            }
            System.out.println();
        for ( int j = 0; j < 2; j++ ) {
            System.out.print(n[j].getStatus() + "   ");
        }


        System.out.println();
        System.out.println("Element 13");
        n = elements[12].getND();
        for ( int j = 3; j >1; j-- ) {
            System.out.print(n[j].getStatus() + "   ");
        }
        System.out.println();
        for ( int j = 0; j < 2; j++ ) {
            System.out.print(n[j].getStatus() + "   ");
        }


        System.out.println();
        System.out.println("Element 16");
        n = elements[15].getND();
        for ( int j = 3; j >1; j-- ) {
            System.out.print(n[j].getStatus() + "   ");
        }
        System.out.println();
        for ( int j = 0; j < 2; j++ ) {
            System.out.print(n[j].getStatus() + "   ");
        }
/*
        n = elements[3].getND();
        for ( int j = 0; j < 4; j++ ) {
            System.out.println( dec.format( n[j].getX() ) + "\t" + dec.format( n[j].getY() ) + "\t" + n[j].getStatus() );
        }
        System.out.println();
*/
    }

    public void showNodes () {
        System.out.println( "\n\nWEZLY\n\n" );

        DecimalFormat dec = new DecimalFormat( "#0.0000" );
        System.out.println( "WSPOLRZEDNE\n" );
        int k = 0;
        for ( int i = 0; i < globalData.getnB(); i++ ) {
            for ( int j = 0; j < globalData.getnH(); j++ ) {
                System.out.print( "[" + dec.format( nodes[k].getX() ) + " | " + dec.format( nodes[k].getY() ) + "]\t\t" );
                k++;
            }
            System.out.println( "\n" );
        }
/*
        System.out.println( "\nSTATUSY\n" );
        k = 0;
        for ( int i = 0; i < globalData.getnB(); i++ ) {
            for ( int j = 0; j < globalData.getnH(); j++ ) {
                System.out.print( nodes[k].getStatus() + "\t" );
                k++;
            }
            System.out.println();
        }
        */
    }

}
