package mes;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {


        GlobalData globalData = GlobalData.getInstance();
        Grid grid = Grid.getInstance();
        double[] t;
        double temp_mid = 0;


        grid.showNodes();
        System.out.println();
        if( globalData.getnH() == 5 && globalData.getnB() == 5)
        grid.showElements();


        System.out.println();

        grid.showElementsProperties();

        System.out.println();

        grid.showEStatus();

        System.out.println();


        for (int itau = 0; itau < globalData.getTau(); itau++) {
            System.out.println("-------- Iteration "+itau+" --------");


            globalData.compute();
            t = Solver.gaussElimination(globalData.getNh(), globalData.getH_global(), globalData.getP_global());

            /*System.out.print("Macierz HC:  \n");
            for (int i = 0; i < globalData.getNh(); i++) {
                System.out.print("| ");
                for (int j = 0; j < globalData.getNh(); j++) {
                    System.out.printf("%.5f\t", globalData.getH_global()[i][j]);
                }
                System.out.print(" |\n");
            }
            System.out.println("\n");
            System.out.print("Macierz P:  \n|");
            for (int i = 0; i < globalData.getNh(); i++)
            {
                System.out.printf("%.4f\t", globalData.getP_global()[i]);
            }*/
            System.out.print("| \n \n");
            for (int i = 0; i < globalData.getNh(); i++) {
                grid.nodes[i].setT(t[i]);
            }


            for (int i = 40; i < 180; i++)
            {
                temp_mid += grid.nodes[i].getT();
            }
            temp_mid = temp_mid/140;

            for (int i = 40; i < 180; i++)
            {
                grid.nodes[i].setT(temp_mid);
            }





            for (int i = 220; i < 360; i++)
            {
                temp_mid += grid.nodes[i].getT();
            }
            temp_mid = temp_mid/140;

            for (int i = 220; i < 360; i++)
            {
                grid.nodes[i].setT(temp_mid);
            }


            int count = 0;
            System.out.println("Temperatura:");
            for (int i = 0; i < globalData.getnB(); i++) {
                for (int j = 0; j < globalData.getnH(); j++) {
                    System.out.printf("%.14f\t", grid.nodes[count++].getT());
                }
                System.out.println("");
            }
            System.out.println("\n\n");





        }






    }
}
