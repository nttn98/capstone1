package com.capstone1.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonIgnore
    List<CartItem> listItem;

    public Cart() {
        this.listItem = new ArrayList<>();
    }

    public Cart(User user) {
        this.user = user;
        this.listItem = new ArrayList<>();
    }

    public boolean addProduct(Product product, Cart cart) {
        boolean result = false;

        CartItem temp = checkProductExist(product.getProductId());

        if (temp == null) {
            CartItem newItem = new CartItem(cart, product, 1);
            this.listItem.add(newItem);
        } else {
            temp.changeQuantity(1);
        }

        return result;
    }

    public CartItem checkProductExist(long productID) {
        CartItem foundCartItem = null;

        for (int i = 0; i < listItem.size(); i++) {
            if (listItem.get(i).getProduct().getProductId() == productID) {
                return listItem.get(i);
            }
        }

        return foundCartItem;

    }

    public void removeItem(long productID) {
        for (int i = 0; i < listItem.size(); i++) {
            if (listItem.get(i).getProduct().getProductId() == productID) {
                this.listItem.remove(i);
            }
        }
    }

    public int getTotal() {
        float total = 0;
        for (int i = 0; i < listItem.size(); i++) {
            total = total + listItem.get(i).getSubtotal();
        }
        return (int) total;
    }

    public List<CartItem> getListItem() {
        return listItem;
    }

    public void deleteByProductId(long id) {
        for (int i = 0; i < listItem.size(); i++) {
            if (listItem.get(i).product.getProductId() == id) {
                listItem.remove(i);
            }
        }
    }

    public void setListItem(ArrayList<CartItem> listItem) {
        this.listItem = listItem;
    }

    public void updateQuan(int productID, int quant) {
        CartItem temp = checkProductExist(productID);
        temp.setQuantity(quant);

    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public long totalSize() {
        long count = 0;
        for (CartItem item : listItem) {
            count += item.getQuantity();
        }
        return count;
    }

}