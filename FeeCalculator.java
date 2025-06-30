import java.util.List;

public class FeeCalculator {
    private FeeCalculationStrategy discountStrategy;
    
    public FeeCalculator(FeeCalculationStrategy strategy) {
        this.discountStrategy = strategy;
    }
    
    public void setDiscountStrategy(FeeCalculationStrategy strategy) {
        this.discountStrategy = strategy;
    }
    
    public FeeBreakdown calculateFee(double baseFee, int numberOfRegistrations, 
                                   List<AdditionalService> additionalServices, 
                                   boolean isEarlyBird) {
        
        // Calculate base amount
        double baseAmount = baseFee * numberOfRegistrations;
        
        // Calculate additional services cost
        double servicesTotal = 0.0;
        StringBuilder servicesDetails = new StringBuilder();
        
        for (AdditionalService service : additionalServices) {
            if (service.isSelected()) {
                double serviceCost = service.getPrice() * numberOfRegistrations;
                servicesTotal += serviceCost;
                servicesDetails.append("• ").append(service.getName())
                              .append(": $").append(String.format("%.2f", service.getPrice()))
                              .append(" × ").append(numberOfRegistrations)
                              .append(" = $").append(String.format("%.2f", serviceCost))
                              .append("\n");
            }
        }
        
        // Calculate total before discount
        double totalBeforeDiscount = baseAmount + servicesTotal;
        
        // Calculate discount
        double discountAmount = discountStrategy.calculateDiscount(totalBeforeDiscount, numberOfRegistrations, isEarlyBird);
        
        // Calculate net payable amount
        double netAmount = totalBeforeDiscount - discountAmount;
        
        return new FeeBreakdown(baseAmount, servicesTotal, servicesDetails.toString(),
                               totalBeforeDiscount, discountAmount, netAmount,
                               discountStrategy.getDiscountDescription(), numberOfRegistrations);
    }
}

// Class to hold fee breakdown details
class FeeBreakdown {
    private double baseAmount;
    private double servicesTotal;
    private String servicesDetails;
    private double totalBeforeDiscount;
    private double discountAmount;
    private double netAmount;
    private String discountDescription;
    private int numberOfRegistrations;
    
    public FeeBreakdown(double baseAmount, double servicesTotal, String servicesDetails,
                       double totalBeforeDiscount, double discountAmount, double netAmount,
                       String discountDescription, int numberOfRegistrations) {
        this.baseAmount = baseAmount;
        this.servicesTotal = servicesTotal;
        this.servicesDetails = servicesDetails;
        this.totalBeforeDiscount = totalBeforeDiscount;
        this.discountAmount = discountAmount;
        this.netAmount = netAmount;
        this.discountDescription = discountDescription;
        this.numberOfRegistrations = numberOfRegistrations;
    }
    
    // Getters
    public double getBaseAmount() { return baseAmount; }
    public double getServicesTotal() { return servicesTotal; }
    public String getServicesDetails() { return servicesDetails; }
    public double getTotalBeforeDiscount() { return totalBeforeDiscount; }
    public double getDiscountAmount() { return discountAmount; }
    public double getNetAmount() { return netAmount; }
    public String getDiscountDescription() { return discountDescription; }
    public int getNumberOfRegistrations() { return numberOfRegistrations; }
    
    public String getDetailedBill() {
        StringBuilder bill = new StringBuilder();
        bill.append("=== FEE BREAKDOWN ===\n\n");
        bill.append("Base Registration Fee:\n");
        bill.append("• $").append(String.format("%.2f", baseAmount / numberOfRegistrations))
            .append(" × ").append(numberOfRegistrations)
            .append(" = $").append(String.format("%.2f", baseAmount)).append("\n\n");
        
        if (servicesTotal > 0) {
            bill.append("Additional Services:\n");
            bill.append(servicesDetails);
            bill.append("Services Subtotal: $").append(String.format("%.2f", servicesTotal)).append("\n\n");
        }
        
        bill.append("Total Before Discount: $").append(String.format("%.2f", totalBeforeDiscount)).append("\n");
        
        if (discountAmount > 0) {
            bill.append("Discount Applied: ").append(discountDescription).append("\n");
            bill.append("Discount Amount: -$").append(String.format("%.2f", discountAmount)).append("\n");
        } else {
            bill.append("No Discount Applied\n");
        }
        
        bill.append("\n");
        bill.append("NET PAYABLE AMOUNT: $").append(String.format("%.2f", netAmount));
        
        return bill.toString();
    }
}
