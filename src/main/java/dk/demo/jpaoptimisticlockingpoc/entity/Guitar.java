package dk.demo.jpaoptimisticlockingpoc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Objects;

@Entity
public class Guitar {
    @Id
    @GeneratedValue
    private long id;

    private String color;

    private int year;

    private String type;

    @Version
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        Guitar guitar = (Guitar) o;
        return id == guitar.id &&
                year == guitar.year &&
                Objects.equals(color, guitar.color) &&
                Objects.equals(type, guitar.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color, year, type);
    }
}
