/****************************************************** 
 *  Copyright 2018 IBM Corporation 
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at 
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *  See the License for the specific language governing permissions and 
 *  limitations under the License.
 */ 
package org.app.blockchain.hf.chaincode.invocation;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.Security;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;

/**
 * 
 * @author Balaji Kadambi
 *
 */

public class QueryChaincode {

	private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
	private static final String EXPECTED_EVENT_NAME = "event";

	public static void main(String args[]) {
		{
			Security.addProvider(new BouncyCastleProvider());
		}
		try {
            org.app.blockchain.hf.util.Util.cleanUp();
			String caUrl = org.app.blockchain.hf.config.Config.CA_ORG1_URL;
			org.app.blockchain.hf.client.CAClient caClient = new org.app.blockchain.hf.client.CAClient(caUrl, null);
			// Enroll Admin to Org1MSP
			org.app.blockchain.hf.user.UserContext adminUserContext = new org.app.blockchain.hf.user.UserContext();
			adminUserContext.setName(org.app.blockchain.hf.config.Config.ADMIN);
			adminUserContext.setAffiliation(org.app.blockchain.hf.config.Config.ORG1);
			adminUserContext.setMspId(org.app.blockchain.hf.config.Config.ORG1_MSP);
			caClient.setAdminUserContext(adminUserContext);
			adminUserContext = caClient.enrollAdminUser(org.app.blockchain.hf.config.Config.ADMIN, org.app.blockchain.hf.config.Config.ADMIN_PASSWORD);
			
			org.app.blockchain.hf.client.FabricClient fabClient = new org.app.blockchain.hf.client.FabricClient(adminUserContext);
			
			org.app.blockchain.hf.client.ChannelClient channelClient = fabClient.createChannelClient(org.app.blockchain.hf.config.Config.CHANNEL_NAME);
			Channel channel = channelClient.getChannel();
			Peer peer = fabClient.getInstance().newPeer(org.app.blockchain.hf.config.Config.ORG1_PEER_0, org.app.blockchain.hf.config.Config.ORG1_PEER_0_URL);
			EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
			Orderer orderer = fabClient.getInstance().newOrderer(org.app.blockchain.hf.config.Config.ORDERER_NAME, org.app.blockchain.hf.config.Config.ORDERER_URL);
			channel.addPeer(peer);
			channel.addEventHub(eventHub);
			channel.addOrderer(orderer);
			channel.initialize();

			Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Querying for all cars ...");
			Collection<ProposalResponse>  responsesQuery = channelClient.queryByChainCode("fabcar", "queryAllCars", null);
			for (ProposalResponse pres : responsesQuery) {
				String stringResponse = new String(pres.getChaincodeActionResponsePayload());
				Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, stringResponse);
			}

			Thread.sleep(10000);
			String[] args1 = {"CAR1"};
			Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, "Querying for a car - " + args1[0]);
			
			Collection<ProposalResponse>  responses1Query = channelClient.queryByChainCode("fabcar", "queryCar", args1);
			for (ProposalResponse pres : responses1Query) {
				String stringResponse = new String(pres.getChaincodeActionResponsePayload());
				Logger.getLogger(QueryChaincode.class.getName()).log(Level.INFO, stringResponse);
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
