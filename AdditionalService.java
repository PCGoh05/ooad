public class AdditionalService {
    private String name;
    private double price;
    private boolean selected;
    
    public AdditionalService(String name, double price) {
        this.name = name;
        this.price = price;
        this.selected = false;
    }
    
    public String getName() { return name; }
    public double getPrice() { return price; }
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
    
    @Override
    public String toString() {
        return name + " (+$" + String.format("%.2f", price) + ")";
    }
}
