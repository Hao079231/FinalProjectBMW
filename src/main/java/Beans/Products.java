package Beans;



public class Products {
	private int productId;
    private String name;
    private String description;
    private byte[] image;
    private int price;
    private String size;
    private int stock;
    private int categoryId;
    private boolean status;
   

    // Constructor
    public Products(int productId, String name, String description, byte[] image, int price, String size,
            int stock, int categoryId, boolean status) {
	 this.productId = productId;
	 this.name = name;
	 this.description = description;
	 this.image = image;
	 this.price=price;
	 this.size = size;
	 this.stock = stock;
	 this.categoryId = categoryId;
	 this.status = status;
    }
    public Products() {}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
}