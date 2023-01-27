package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A SeatControl.
 */
@Entity
@Table(name = "seat_control")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SeatControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "seat_id", nullable = false, unique = true)
    private Long seatId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SeatControl id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSeatId() {
        return this.seatId;
    }

    public SeatControl seatId(Long seatId) {
        this.setSeatId(seatId);
        return this;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeatControl)) {
            return false;
        }
        return id != null && id.equals(((SeatControl) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SeatControl{" +
            "id=" + getId() +
            ", seatId=" + getSeatId() +
            "}";
    }
}
