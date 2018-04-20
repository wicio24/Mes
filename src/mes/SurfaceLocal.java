package mes;

public class SurfaceLocal
{

    public NodeLocal[] ND;

    public double N[][];

    public SurfaceLocal(NodeLocal node1, NodeLocal node2) {
        ND = new NodeLocal[2];

        ND[0] = node1;
        ND[1] = node2;

        N = new double[2][4];
    }
}