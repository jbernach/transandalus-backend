package org.transandalus.backend.domain;


import javax.persistence.*;

import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * I18n text field.
 * @author JoseMaria
 *
 */
@Entity
@Table(name = "i18n")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class I18n implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_i18n")
    private Long id;

	@OneToMany(mappedBy = "i18n", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapKey(name = "idLocale")
	private Map<String, Translation> translations = new HashMap<String, Translation>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, Translation> getTranslations() {
		return translations;
	}

	public void setTranslations(Map<String, Translation> translations) {
		this.translations = translations;
	}
	
	
	public void setTranslationText(String text){
		String locale = LocaleContextHolder.getLocale().getLanguage();
		Translation t = getTranslations().get(locale);
		
		if(t == null){
			t = new Translation();
			t.setI18n(this);
			t.setIdLocale(locale);
			getTranslations().put(locale, t);
		}
		
		t.setTxContent(text);
	}
	
	/**
	 * Returns the translation text.
	 * @return
	 */
	public String getTranslationText(){
		String text = null;
		String locale = LocaleContextHolder.getLocale().getLanguage();
		
		Translation t = getTranslations().get(locale);
		
		if(t != null) text = t.getTxContent();
		
		return text;
	}
	
	public static String getTranslationText(I18n i18n){
		return (i18n == null)?null:i18n.getTranslationText();
	}
	
	public static I18n setTranslationText(I18n i18n, String text){
		I18n res = (i18n == null)?new  I18n():i18n;
		res.setTranslationText(text);
		
		return res;
	}
}
