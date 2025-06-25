public class FeeCalculator {
    private DiscountStrategy discountStrategy;

    public FeeCalculator(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public double calculateTotal(Event event, boolean catering, boolean transportation) {
        double base = event.getPrice();
        double add = 0;
        if (catering) add += 10.0;
        if (transportation) add += 5.0;
        double beforeDiscount = base + add;
        double discount = discountStrategy.getDiscount(event, beforeDiscount);
        return beforeDiscount - discount;
    }
}
