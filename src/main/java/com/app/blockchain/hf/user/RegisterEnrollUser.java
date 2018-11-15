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
package org.app.blockchain.hf.user;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * 
 * @author Balaji Kadambi
 *
 */

public class RegisterEnrollUser {

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

			// Register and Enroll user to Org1MSP
			org.app.blockchain.hf.user.UserContext userContext = new org.app.blockchain.hf.user.UserContext();
			String name = "user"+System.currentTimeMillis();
			userContext.setName(name);
			userContext.setAffiliation(org.app.blockchain.hf.config.Config.ORG1);
			userContext.setMspId(org.app.blockchain.hf.config.Config.ORG1_MSP);

			String eSecret = caClient.registerUser(name, org.app.blockchain.hf.config.Config.ORG1);

			userContext = caClient.enrollUser(userContext, eSecret);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
