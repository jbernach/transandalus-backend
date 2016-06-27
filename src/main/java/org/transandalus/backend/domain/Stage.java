package org.transandalus.backend.domain;


import javax.persistence.*;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.transandalus.backend.domain.enumeration.Difficulty;
import org.transandalus.backend.domain.enumeration.StageType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A Stage.
 */
@Entity
@Table(name = "stage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Stage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "stage_type")
    private StageType stageType = StageType.REGULAR;
    
    @Size(max = 1024)
    @Column(name="start_place", length = 1024, columnDefinition="clob")
    private  String startPlace;
    
    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String description;
    
    @Column(name="track", insertable = false, updatable = false)
    private Long trackId;
    
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "i18n_name", nullable = false)
    @JsonIgnore
    private I18n i18nName;
    
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "i18n_description", nullable = false)
    @JsonIgnore
    private I18n i18nDescription;
    
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "track", nullable = false)
    private Track track;
 
    @Size(max = 1024)
    @Column(name="image_url", length = 1024)
    private  String imageUrl;
    
    @Column(name = "distance_total")
    private Float distanceTotal;
    
    @Column(name = "distance_road")
    private Float distanceRoad;
    
    @Column(name = "estimated_time")
    private Integer estimatedTime;
    
    @Column(name = "elevation")
    private Integer elevation;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_phys")
    private Difficulty difficultyPhys;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_tech")
    private Difficulty difficultyTech;
    
    @Size(max = 1024)
    @Column(name="gallery_url", length = 1024)
    private String galleryURL;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "province_id")
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_stage_id")
    @JsonIgnoreProperties({"nextStage", "nextAltStage", "prevStage", "prevAltStage"})
    private Stage nextStage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_alt_stage_id")
    @JsonIgnoreProperties({"nextStage", "nextAltStage", "prevStage", "prevAltStage"})
    private Stage nextAltStage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_stage_id")
    @JsonIgnoreProperties({"nextStage", "nextAltStage", "prevStage", "prevAltStage"})
    private Stage prevStage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_alt_stage_id")
    @JsonIgnoreProperties({"nextStage", "nextAltStage", "prevStage", "prevAltStage"})
    private Stage prevAltStage;

    
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

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public I18n getI18nName() {
		return i18nName;
	}

	public void setI18nName(I18n i18nName) {
		this.i18nName = i18nName;
	}

	public I18n getI18nDescription() {
		return i18nDescription;
	}

	public void setI18nDescription(I18n i18nDescription) {
		this.i18nDescription = i18nDescription;
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

	public Float getDistanceTotal() {
        return distanceTotal;
    }
    
    public void setDistanceTotal(Float distanceTotal) {
        this.distanceTotal = distanceTotal;
    }

    public Float getDistanceRoad() {
        return distanceRoad;
    }
    
    public void setDistanceRoad(Float distanceRoad) {
        this.distanceRoad = distanceRoad;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }
    
    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getElevation() {
        return elevation;
    }
    
    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    public Difficulty getDifficultyPhys() {
        return difficultyPhys;
    }
    
    public void setDifficultyPhys(Difficulty difficultyPhys) {
        this.difficultyPhys = difficultyPhys;
    }

    public Difficulty getDifficultyTech() {
        return difficultyTech;
    }
    
    public void setDifficultyTech(Difficulty difficultyTech) {
        this.difficultyTech = difficultyTech;
    }

    public String getGalleryURL() {
        return galleryURL;
    }
    
    public void setGalleryURL(String galleryURL) {
        this.galleryURL = galleryURL;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Stage getNextStage() {
        return nextStage;
    }

    public void setNextStage(Stage stage) {
        this.nextStage = stage;
    }

    public Stage getNextAltStage() {
        return nextAltStage;
    }

    public void setNextAltStage(Stage stage) {
        this.nextAltStage = stage;
    }

    public Stage getPrevStage() {
        return prevStage;
    }

    public void setPrevStage(Stage stage) {
        this.prevStage = stage;
    }

    public Stage getPrevAltStage() {
        return prevAltStage;
    }

    public void setPrevAltStage(Stage stage) {
        this.prevAltStage = stage;
    }

    public StageType getStageType() {
		return stageType;
	}

	public void setStageType(StageType stageType) {
		this.stageType = stageType;
	}

	public String getStartPlace() {
		return startPlace;
	}

	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
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
        Stage stage = (Stage) o;
        if(stage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, stage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Stage{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", distanceTotal='" + distanceTotal + "'" +
            ", distanceRoad='" + distanceRoad + "'" +
            ", estimatedTime='" + estimatedTime + "'" +
            ", elevation='" + elevation + "'" +
            ", difficultyPhys='" + difficultyPhys + "'" +
            ", difficultyTech='" + difficultyTech + "'" +
            ", galleryURL='" + galleryURL + "'" +
            '}';
    }
    
    public void resolveTraduction(){
    	this.setName(I18n.getTranslationText(this.getI18nName()));
    	this.setDescription(I18n.getTranslationText(this.getI18nDescription()));
    }
}
