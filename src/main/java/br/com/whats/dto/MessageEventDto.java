package br.com.whats.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.zenvia.api.sdk.ZonedDateTimeDeserializer;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MessageEventDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -795853747122347491L;

	private String id;
	
	@JsonDeserialize( using = ZonedDateTimeDeserializer.class )
	@JsonSerialize( using = ZonedDateTimeSerializer.class )
	@JsonFormat( pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]XXX" )
	private ZonedDateTime timestamp;
	
	private String subscriptionId;
	
	private MessageDto message;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public MessageDto getMessage() {
		return message;
	}

	public void setMessage(MessageDto message) {
		this.message = message;
	}
}
