package cz.muni.fi.pv168.project.model;

public enum TransactionDirection {
    IN("in"),
    OUT("out");

    private final String directionName;

    TransactionDirection(String value) {
        this.directionName = value;
    }

    public String getDirectionName() {
        return directionName;
    }
}
