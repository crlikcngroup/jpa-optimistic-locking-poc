package dk.demo.jpaoptimisticlockingpoc.dto;

public class DrumsDto {
    private long id;
    private String manufacturer;
    private int decibels;
    private String color;
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
}
