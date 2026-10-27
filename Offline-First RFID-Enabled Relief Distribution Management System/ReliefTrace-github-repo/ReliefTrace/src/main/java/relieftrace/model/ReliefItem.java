package relieftrace.model;

public class ReliefItem {
    private final int itemId;
    private final String itemName;
    private final String unit;
    private int quantityOnHand;
    private final int lowStockThreshold;

    public ReliefItem(int id, String name, String unit, int qty, int lowThreshold) {
        this.itemId = id; this.itemName = name; this.unit = unit;
        this.quantityOnHand = qty; this.lowStockThreshold = lowThreshold;
    }
    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getUnit() { return unit; }
    public int getQuantityOnHand() { return quantityOnHand; }
    public int getLowStockThreshold() { return lowStockThreshold; }
    public boolean isLowStock() { return quantityOnHand <= lowStockThreshold; }

    public void increase(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive");
        quantityOnHand += qty;
    }
    public void decrease(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (qty > quantityOnHand) throw new IllegalStateException("Insufficient stock for " + itemName);
        quantityOnHand -= qty;
    }
}
