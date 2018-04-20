package mes;

public class Solver
{


    public static double[] gaussElimination(int n, double[][] H_global, double[] P_global) {

        boolean r = false;
        double m, s, e;
        e = Math.pow(10, -12);
        double[] tabResult = new double[n];

        double[][] tabAB = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tabAB[j][i] = H_global[j][i];
            }
        }

        for (int i = 0; i < n; i++) {
            tabAB[i][n] = P_global[i];
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(tabAB[i][i]) < e) {
                    System.err.println("dzielnik rowny 0");
                    break;
                }

                m = -tabAB[j][i] / tabAB[i][i];
                for (int k = 0; k < n + 1; k++) {
                    tabAB[j][k] += m * tabAB[i][k];
                }
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            s = tabAB[i][n];
            for (int j = n - 1; j >= 0; j--) {
                s -= tabAB[i][j] * tabResult[j];
            }
            if (Math.abs(tabAB[i][i]) < e) {
                System.err.println("dzielnik rowny 0");
                break;
            }
            tabResult[i] = s / tabAB[i][i];
            r = true;
        }
        return tabResult;
    }
}