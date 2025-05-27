package Beans;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private int cartID;
    private int userId;
    private int productId;
    private int quantity;
    private String feature;
    private Products product;

    private Map<Integer, CartItem> items;

    public Cart() {
        items = new HashMap<>();
    }

    // Constructor khi có đầy đủ thông tin
    public Cart(int cartID, int userId, int productId, int quantity, String feature) {
        this.cartID = cartID;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.feature = feature;
    }

    // Constructor khi có thông tin đầy đủ và đối tượng product
    public Cart(int cartID, int userId, int productId, int quantity, String feature, Products product) {
        this.cartID = cartID;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.feature = feature;
        this.product = product;
    }

    // Getter và Setter
    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    // Lấy giá từ Product
    public double getPrice() {
        return product != null ? product.getPrice() : 0.0;
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addItem(CartItem item) {
        // Kiểm tra tồn kho
        int stock = item.getProduct() != null ? item.getProduct().getStock() : 0;
        int newQuantity = item.getQuantity();

        if (items.containsKey(item.getProductId())) {
            newQuantity = items.get(item.getProductId()).getQuantity() + item.getQuantity();
        }

        if (newQuantity <= 0 || newQuantity > stock) {
            throw new IllegalArgumentException("Số lượng không hợp lệ hoặc vượt quá tồn kho cho sản phẩm ID: " + item.getProductId());
        }

        if (items.containsKey(item.getProductId())) {
            items.get(item.getProductId()).setQuantity(newQuantity);
        } else {
            items.put(item.getProductId(), item);
        }
        System.out.println("Giỏ hàng: " + items);
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeItem(int productId) {
        items.remove(productId);
    }

    // Chỉnh sửa số lượng sản phẩm
    public void updateItemQuantity(int productId, int quantity) {
        CartItem item = items.get(productId);
        if (item != null) {
            int stock = item.getProduct() != null ? item.getProduct().getStock() : 0;
            if (quantity <= 0 || quantity > stock) {
                throw new IllegalArgumentException("Số lượng không hợp lệ hoặc vượt quá tồn kho cho sản phẩm ID: " + productId);
            }
            item.setQuantity(quantity);
        }
    }

    // Lấy danh sách các sản phẩm trong giỏ hàng
    public Map<Integer, CartItem> getItems() {
        return items;
    }

    // Tính tổng giá trị giỏ hàng
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items.values()) {
            total += item.getTotal(); // Sử dụng getTotal từ CartItem để tôn trọng giới hạn tồn kho
        }
        return total;
    }

    // Kiểm tra xem giỏ hàng có rỗng không
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    // Xóa các mục không hợp lệ trong giỏ hàng
    public void cleanCart() {
        items.entrySet().removeIf(entry -> {
            CartItem item = entry.getValue();
            int stock = item.getProduct() != null ? item.getProduct().getStock() : 0;
            return item.getQuantity() < 1 || item.getQuantity() > stock;
        });
    }
}