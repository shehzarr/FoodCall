package com.example.foodcall.Menu;

public class menu_class {

    String itemName;
    String price;
    Boolean isSelected;
    String item_id;

    public menu_class(){

    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public menu_class(String itemName, String price, Boolean isSelected) {
        this.itemName = itemName;
        this.price = price;
        this.isSelected = isSelected;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
