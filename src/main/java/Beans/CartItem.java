package Beans;

public class CartItem {
    private int productId;
    private int quantity;
    private Products product;

    // Constructor
    public CartItem(int productId, int quantity, Products product) {
        this.productId = productId;
        this.quantity = quantity;
        this.product = product;
    }

    // Getter và Setter
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public double getPrice() {
        return product != null ? product.getPrice() : 0.0;
    }

    // Tăng số lượng sản phẩm
    public void incrementQuantity() {
        this.quantity++;
    }

    // Tính tổng giá, giới hạn bởi tồn kho
    public double getTotal() {
        if (product == null || quantity < 1) {
            return 0.0;
        }
        // Giới hạn số lượng theo tồn kho
        int maxQuantity = Math.min(quantity, product.getStock());
        return maxQuantity * product.getPrice();
    }
}