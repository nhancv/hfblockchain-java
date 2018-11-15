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
package org.app.blockchain.hf.network;

import java.io.File;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

/**
 * 
 * @author Balaji Kadambi
 *
 */

public class DeployInstantiateChaincode {

	public static void main(String[] args) {
		{
			Security.addProvider(new BouncyCastleProvider());
		}
		try {
			CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
			
			org.app.blockchain.hf.user.UserContext org1Admin = new org.app.blockchain.hf.user.UserContext();
			File pkFolder1 = new File(org.app.blockchain.hf.config.Config.ORG1_USR_ADMIN_PK);
			File[] pkFiles1 = pkFolder1.listFiles();
			File certFolder = new File(org.app.blockchain.hf.config.Config.ORG1_USR_ADMIN_CERT);
			File[] certFiles = certFolder.listFiles();
			Enrollment enrollOrg1Admin = org.app.blockchain.hf.util.Util.getEnrollment(org.app.blockchain.hf.config.Config.ORG1_USR_ADMIN_PK, pkFiles1[0].getName(),
					org.app.blockchain.hf.config.Config.ORG1_USR_ADMIN_CERT, certFiles[0].getName());
			org1Admin.setEnrollment(enrollOrg1Admin);
			org1Admin.setMspId("Org1MSP");
			org1Admin.setName("admin");

			org.app.blockchain.hf.user.UserContext org2Admin = new org.app.blockchain.hf.user.UserContext();
			File pkFolder2 = new File(org.app.blockchain.hf.config.Config.ORG2_USR_ADMIN_PK);
			File[] pkFiles2 = pkFolder2.listFiles();
			File certFolder2 = new File(org.app.blockchain.hf.config.Config.ORG2_USR_ADMIN_CERT);
			File[] certFiles2 = certFolder2.listFiles();
			Enrollment enrollOrg2Admin = org.app.blockchain.hf.util.Util.getEnrollment(org.app.blockchain.hf.config.Config.ORG2_USR_ADMIN_PK, pkFiles2[0].getName(),
					org.app.blockchain.hf.config.Config.ORG2_USR_ADMIN_CERT, certFiles2[0].getName());
			org2Admin.setEnrollment(enrollOrg2Admin);
			org2Admin.setMspId(org.app.blockchain.hf.config.Config.ORG2_MSP);
			org2Admin.setName(org.app.blockchain.hf.config.Config.ADMIN);
			
			org.app.blockchain.hf.client.FabricClient fabClient = new org.app.blockchain.hf.client.FabricClient(org1Admin);

			Channel mychannel = fabClient.getInstance().newChannel(org.app.blockchain.hf.config.Config.CHANNEL_NAME);
			Orderer orderer = fabClient.getInstance().newOrderer(org.app.blockchain.hf.config.Config.ORDERER_NAME, org.app.blockchain.hf.config.Config.ORDERER_URL);
			Peer peer0_org1 = fabClient.getInstance().newPeer(org.app.blockchain.hf.config.Config.ORG1_PEER_0, org.app.blockchain.hf.config.Config.ORG1_PEER_0_URL);
			Peer peer1_org1 = fabClient.getInstance().newPeer(org.app.blockchain.hf.config.Config.ORG1_PEER_1, org.app.blockchain.hf.config.Config.ORG1_PEER_1_URL);
			Peer peer0_org2 = fabClient.getInstance().newPeer(org.app.blockchain.hf.config.Config.ORG2_PEER_0, org.app.blockchain.hf.config.Config.ORG2_PEER_0_URL);
			Peer peer1_org2 = fabClient.getInstance().newPeer(org.app.blockchain.hf.config.Config.ORG2_PEER_1, org.app.blockchain.hf.config.Config.ORG2_PEER_1_URL);
			mychannel.addOrderer(orderer);
			mychannel.addPeer(peer0_org1);
			mychannel.addPeer(peer1_org1);
			mychannel.addPeer(peer0_org2);
			mychannel.addPeer(peer1_org2);
			mychannel.initialize();

			List<Peer> org1Peers = new ArrayList<Peer>();
			org1Peers.add(peer0_org1);
			org1Peers.add(peer1_org1);
			
			List<Peer> org2Peers = new ArrayList<Peer>();
			org2Peers.add(peer0_org2);
			org2Peers.add(peer1_org2);
			
			Collection<ProposalResponse> response = fabClient.deployChainCode(org.app.blockchain.hf.config.Config.CHAINCODE_1_NAME,
					org.app.blockchain.hf.config.Config.CHAINCODE_1_PATH, org.app.blockchain.hf.config.Config.CHAINCODE_ROOT_DIR, Type.GO_LANG.toString(),
					org.app.blockchain.hf.config.Config.CHAINCODE_1_VERSION, org1Peers);
			
			
			for (ProposalResponse res : response) {
				Logger.getLogger(DeployInstantiateChaincode.class.getName()).log(Level.INFO,
						org.app.blockchain.hf.config.Config.CHAINCODE_1_NAME + "- Chain code deployment " + res.getStatus());
			}

			fabClient.getInstance().setUserContext(org2Admin);
			
			response = fabClient.deployChainCode(org.app.blockchain.hf.config.Config.CHAINCODE_1_NAME,
					org.app.blockchain.hf.config.Config.CHAINCODE_1_PATH, org.app.blockchain.hf.config.Config.CHAINCODE_ROOT_DIR, Type.GO_LANG.toString(),
					org.app.blockchain.hf.config.Config.CHAINCODE_1_VERSION, org2Peers);
			
			
			for (ProposalResponse res : response) {
				Logger.getLogger(DeployInstantiateChaincode.class.getName()).log(Level.INFO,
						org.app.blockchain.hf.config.Config.CHAINCODE_1_NAME + "- Chain code deployment " + res.getStatus());
			}
			
			org.app.blockchain.hf.client.ChannelClient channelClient = new org.app.blockchain.hf.client.ChannelClient(mychannel.getName(), mychannel, fabClient);

			String[] arguments = { "" };
			response = channelClient.instantiateChainCode(org.app.blockchain.hf.config.Config.CHAINCODE_1_NAME, org.app.blockchain.hf.config.Config.CHAINCODE_1_VERSION,
					org.app.blockchain.hf.config.Config.CHAINCODE_1_PATH, Type.GO_LANG.toString(), "init", arguments, null);

			for (ProposalResponse res : response) {
				Logger.getLogger(DeployInstantiateChaincode.class.getName()).log(Level.INFO,
						org.app.blockchain.hf.config.Config.CHAINCODE_1_NAME + "- Chain code instantiation " + res.getStatus());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
