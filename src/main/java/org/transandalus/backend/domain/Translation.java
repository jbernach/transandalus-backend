package org.transandalus.backend.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;
import java.util.Objects;


/**
 * Content Translation.
 * @author JoseMaria
 *
 */
@Entity
@Table(name = "translation")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class Translation implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_i18n", nullable = false)
	private I18n i18n;

	@Column(name = "id_locale",nullable = false)
	private String idLocale; 
	
	@Column(name = "tx_content", columnDefinition="clob")
	private String txContent;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public I18n getI18n() {
		return i18n;
	}

	public void setI18n(I18n i18n) {
		this.i18n = i18n;
	}

	public String getIdLocale() {
		return idLocale;
	}

	public void setIdLocale(String idLocale) {
		this.idLocale = idLocale;
	}

	public String getTxContent() {
		return txContent;
	}

	public void setTxContent(String txContent) {
		this.txContent = txContent;
	}
	
	 @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Translation translation = (Translation) o;
        if(translation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, translation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
