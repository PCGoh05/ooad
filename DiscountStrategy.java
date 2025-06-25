public interface DiscountStrategy {
    double getDiscount(Event event, double amount);
}
