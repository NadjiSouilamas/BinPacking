package application;

import java.util.Comparator;

public class SortNode implements Comparator<Node> {

    @Override
    public int compare(Node a, Node b) {
        return (a.getBins().get(a.nbBins() - 1).spaceleft() - b.getBins().get(b.nbBins() - 1).spaceleft());
    }
}
