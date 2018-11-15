
### Usage

#### 1. Pull Hyperledger Fabric images
```
./hfimages/bootstrap.sh 1.2.1 1.2.1 1.2.1
```
#### 2. Generate network artifact
```
cd network
./mvp.sh generate
```
### 3. Start Hyperledger network
```
cd network
./mvp.sh up
```

#### 3. Build source
```
cd scripts
chmod +x build.sh
./build.sh
```

#### 4. Run test network
```
cd scripts
chmod +x test.sh
./test.sh
```

#### 4. Teardown network
```
cd network
./mvp.sh down
```

#### Mvp help
```
cd network
./mvp.sh -h
```


#### Inspect genesis block
```
configtxgen -inspectBlock genesis.block --configPath=/Volumes/Data/Docs/Blockchain/HyperledgerFabric/hfblockchain-java/network
```

#### Limitation
The channel-scripts and docker-compose-full are not support in mvp version.