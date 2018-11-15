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
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.ChannelConfiguration;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

/**
 * 
 * @author Balaji Kadambi
 *
 */

public class CreateChannel {

	public static void main(String[] args) {
		{
			Security.addProvider(new BouncyCastleProvider());
		}
		try {
			CryptoSuite.Factory.getCryptoSuite();
			org.app.blockchain.hf.util.Util.cleanUp();
			// Construct Channel
			org.app.blockchain.hf.user.UserContext org1Admin = new org.app.blockchain.hf.user.UserContext();
			File pkFolder1 = new File(org.app.blockchain.hf.config.Config.ORG1_USR_ADMIN_PK);
			File[] pkFiles1 = pkFolder1.listFiles();
			File certFolder1 = new File(org.app.blockchain.hf.config.Config.ORG1_USR_ADMIN_CERT);
			File[] certFiles1 = certFolder1.listFiles();
			Enrollment enrollOrg1Admin = org.app.blockchain.hf.util.Util.getEnrollment(org.app.blockchain.hf.config.Config.ORG1_USR_ADMIN_PK, pkFiles1[0].getName(),
					org.app.blockchain.hf.config.Config.ORG1_USR_ADMIN_CERT, certFiles1[0].getName());
			org1Admin.setEnrollment(enrollOrg1Admin);
			org1Admin.setMspId(org.app.blockchain.hf.config.Config.ORG1_MSP);
			org1Admin.setName(org.app.blockchain.hf.config.Config.ADMIN);

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

			// Create a new channel
			Orderer orderer = fabClient.getInstance().newOrderer(org.app.blockchain.hf.config.Config.ORDERER_NAME, org.app.blockchain.hf.config.Config.ORDERER_URL);
			ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(org.app.blockchain.hf.config.Config.CHANNEL_CONFIG_PATH));

			byte[] channelConfigurationSignatures = fabClient.getInstance()
					.getChannelConfigurationSignature(channelConfiguration, org1Admin);

			Channel mychannel = fabClient.getInstance().newChannel(org.app.blockchain.hf.config.Config.CHANNEL_NAME, orderer, channelConfiguration,
					channelConfigurationSignatures);

			Peer peer0_org1 = fabClient.getInstance().newPeer(org.app.blockchain.hf.config.Config.ORG1_PEER_0, org.app.blockchain.hf.config.Config.ORG1_PEER_0_URL);
			Peer peer1_org1 = fabClient.getInstance().newPeer(org.app.blockchain.hf.config.Config.ORG1_PEER_1, org.app.blockchain.hf.config.Config.ORG1_PEER_1_URL);
			Peer peer0_org2 = fabClient.getInstance().newPeer(org.app.blockchain.hf.config.Config.ORG2_PEER_0, org.app.blockchain.hf.config.Config.ORG2_PEER_0_URL);
			Peer peer1_org2 = fabClient.getInstance().newPeer(org.app.blockchain.hf.config.Config.ORG2_PEER_1, org.app.blockchain.hf.config.Config.ORG2_PEER_1_URL);

			mychannel.joinPeer(peer0_org1);
			mychannel.joinPeer(peer1_org1);
			
			mychannel.addOrderer(orderer);

			mychannel.initialize();
			
			fabClient.getInstance().setUserContext(org2Admin);
			mychannel = fabClient.getInstance().getChannel("mychannel");
			mychannel.joinPeer(peer0_org2);
			mychannel.joinPeer(peer1_org2);
			
			Logger.getLogger(CreateChannel.class.getName()).log(Level.INFO, "Channel created "+mychannel.getName());
            Collection peers = mychannel.getPeers();
            Iterator peerIter = peers.iterator();
            while (peerIter.hasNext())
            {
            	  Peer pr = (Peer) peerIter.next();
            	  Logger.getLogger(CreateChannel.class.getName()).log(Level.INFO,pr.getName()+ " at " + pr.getUrl());
            }
            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
