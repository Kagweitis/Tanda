package com.tanda.payments.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class B2CRequest {

	@JsonProperty("occasion")
	private String occasion;

	@JsonProperty("QueueTimeOutURL")
	private String queueTimeOutURL;

	@JsonProperty("OriginatorConversationID")
	private String originatorConversationID;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("Amount")
	private int amount;

	@JsonProperty("InitiatorName")
	private String initiatorName;

	@JsonProperty("SecurityCredential")
	private String securityCredential;

	@JsonProperty("CommandID")
	private String commandID;

	@JsonProperty("PartyA")
	private int partyA;

	@JsonProperty("PartyB")
	private long partyB;

	@JsonProperty("ResultURL")
	private String resultURL;


}