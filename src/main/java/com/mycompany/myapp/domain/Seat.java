package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Seat.
 */
@Entity
@Table(name = "seat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "can_sit", nullable = false)
    private Boolean canSit;

    @NotNull
    @Column(name = "top", nullable = false)
    private String top;

    @NotNull
    @Column(name = "jhi_left", nullable = false)
    private String left;

    @ManyToOne(optional = false)
    @NotNull
    private SeatGroup seatGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Seat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Seat name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCanSit() {
        return this.canSit;
    }

    public Seat canSit(Boolean canSit) {
        this.setCanSit(canSit);
        return this;
    }

    public void setCanSit(Boolean canSit) {
        this.canSit = canSit;
    }

    public String getTop() {
        return this.top;
    }

    public Seat top(String top) {
        this.setTop(top);
        return this;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getLeft() {
        return this.left;
    }

    public Seat left(String left) {
        this.setLeft(left);
        return this;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public SeatGroup getSeatGroup() {
        return this.seatGroup;
    }

    public void setSeatGroup(SeatGroup seatGroup) {
        this.seatGroup = seatGroup;
    }

    public Seat seatGroup(SeatGroup seatGroup) {
        this.setSeatGroup(seatGroup);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seat)) {
            return false;
        }
        return id != null && id.equals(((Seat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Seat{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", canSit='" + getCanSit() + "'" +
            ", top='" + getTop() + "'" +
            ", left='" + getLeft() + "'" +
            "}";
    }
}
