package cz.muni.fi.pv168.project.ui.filter;

import cz.muni.fi.pv168.project.model.TransactionType;

import java.util.List;

public enum TransactionTypeOption {
    BOTH("both", TransactionType.ONE_TIME, TransactionType.PERIODIC),
    ONE_TIME("oneTime", TransactionType.ONE_TIME),
    PERIODIC("periodic", TransactionType.PERIODIC);

    private final String name;
    private final List<TransactionType> allowedTypes;

    TransactionTypeOption(String name, TransactionType... allowedTypes) {
        this.name = name;
        this.allowedTypes = List.of(allowedTypes);
    }

    public String getName() {
        return name;
    }

    public boolean allows(TransactionType type) {
        return allowedTypes.contains(type);
    }
}
