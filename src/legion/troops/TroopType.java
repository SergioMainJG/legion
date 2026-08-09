package legion.troops;

/**
 * Catalogue of the unit types recognised by the simulator.
 * Types marked as not implemented are already part of the model so
 * the architecture can host them without modifying existing code.
 */
public enum TroopType {

    COMMANDER("C", "Commander", true),
    MEDIC("M", "Medic", true),
    INFANTRY("I", "Infantry", true),
    TANK("T", "Tank", false),
    SNIPER("S", "Sniper", false);

    private final String symbol;
    private final String label;
    private final boolean implemented;

    TroopType(String symbol, String label, boolean implemented) {
        this.symbol = symbol;
        this.label = label;
        this.implemented = implemented;
    }

    /**
     * Returns the character drawn on the battlefield for this type.
     *
     * @return the rendering symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the readable name of the type.
     *
     * @return the label of the type
     */
    public String getLabel() {
        return label;
    }

    /**
     * Indicates whether the type has a concrete unit in this milestone.
     *
     * @return true when the type can be instantiated
     */
    public boolean isImplemented() {
        return implemented;
    }

    /**
     * Returns the types that can be deployed in this milestone.
     *
     * @return an array with the implemented types in deployment order
     */
    public static TroopType[] implementedValues() {
        return new TroopType[] { COMMANDER, MEDIC, INFANTRY };
    }
}
