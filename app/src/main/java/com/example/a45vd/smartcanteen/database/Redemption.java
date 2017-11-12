package com.example.a45vd.smartcanteen.database;

public class Redemption {
    private int RedemptionID;
    private int PointNeeded;
    private int AmountAvailable;
    private String RewardTitle;

    public Redemption(int redemptionID, int pointNeeded, int amountAvailable, String rewardTitle) {
        RedemptionID = redemptionID;
        PointNeeded = pointNeeded;
        AmountAvailable = amountAvailable;
        RewardTitle = rewardTitle;
    }

    public int getRewardID() {
        return RedemptionID;
    }

    public void setRewardID(int rewardID) {
        RedemptionID = rewardID;
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

    public String getRewardTitle() {
        return RewardTitle;
    }

    public void setRewardTitle(String rewardTitle) {
        RewardTitle = rewardTitle;
    }
}
