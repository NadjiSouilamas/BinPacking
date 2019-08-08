package application;

import java.util.Comparator;

public class SortBin implements Comparator<Bin> {

    @Override
    public int compare(Bin binA, Bin binB) {
        return binA.getFilled() - binB.getFilled();
    }
}
