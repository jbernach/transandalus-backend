package org.transandalus.backend.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Sponsor.
 */
@Entity
@Table(name = "sponsor")
public class Sponsor implements Serializable {

    private static final long serialVersionUID = 43577L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "link")
    private String link;

    @Column(name = "image")
    private String image;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String text;

    @ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "i18n_text", nullable = true)
    @JsonIgnore
    private I18n i18nText;

    @Column(name = "from_date")
    private ZonedDateTime fromDate = null;

    @Column(name = "to_date")
    private ZonedDateTime toDate = null;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public ZonedDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getToDate() {
        return toDate;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sponsor sponsor = (Sponsor) o;
        if(sponsor.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sponsor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sponsor{" +
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
