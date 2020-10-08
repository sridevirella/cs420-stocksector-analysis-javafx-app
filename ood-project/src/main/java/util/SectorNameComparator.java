package util;

import domain.SectorName;

import java.util.Comparator;

public class SectorNameComparator implements Comparator<SectorName> {

    public SectorNameComparator() {}

    @Override
    public int compare(SectorName o1, SectorName o2) {
        return o1.getOrder() - o2.getOrder();
    }
}
