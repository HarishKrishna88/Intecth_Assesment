package com.intech.taxiapp.dto;


/**
 * This is POJO class used for the details of the Speed and its details.
 */
public class SpeedDetails {
    private String customerId;
    private double maxSpeed;

    public SpeedDetails(String customerId, double maxSpeed) {
        this.customerId = customerId;
        this.maxSpeed = maxSpeed;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

}
