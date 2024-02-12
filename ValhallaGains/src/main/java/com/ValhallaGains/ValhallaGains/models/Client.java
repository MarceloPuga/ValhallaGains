package com.ValhallaGains.ValhallaGains.models;
import com.ValhallaGains.ValhallaGains.dtos.TrainingType;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Purchase> purchases = new HashSet<>();
    private String name, email,lastName, password;
    @OneToOne(targetEntity = Direction.class) //RELACION DIRECTA CAPO, PORQUE ES UNA RELACION UNO A UNO
    private Direction directions;
    private String role = "client";
    private String accessCode;
    private Trainingtype type=Trainingtype.NONE;

    public Client() {
    }

    public Client(String name, String email, String password, Direction direction, String lastName,Trainingtype type) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.directions = direction;
        this.type=type;

    }
    public String getName() {
        return name;
    }
    public Direction getDirections() {
        return directions;
    }

    public void setDirections(Direction directions) {
        this.directions = directions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Long getId() {
        return id;
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public Trainingtype getType() {
        return type;
    }

    public void setType(Trainingtype type) {
        this.type = type;
    }

    public void addPurchase(Purchase purchase){
        purchase.setClient(this);
        purchases.add(purchase);
    }


}
