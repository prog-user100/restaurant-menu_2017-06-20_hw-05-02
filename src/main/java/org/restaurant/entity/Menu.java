package org.restaurant.entity;

import javax.persistence.*;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;

@Entity(name="menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String dish;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer weight;

    @Column(nullable = false)
    private Boolean discountAvailable;

    public Menu() {
    }

    public Menu(String dish, Double price, Integer weight, Boolean discountAvailable) {
        this.dish = dish;
        this.price = price;
        this.weight = weight;
        this.discountAvailable = discountAvailable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Boolean getDiscountAvailable() {
        return discountAvailable;
    }

    public void setDiscountAvailable(Boolean discountAvailable) {
        this.discountAvailable = discountAvailable;
    }

    @Override
    public String toString() {
        return "Menu{" + "id=" + id + ", dish='" + dish + '\'' + ", price=" + price + ", weight=" + weight + ", discountAvailable=" + discountAvailable + '}';
    }
}
