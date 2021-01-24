package com.askredrover.pinpoint;

public interface Message {

	public String getOriginationNumber();

	public String getDestinationNumber();

	public String getMessageKeyword();

	public String getMessageBody();

	public String getInboundMessageId();

	public String getPreviousPublishedMessageId();

	public String getStatus();

	public java.sql.Timestamp getAdded();

}
