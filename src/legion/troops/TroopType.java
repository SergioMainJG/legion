package legion.troops;

/**
 * Catalogue of the unit types recognised by the simulator.
 * The declaration order is the deployment order consumed by the
 * parameter {@code u}.
 */
public enum TroopType {

    COMMANDER("C", "Commander"),
    MEDIC("M", "Medic"),
    TANK("T", "Tank"),
    SNIPER("S", "Sniper"),
    INFANTRY("I", "Infantry"),
    ENGINEER("E", "Engineer"),
    ARTILLERY("A", "Artillery"),
    ANTI_AIRCRAFT("R", "AntiAircraft");

    private final String symbol;
    private final String label;

    TroopType(String symbol, String label) {
        this.symbol = symbol;
        this.label = label;
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
     * Returns the types in the order the parameter {@code u} lists them.
     *
     * @return every type in deployment order
     */
    public static TroopType[] deploymentOrder() {
        return values();
    }
}
