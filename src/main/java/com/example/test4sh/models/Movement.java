package com.example.test4sh.models;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.sql.Date;

@Entity
@Table(name = "movements")
public
class Movement {

    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Integer id;
    @NonNull
    private String author;
    @NonNull
    private Date creationDateTime;
    @NonNull
    private String codeWarehouse;
    @NonNull
    private String labelWarehouse;
    @NonNull
    private String status;
    @Nullable
    private String typeDocDouane;
    @Nullable
    private String refDocDouane;
    @NonNull
    private Date movementTime;
    @NonNull
    private String ref;
    @NonNull
    private String refType;
    @NonNull
    private Integer quantity;
    @NonNull
    private Integer totalQuantity;
    @NonNull
    private Integer weight;
    @NonNull
    private Integer totalWeight;
    @NonNull
    private String description;

    public Movement(Integer id, String author, Date creationDateTime, String codeWarehouse, String labelWarehouse, String status, String typeDocDouane, String refDocDouane, Date movementTime, String ref, String refType, Integer quantity, Integer totalQuantity, Integer weight, Integer totalWeight, String description) {
        super();
        this.id = id;
        this.author = author;
        this.creationDateTime = creationDateTime;
        this.codeWarehouse = codeWarehouse;
        this.labelWarehouse = labelWarehouse;
        this.status = status;
        this.typeDocDouane = typeDocDouane;
        this.refDocDouane = refDocDouane;
        this.movementTime = movementTime;
        this.ref = ref;
        this.refType = refType;
        this.quantity = quantity;
        this.totalQuantity = totalQuantity;
        this.weight = weight;
        this.totalWeight = totalWeight;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }
    @NonNull
    public String getauthor() { return author; }
    @NonNull
    public Integer getQuantity() {
        return quantity;
    }
    @NonNull
    public Integer getTotalQuantity() {
        return totalQuantity;
    }
    @NonNull
    public Integer getTotalWeight() {
        return totalWeight;
    }
    @NonNull
    public Integer getWeight() {
        return weight;
    }
    @NonNull
    public String getCodeWarehouse() {
        return codeWarehouse;
    }
    @NonNull
    public Date getCreationDateTime() {
        return creationDateTime;
    }
    @NonNull
    public String getLabelWarehouse() {
        return labelWarehouse;
    }
    @NonNull
    public String getDescription() {
        return description;
    }
    @NonNull
    public Date getMovementTime() {
        return movementTime;
    }
    @NonNull
    public String getRef() {
        return ref;
    }
    @Nullable
    public String getRefDocDouane() {
        return refDocDouane;
    }
    @NonNull
    public String getRefType() {
        return refType;
    }
    @NonNull
    public String getStatus() {
        return status;
    }
    @Nullable
    public String getTypeDocDouane() {
        return typeDocDouane;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
