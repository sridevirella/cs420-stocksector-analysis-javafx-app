package domain;

public enum SectorName {

    MATERIALS("XLB"),
    COMMUNICATION("XLC"),
    ENERGY("XLE"),
    FINANCIAL("XLF"),
    INDUSTRIAL("XLI"),
    TECHNOLOGY("XLK"),
    STAPLES("XLP"),
    REAL_ESTATE("XLRE"),
    UTILITIES("XLU"),
    HEALTH_CARE("XLV"),
    DISCRETIONARY("XLY");

    private final String sectorName;

    SectorName( String sectorName ) {
        this.sectorName = sectorName;
    }

    public String getSector() {
        return sectorName;
    }

    @Override
    public String toString() {
        return sectorName;
    }
}
