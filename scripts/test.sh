#!/bin/bash
CURRENT_DIR=$PWD
# Go to src
cd ../application
set -e

# Create and Initialize the channel
echo -e"-------------Create and Initialize the channel-------------"
java -cp blockchain-client.jar com.app.blockchain.hf.network.CreateChannel
sleep 2

# Deploy and Instantiate the chaincode
echo -e "-------------Deploy and Instantiate the chaincode-------------"
java -cp blockchain-client.jar com.app.blockchain.hf.network.DeployInstantiateChaincode
sleep 2

# Register and enroll users
echo -e "-------------Register and enroll users-------------"
java -cp blockchain-client.jar com.app.blockchain.hf.user.RegisterEnrollUser
sleep 2

# Perform Invoke and Query on network
echo -e "-------------Perform Invoke and Query on network-------------"
java -cp blockchain-client.jar com.app.blockchain.hf.chaincode.invocation.InvokeChaincode
java -cp blockchain-client.jar com.app.blockchain.hf.chaincode.invocation.QueryChaincode

# Restore path
cd "$CURRENT_DIR"
