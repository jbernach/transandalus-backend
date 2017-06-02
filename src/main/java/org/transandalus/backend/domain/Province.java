package org.transandalus.backend.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Province.
 */
@Entity
@Table(name = "province")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Province implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 16)
    @Column(name = "code", length = 16, nullable = false)
    private String code;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String name;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String title;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String description;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String guideUrl;

    @Column(name="track", insertable = false, updatable = false)
    private Long trackId;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "i18n_name", nullable = false)
    @JsonIgnore
    private I18n i18nName;

    @ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "i18n_title", nullable = true)
    @JsonIgnore
    private I18n i18nTitle;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "i18n_description", nullable = false)
    @JsonIgnore
    private I18n i18nDescription;

    @ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "i18n_guide_url", nullable = true)
    @JsonIgnore
    private I18n i18nGuideUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "track", nullable = false)
    private Track track;

    @Size(max = 1024)
    @Column(name="image_url", length = 1024)
    private String imageUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
    	return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuideUrl() {
        return guideUrl;
    }

    public void setGuideUrl(String guideUrl) {
        this.guideUrl = guideUrl;
    }

    public I18n getI18nName() {
		return i18nName;
	}

	public void setI18nName(I18n i18nName) {
		this.i18nName = i18nName;
	}

    public I18n getI18nTitle() {
        return i18nTitle;
    }

    public void setI18nTitle(I18n i18nTitle) {
        this.i18nTitle = i18nTitle;
    }

	public I18n getI18nDescription() {
        return i18nDescription;
    }

    public void setI18nDescription(I18n i18nDescription) {
        this.i18nDescription = i18nDescription;
    }

    public I18n getI18nGuideUrl() {
        return i18nGuideUrl;
    }

    public void setI18nGuideUrl(I18n i18nGuideUrl) {
        this.i18nGuideUrl = i18nGuideUrl;
    }

    public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Long getTrackId() {
		return trackId;
	}

	public void setTrackId(Long trackId) {
		this.trackId = trackId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Province province = (Province) o;
        if(province.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, province.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Province{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", name='" + name + "'" +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            '}';
    }

    public void resolveTraduction(){
    	this.setName(I18n.getTranslationText(this.getI18nName()));
        this.setTitle(I18n.getTranslationText(this.getI18nTitle()));
    	this.setDescription(I18n.getTranslationText(this.getI18nDescription()));
    	this.setGuideUrl(I18n.getTranslationText(this.getI18nGuideUrl()));
    }


}
