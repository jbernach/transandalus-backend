package org.transandalus.backend.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Volunteer.
 */
@Entity
@Table(name = "volunteer")
public class Volunteer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String text;

    @Column(name = "image")
    private String image;

    @ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "i18n_text", nullable = true)
    @JsonIgnore
    private I18n i18nText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public I18n getI18nText() {
        return i18nText;
    }

    public void setI18nText(I18n i18nText) {
        this.i18nText = i18nText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Volunteer volunteer = (Volunteer) o;
        if(volunteer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, volunteer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Volunteer{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", text='" + text + "'" +
            ", image='" + image + "'" +
            '}';
    }

    public void resolveTraduction(){
        this.setText(I18n.getTranslationText(this.getI18nText()));
    }
}
