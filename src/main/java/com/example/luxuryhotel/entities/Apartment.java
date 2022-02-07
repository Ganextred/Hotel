package com.example.luxuryhotel.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Apartment {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private Integer price;
    private Integer beds;

    @Enumerated(EnumType.ORDINAL)
    private Clazz clazz;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "apartment_image", joinColumns = @JoinColumn(name = "apartment_id"))
    private List<String> images;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ApartmentStatus> apartmentStatuses;

    public List<ApartmentStatus> getApartmentStatuses() {
        return apartmentStatuses;
    }

    public void setApartmentStatuses(List<ApartmentStatus> apartmentStatuses) {
        this.apartmentStatuses = apartmentStatuses;
    }

    public Apartment() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}
