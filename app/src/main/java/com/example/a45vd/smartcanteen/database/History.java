package com.example.a45vd.smartcanteen.database;

public class History {

    private static String TransactionDate;
    private static String WalletID;
    private static String ItemName;
    private static String Seller;
    private String Price;


    public History(String transactionDate, String WalletID, String itemName, String seller, String price) {
        TransactionDate = transactionDate;
        WalletID = WalletID;
        ItemName = itemName;
        //Seller = seller;
        Price = price;
    }

    public static String getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {

        TransactionDate = transactionDate;
    }

    public static String getWalletID() {

        return WalletID;
    }

    public void setWalletID(String WalletID) {

        WalletID = WalletID;
    }

    public static String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        this.ItemName = itemName;
    }

/*    public static String getSeller() {
        return Seller;
    }

    public void setSeller(String seller) {

        Seller = seller;
    }*/

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
