package br.com.whats.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDto implements Serializable{

	private static final long serialVersionUID = -156574856062759606L;
	
	private String id;
	private String from;
	private String to;
	private List<ContentDto> contents;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public List<ContentDto> getContents() {
		return contents;
	}

	public void setContents(List<ContentDto> contents) {
		this.contents = contents;
	}
}
