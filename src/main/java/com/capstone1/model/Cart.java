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
    private long id;

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

    public void addCartItem(CartItem cartItem) {
        listItem.add(cartItem);
        cartItem.setCart(this);
    }

    public boolean addProduct(Product product, Cart cart) {
        boolean result = false;

        CartItem temp = checkProductExist(product.getId());

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
            if (listItem.get(i).getProduct().getId() == productID) {
                return listItem.get(i);
            }
        }

        return foundCartItem;

    }

    public void removeItem(long productId) {
        for (int i = 0; i < listItem.size(); i++) {
            if (listItem.get(i).getProduct().getId() == productId) {
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
            if (listItem.get(i).product.getId() == id) {
                listItem.remove(i);
            }
        }
    }

    public void setListItem(ArrayList<CartItem> listItem) {
        this.listItem = listItem;
    }

    public void updateQuan(int productId, int quant) {
        CartItem temp = checkProductExist(productId);
        temp.setQuantity(quant);

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long totalSize() {
        long count = 0;
        for (CartItem item : listItem) {
            count += item.getQuantity();
        }
        return count;
    }

}