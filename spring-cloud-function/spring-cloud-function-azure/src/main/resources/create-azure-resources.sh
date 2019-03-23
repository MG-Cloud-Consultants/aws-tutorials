#!/bin/bash

functionAppName="spring-cloud-function-azure"
collectionName="demoentity"
resourceGroup="spring-cloud-function-azure"


# Create an Azure Cosmos DB.
az cosmosdb create \
  --name $collectionName \
  --resource-group $resourceGroup

# Get the Azure Cosmos DB connection string.
endpoint=$(az cosmosdb show \
  --name $collectionName \
  --resource-group $resourceGroup \
  --query documentEndpoint \
  --output tsv)

key=$(az cosmosdb list-keys \
  --name $collectionName \
  --resource-group $resourceGroup \
  --query primaryMasterKey \
  --output tsv)

# Configure function app settings to use the Azure Cosmos DB connection string.
az functionapp config appsettings set \
  --name $functionAppName \
  --resource-group $resourceGroupe \
  --setting CosmosDB_Endpoint=$endpoint CosmosDB_Key=$key