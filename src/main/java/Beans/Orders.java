package Beans;

import java.util.Date;

public class Orders {
    private int orderId;
    private int customerId;
    private double totalAmount;
    private boolean paymentStatus;
    private String deliveryStatus;
    private Date createdAt;
    private Date updatedAt;
    private String customerName;
    private String shippingAddress;  // Thêm thuộc tính địa chỉ giao hàng

    // Constructor
    public Orders(int orderId, String customerName, double totalAmount, boolean paymentStatus, String deliveryStatus,
                  Date createdAt, Date updatedAt, String shippingAddress) {
        this.setOrderId(orderId);
        this.setCustomerName(customerName);
        this.setTotalAmount(totalAmount);
        this.setPaymentStatus(paymentStatus);
        this.setDeliveryStatus(deliveryStatus);
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
        this.setShippingAddress(shippingAddress);  // Thêm địa chỉ giao hàng vào constructor
    }
    public Orders(int orderId, String customerName, double totalAmount, boolean paymentStatus, String deliveryStatus,
            Date createdAt, Date updatedAt) {
        this.setOrderId(orderId);
        this.setCustomerName(customerName);
        this.setTotalAmount(totalAmount);
        this.setPaymentStatus(paymentStatus);
        this.setDeliveryStatus(deliveryStatus);
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    public Orders() {
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    // Getter và Setter cho địa chỉ giao hàng
    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
