package domain;

import util.MathUtil;

public abstract class Body {
    private final String designation;
    private String name;

    public Body(String designation, String name) {
        this.designation = designation;
        this.name = name;
    }

    public String getDesignation() { return designation; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public int hashCode() {
        final int prime = 997;
        int result = 1;
        result = prime * result + ((this.getDesignation() == null) ? 0 : this.getDesignation().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Body other = (Body) obj;
        if (this.getDesignation() == null) return other.getDesignation() == null;
        return this.getDesignation().equals(other.getDesignation());
    }


    public double calculateDistance(Body other) {
        return MathUtil.distance(this, other);
    }

    public abstract Coordinate getCoordinate();
}