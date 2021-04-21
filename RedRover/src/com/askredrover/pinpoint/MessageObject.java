package com.askredrover.pinpoint;

public class MessageObject implements Message {

	private String originationNumber, destinationNumber, messageKeyword, messageBody, inboundMessageId,
			previousPublishedMessageId, status = null;
	public java.sql.Timestamp added;

	public MessageObject(String originationNumber, String destinationNumber, String messageKeyword, String messageBody,
			String inboundMessageId, String previousPublishedMessageId, String status, java.sql.Timestamp added) {

		this.originationNumber = originationNumber;
		this.destinationNumber = destinationNumber;
		this.messageKeyword = messageKeyword;
		this.messageBody = messageBody;
		this.inboundMessageId = inboundMessageId;
		this.previousPublishedMessageId = previousPublishedMessageId;
		this.status = status;
		this.added = added;

	}

	public String getOriginationNumber() {
		return originationNumber;
	}

	public String getDestinationNumber() {
		return destinationNumber;
	}

	public String getMessageKeyword() {
		return messageKeyword;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public String getInboundMessageId() {
		return inboundMessageId;
	}

	public String getPreviousPublishedMessageId() {
		return previousPublishedMessageId;
	}

	public String getStatus() {
		return status;
	}

	public java.sql.Timestamp getAdded() {
		return added;
	}

}
