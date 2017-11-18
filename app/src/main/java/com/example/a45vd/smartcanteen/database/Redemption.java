package com.example.a45vd.smartcanteen.database;

public class Redemption {
   private int RedemptionID;
    private int PointNeeded;
    private int AmountAvailable;
    private String ProductName;

    public Redemption(int redemptionID, int pointNeeded, int amountAvailable, String productName) {
        RedemptionID = redemptionID;
        PointNeeded = pointNeeded;
        AmountAvailable = amountAvailable;
        ProductName = productName;
    }

   public int getRewardID() {
        return RedemptionID;
    }

    public void setRewardID(int rewardID) {
        RedemptionID = rewardID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getPointNeeded() {
        return PointNeeded;
    }

    public void setPointNeeded(int pointNeeded) {
        this.PointNeeded = pointNeeded;
    }

    public int getAmountAvailable() {
        return AmountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
        AmountAvailable = amountAvailable;
    }


}
