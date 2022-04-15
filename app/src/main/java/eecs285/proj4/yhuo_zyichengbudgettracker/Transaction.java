package eecs285.proj4.yhuo_zyichengbudgettracker;

import java.io.Serializable;

class Transaction implements Serializable {
    private String category;
    private String name;
    private String time;
    private long cost;

    Transaction(String category, String name, String time, long cost) {
        this.category = category;
        this.name = name;
        this.time = time;
        this.cost = cost;
    }

    public void addCost(long newCost) {
        cost = cost + newCost;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getTime() {
        return time;
    }

    public long getCost() {
        return cost;
    }

    public String getCostString() {
        long costDollar = cost / 100;
        long costCent = cost % 100;
        if (costCent < 10) {
            return "$" + costDollar + ".0" + costCent;
        }
        return "$" + costDollar + "." + costCent;
    }
}
