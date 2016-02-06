package org.transandalus.backend.domain;


import javax.persistence.*;

import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * DB persisted image.
 * @author JoseMaria
 *
 */
@Entity
@Table(name = "image")
public class Image implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Lob
	@Column(name = "content")
	private byte[] content;
	    
	@Column(name = "content_type")
	private String ContentType;
	    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getContentType() {
		return ContentType;
	}

	public void setContentType(String contentType) {
		ContentType = contentType;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Image img = (Image) o;
        if(img.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, img.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
   
}
