package org.transandalus.backend.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Objects;

/**
 * A MenuItem.
 */
@Entity
@Table(name = "menu_item")
public class MenuItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private String text;
    
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "i18n_text", nullable = false)
    @JsonIgnore
    private I18n i18nText;
    
    @NotNull
    @Size(max = 2048)
    @Column(name = "url", length = 2048, nullable = false)
    private String url;
    
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public I18n getI18nText() {
		return i18nText;
	}

	public void setI18nText(I18n i18nText) {
		this.i18nText = i18nText;
	}

	public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuItem menuItem = (MenuItem) o;
        if(menuItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, menuItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MenuItem{" +
            "id=" + id +
            ", text='" + text + "'" +
            ", url='" + url + "'" +
            '}';
    }
    
    public void resolveTraduction(){
    	this.setText(I18n.getTranslationText(this.getI18nText()));
    }
}
