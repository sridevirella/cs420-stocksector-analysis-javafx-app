package domain;

public enum SectorName {

    FINANCIAL("XLF", 4),
    MATERIALS("XLB",1),
    COMMUNICATION("XLC",2),
    ENERGY("XLE", 3),
    INDUSTRIAL("XLI", 5),
    TECHNOLOGY("XLK", 6),
    STAPLES("XLP", 7),
    REAL_ESTATE("XLRE", 8),
    UTILITIES("XLU", 9),
    HEALTH_CARE("XLV", 10),
    DISCRETIONARY("XLY",11);

    private final String sectorName;
    private final int order;

    SectorName( String sectorName, int order ) {
        this.sectorName = sectorName;
        this.order = order;
    }

    public String getSector() {
        return sectorName;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return sectorName;
    }
}
