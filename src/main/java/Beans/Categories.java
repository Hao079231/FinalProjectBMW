package Beans;

public class Categories {
    private int categoryId; 
    private String cname;   

    public Categories() {
    }

    public Categories(int categoryId, String cname) {
        this.categoryId = categoryId;
        this.cname = cname;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

}