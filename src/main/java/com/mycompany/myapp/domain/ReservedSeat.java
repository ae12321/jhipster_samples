package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A ReservedSeat.
 */
@Entity
@Table(name = "reserved_seat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReservedSeat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "seat_name")
    private String seatName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReservedSeat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonName() {
        return this.personName;
    }

    public ReservedSeat personName(String personName) {
        this.setPersonName(personName);
        return this;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getSeatName() {
        return this.seatName;
    }

    public ReservedSeat seatName(String seatName) {
        this.setSeatName(seatName);
        return this;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservedSeat)) {
            return false;
        }
        return id != null && id.equals(((ReservedSeat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservedSeat{" +
            "id=" + getId() +
            ", personName='" + getPersonName() + "'" +
            ", seatName='" + getSeatName() + "'" +
            "}";
    }
}
