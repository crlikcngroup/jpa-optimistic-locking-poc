package dk.demo.jpaoptimisticlockingpoc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Objects;

@Entity
public class Drums {
    @Id
    @GeneratedValue
    private long id;

    private String manufacturer;

    private int decibels;

    private String color;

    @Version
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getDecibels() {
        return decibels;
    }

    public void setDecibels(int decibels) {
        this.decibels = decibels;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drums drums = (Drums) o;
        return id == drums.id &&
                decibels == drums.decibels &&
                Objects.equals(manufacturer, drums.manufacturer) &&
                Objects.equals(color, drums.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, manufacturer, decibels, color);
    }
}
