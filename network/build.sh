#!/bin/bash
#
# Exit on first error, print all commands.
export PATH=${PWD}/bin:${PWD}:$PATH
set -e

#Start from here
echo -e "\nStopping the previous network (if any)"
set -x
docker-compose -f docker-compose.yaml down
set +x

# # If need to re-generate the artifacts, uncomment the following lines and run
# #
# rm -Rf config
# mkdir config
# rm -Rf crypto-config
# cryptogen generate --config=./crypto-config.yaml
# CHANNEL_NAME="mychannel"
# configtxgen -profile TwoOrgsOrdererGenesis -outputBlock ./config-artifacts/genesis.block -channelID $CHANNEL_NAME
# configtxgen -profile TwoOrgsChannel -outputCreateChannelTx ./config-artifacts/channel.tx -channelID $CHANNEL_NAME
# configtxgen -profile TwoOrgsChannel -outputAnchorPeersUpdate ./config-artifacts/Org1MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org1MSP
# configtxgen -profile TwoOrgsChannel -outputAnchorPeersUpdate ./config-artifacts/Org2MSPanchors.tx -channelID $CHANNEL_NAME -asOrg Org2MSP

# # Gen ca key
# ARCH=$(uname -s | grep Darwin)
# if [ "$ARCH" == "Darwin" ]; then
# 	OPTS="-it"
# else
# 	OPTS="-i"
# fi
# cp docker-compose-root.yml docker-compose.yml
# CURRENT_DIR=$PWD
# cd crypto-config/peerOrganizations/org1.example.com/ca/
# PRIV_KEY=$(ls *_sk)
# cd "$CURRENT_DIR"
# sed $OPTS "s/CA1_PRIVATE_KEY/${PRIV_KEY}/g" docker-compose.yml

# cd crypto-config/peerOrganizations/org2.example.com/ca/
# PRIV_KEY=$(ls *_sk)
# cd "$CURRENT_DIR"
# sed $OPTS "s/CA2_PRIVATE_KEY/${PRIV_KEY}/g" docker-compose.yml
# # If MacOSX, remove the temporary backup of the docker-compose file
# if [ "$ARCH" == "Darwin" ]; then
# 	rm docker-compose.ymlt
# fi

# # Move config to network_resources
# rm -Rf ../network_resources/config-artifacts/*
# rm -Rf ../network_resources/crypto-config/*

# cp -R config/ ../network_resources/config-artifacts/
# cp -R crypto-config/ ../network_resources/crypto-config/

#
# Create and Start the Docker containers for the network
echo -e "\nSetting up the Hyperledger Fabric 1.1 network"
docker-compose -f docker-compose.yaml up -d
sleep 15
echo -e "\nNetwork setup completed!!\n"

