package com.app.blockchain.hf.config;

import java.io.File;

public class Config {

    public static final String ORG1_MSP = "Org1MSP";

    public static final String ORG1 = "org1";

    public static final String ORG2_MSP = "Org2MSP";

    public static final String ORG2 = "org2";

    public static final String ADMIN = "admin";

    public static final String ADMIN_PASSWORD = "adminpw";

    public static final String CHANNEL_CONFIG_PATH = "network_resources/config-artifacts/channel.tx";

    public static final String ORG1_USR_BASE_PATH = "network_resources/crypto-config/peerOrganizations/org1.beesightsoft.com/users/Admin@org1.beesightsoft.com/msp";

    public static final String ORG2_USR_BASE_PATH = "network_resources/crypto-config/peerOrganizations/org2.beesightsoft.com/users/Admin@org2.beesightsoft.com/msp";

    public static final String ORG1_USR_ADMIN_PK = ORG1_USR_BASE_PATH + File.separator + "keystore";
    public static final String ORG1_USR_ADMIN_CERT = ORG1_USR_BASE_PATH + File.separator + "admincerts";

    public static final String ORG2_USR_ADMIN_PK = ORG2_USR_BASE_PATH + File.separator + "keystore";
    public static final String ORG2_USR_ADMIN_CERT = ORG2_USR_BASE_PATH + File.separator + "admincerts";

    public static final String CA_ORG1_URL = "http://ca.org1.beesightsoft.com:7054";

    public static final String CA_ORG2_URL = "http://ca.org2.beesightsoft.com:8054";

    public static final String ORDERER_URL = "grpc://orderer.beesightsoft.com:7050";

    public static final String ORDERER_NAME = "orderer.beesightsoft.com";

    public static final String CHANNEL_NAME = "mvpchannel";

    public static final String ORG1_PEER_0 = "peer0.org1.beesightsoft.com";

    public static final String ORG1_PEER_0_URL = "grpc://peer0.org1.beesightsoft.com:7051";

    public static final String ORG1_PEER_1 = "peer1.org1.beesightsoft.com";

    public static final String ORG1_PEER_1_URL = "grpc://peer1.org1.beesightsoft.com:7056";

    public static final String ORG2_PEER_0 = "peer0.org2.beesightsoft.com";

    public static final String ORG2_PEER_0_URL = "grpc://peer0.org2.beesightsoft.com:8051";

    public static final String ORG2_PEER_1 = "peer1.org2.beesightsoft.com";

    public static final String ORG2_PEER_1_URL = "grpc://peer1.org2.beesightsoft.com:8056";

    public static final String CHAINCODE_ROOT_DIR = "network_resources/chaincode";

    public static final String CHAINCODE_1_NAME = "fabcar";

    public static final String CHAINCODE_1_PATH = "github.com/fabcar";

    public static final String CHAINCODE_1_VERSION = "1";


}
