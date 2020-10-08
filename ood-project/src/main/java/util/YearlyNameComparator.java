package util;

import domain.YearName;

import java.util.Comparator;

public class YearlyNameComparator implements Comparator<YearName> {

    public YearlyNameComparator() {}

    @Override
    public int compare(YearName o1, YearName o2) {

        return Integer.compare(o1.getYear(), o2.getYear());
    }
}
