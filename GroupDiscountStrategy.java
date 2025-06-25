public class GroupDiscountStrategy implements DiscountStrategy {
    @Override
    public double getDiscount(Event event, double amount) {
        // Example: 15% discount for group registration (demo logic)
        return amount * 0.15;
    }
}
