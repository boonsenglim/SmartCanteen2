package com.example.a45vd.smartcanteen.database;

public class Member {
    private int WalletID;
    private String StudName;
    //private String password;
    private double Balance;
   // private String email;
    private int LoyaltyPoint;

    public Member() {
    }

    public Member(String username, String password, double balance, String email, int rewardPoint) {
        this.WalletID = WalletID;
        //this.password = password;
        this.Balance = Balance;
        //this.email = email;
        this.LoyaltyPoint = LoyaltyPoint;
    }

    public String getStudName() {
        return StudName;
    }

    public void setUsername(String StudName) {
        this.StudName = StudName;
    }

/*    public String getPassword() {
        return password;
    }*/

//    public void setPassword(String password) {
//        this.password = password;
//    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        this.Balance = Balance;
    }

  /*  public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }*/

    public int getLoyaltyPoint() {
        return LoyaltyPoint;
    }

    public void setLoyaltyPoint(int rewardPoint) {
        this.LoyaltyPoint = rewardPoint;
    }

    public int getWalletID() {
        return WalletID;
    }
}
