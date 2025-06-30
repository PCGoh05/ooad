// Strategy interface for fee calculation
public interface FeeCalculationStrategy {
    double calculateDiscount(double baseAmount, int numberOfRegistrations, boolean isEarlyBird);
    String getDiscountDescription();
}

// Strategy for no discount
class NoDiscountStrategy implements FeeCalculationStrategy {
    @Override
    public double calculateDiscount(double baseAmount, int numberOfRegistrations, boolean isEarlyBird) {
        return 0.0;
    }
    
    @Override
    public String getDiscountDescription() {
        return "No discount applied";
    }
}

// Strategy for early bird discount
class EarlyBirdDiscountStrategy implements FeeCalculationStrategy {
    private double discountPercentage = 0.15; // 15% discount
    
    @Override
    public double calculateDiscount(double baseAmount, int numberOfRegistrations, boolean isEarlyBird) {
        if (isEarlyBird) {
            return baseAmount * discountPercentage;
        }
        return 0.0;
    }
    
    @Override
    public String getDiscountDescription() {
        return "Early Bird Discount (15%)";
    }
}

// Strategy for group discount
class GroupDiscountStrategy implements FeeCalculationStrategy {
    private double discountPercentage = 0.10; // 10% discount for groups
    private int minimumGroupSize = 5;
    
    @Override
    public double calculateDiscount(double baseAmount, int numberOfRegistrations, boolean isEarlyBird) {
        if (numberOfRegistrations >= minimumGroupSize) {
            return baseAmount * discountPercentage;
        }
        return 0.0;
    }
    
    @Override
    public String getDiscountDescription() {
        return "Group Discount (10% for 5+ registrations)";
    }
}

// Strategy for combined discounts
class CombinedDiscountStrategy implements FeeCalculationStrategy {
    private EarlyBirdDiscountStrategy earlyBirdStrategy = new EarlyBirdDiscountStrategy();
    private GroupDiscountStrategy groupStrategy = new GroupDiscountStrategy();
    
    @Override
    public double calculateDiscount(double baseAmount, int numberOfRegistrations, boolean isEarlyBird) {
        double earlyBirdDiscount = earlyBirdStrategy.calculateDiscount(baseAmount, numberOfRegistrations, isEarlyBird);
        double groupDiscount = groupStrategy.calculateDiscount(baseAmount, numberOfRegistrations, isEarlyBird);
        
        // Apply the higher discount, not both
        return Math.max(earlyBirdDiscount, groupDiscount);
    }
    
    @Override
    public String getDiscountDescription() {
        return "Best Available Discount (Early Bird 15% or Group 10%)";
    }
}
