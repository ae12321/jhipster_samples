package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Type;

/**
 * A Representative.
 */
@Entity
@Table(name = "representative")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Representative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "mail", nullable = false, unique = true)
    private String mail;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "face")
    private String face;

    @Column(name = "face_id")
    private String faceId;

    @NotNull
    @Column(name = "hash", nullable = false, unique = true)
    private String hash;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Representative id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Representative name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return this.mail;
    }

    public Representative mail(String mail) {
        this.setMail(mail);
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFace() {
        return this.face;
    }

    public Representative face(String face) {
        this.setFace(face);
        return this;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getFaceId() {
        return this.faceId;
    }

    public Representative faceId(String faceId) {
        this.setFaceId(faceId);
        return this;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getHash() {
        return this.hash;
    }

    public Representative hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Representative)) {
            return false;
        }
        return id != null && id.equals(((Representative) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Representative{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mail='" + getMail() + "'" +
            ", face='" + getFace() + "'" +
            ", faceId='" + getFaceId() + "'" +
            ", hash='" + getHash() + "'" +
            "}";
    }
}
