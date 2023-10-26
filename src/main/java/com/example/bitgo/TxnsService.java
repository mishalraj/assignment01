package com.example.bitgo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
public class TxnsService {
    @Autowired
    private InMemoryBlockDataService inMemoryBlockDataService;

    @Autowired
    CallApiToGetData getData;

    Map<String, Set<String>> ancestrySets = new HashMap<>();
    public void getTxnData(String blockHeight){
        String blockHash = getData.getHashedIdOfBlock(blockHeight);
        if (blockHash == null) {
            System.out.println("Block not found in data, invalid block height, please retry with the correct input");
            return;
        }

        if (inMemoryBlockDataService.getData(blockHash) != null) {
            System.out.println("Getting the txn data from in-memory cache of the block for block height: " + blockHeight);
            formAncestoryDataSet(inMemoryBlockDataService.getData(blockHash));
        } else {
            ArrayList<String> txnIds = getData.getAllTxnIdsForBlock(blockHash);
            int numberTxnIds = txnIds.size();
            if (txnIds.isEmpty()) {
                System.out.println("No txn data found for this block ");
                return;
            }

            AtomicInteger i = new AtomicInteger(0);

            ConcurrentLinkedQueue<JSONObject> allTxnsData = new ConcurrentLinkedQueue<>();

            IntStream.range(0, numberTxnIds)
                    .parallel()
                    .mapToObj(index -> {
                        int currentIndex = i.getAndAdd(25);
                        return getData.getTxnDataFromIndex(currentIndex, blockHash,numberTxnIds);
                    })
                    .flatMap(List::stream)
                    .forEach(allTxnsData::add);

            inMemoryBlockDataService.putData(blockHash, new ArrayList<>(allTxnsData));
            formAncestoryDataSet(new ArrayList<>(allTxnsData));
        }
    }

    public void callForm(){
        String json = " [{\n" +
                "        \"txid\": \"6587550e410fa1815cf180decc03ef84dcddd19478a081097bd2090c5e85b4b6\",\n" +
                "        \"version\": 1,\n" +
                "        \"locktime\": 4278917605,\n" +
                "        \"vin\": [\n" +
                "            {\n" +
                "                \"txid\": \"0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "                \"vout\": 4294967295,\n" +
                "                \"prevout\": null,\n" +
                "                \"scriptsig\": \"0340600a0463f77f602f706f6f6c696e2e636f6d2ffabe6d6d4e4176dc3ab61820d9dde06fde2d60832bbfd022a3c3dd2fe59d88f95a0b08fa0100000000000000c5398f003bff87935185713123d0946c12904517cd0069396a0000000000\",\n" +
                "                \"scriptsig_asm\": \"OP_PUSHBYTES_3 40600a OP_PUSHBYTES_4 63f77f60 OP_PUSHBYTES_47 706f6f6c696e2e636f6d2ffabe6d6d4e4176dc3ab61820d9dde06fde2d60832bbfd022a3c3dd2fe59d88f95a0b08fa OP_PUSHBYTES_1 00 OP_0 OP_0 OP_0 OP_0 OP_0 OP_0 OP_RETURN_197 OP_PUSHBYTES_57 <push past end>\",\n" +
                "                \"witness\": [\n" +
                "                    \"0000000000000000000000000000000000000000000000000000000000000000\"\n" +
                "                ],\n" +
                "                \"is_coinbase\": true,\n" +
                "                \"sequence\": 4294967295\n" +
                "            }\n" +
                "        ],\n" +
                "        \"vout\": [\n" +
                "            {\n" +
                "                \"scriptpubkey\": \"76a914f5da28aa5ed75c2e850a8b998e92a5bec005561e88ac\",\n" +
                "                \"scriptpubkey_asm\": \"OP_DUP OP_HASH160 OP_PUSHBYTES_20 f5da28aa5ed75c2e850a8b998e92a5bec005561e OP_EQUALVERIFY OP_CHECKSIG\",\n" +
                "                \"scriptpubkey_type\": \"p2pkh\",\n" +
                "                \"scriptpubkey_address\": \"1PQwtwajfHWyAkedss5utwBvULqbGocRpu\",\n" +
                "                \"value\": 907377760\n" +
                "            },\n" +
                "            {\n" +
                "                \"scriptpubkey\": \"6a24b9e11b6df46989f4b5ef750e551a34cdac2c75f711712ed4457084698bf049c378b7e6f3\",\n" +
                "                \"scriptpubkey_asm\": \"OP_RETURN OP_PUSHBYTES_36 b9e11b6df46989f4b5ef750e551a34cdac2c75f711712ed4457084698bf049c378b7e6f3\",\n" +
                "                \"scriptpubkey_type\": \"op_return\",\n" +
                "                \"value\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"scriptpubkey\": \"6a24aa21a9eda6b9213aaceb28a8297f060ac8058111b60450eac3832b65c6336e88719d9066\",\n" +
                "                \"scriptpubkey_asm\": \"OP_RETURN OP_PUSHBYTES_36 aa21a9eda6b9213aaceb28a8297f060ac8058111b60450eac3832b65c6336e88719d9066\",\n" +
                "                \"scriptpubkey_type\": \"op_return\",\n" +
                "                \"value\": 0\n" +
                "            },\n" +
                "            {\n" +
                "                \"scriptpubkey\": \"6a2952534b424c4f434b3a9bad0a4663489369d5b539327730c77246be3c3c56ab61fd95f40c2f00321e70\",\n" +
                "                \"scriptpubkey_asm\": \"OP_RETURN OP_PUSHBYTES_41 52534b424c4f434b3a9bad0a4663489369d5b539327730c77246be3c3c56ab61fd95f40c2f00321e70\",\n" +
                "                \"scriptpubkey_type\": \"op_return\",\n" +
                "                \"value\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"size\": 362,\n" +
                "        \"weight\": 1340,\n" +
                "        \"fee\": 0,\n" +
                "        \"status\": {\n" +
                "            \"confirmed\": true,\n" +
                "            \"block_height\": 680000,\n" +
                "            \"block_hash\": \"000000000000000000076c036ff5119e5a5a74df77abf64203473364509f7732\",\n" +
                "            \"block_time\": 1618999138\n" +
                "        }\n" +
                "    }]";

        JSONArray transactions = new JSONArray(json);
        ArrayList<JSONObject> all = new ArrayList<>();
        all.add(transactions.getJSONObject(0));

        formAncestoryDataSet(all);

    }
    private void formAncestoryDataSet(ArrayList<JSONObject> allTxnsData) {
        System.out.println("Forming ancestory data for the block .....");
        for (Object transactionObj : allTxnsData) {
            JSONObject transaction = (JSONObject) transactionObj;
            String txid = (String) transaction.get("txid");
            JSONArray vin = (JSONArray) transaction.get("vin");

            Set<String> ancestrySet = new HashSet<>();
            ancestrySet.add(txid);
            for (Object inputObj : vin) {
                JSONObject input = (JSONObject) inputObj;
                String parentTxid = (String) input.get("txid");
                if (ancestrySets.containsKey(parentTxid)) {
                    ancestrySet.addAll(ancestrySets.get(parentTxid));
                }
            }
            ancestrySets.put(txid, ancestrySet);
        }


        List<Map.Entry<String, Set<String>>> sortedTransactions = new ArrayList<>(ancestrySets.entrySet());
        sortedTransactions.sort(Comparator.comparingInt(entry -> entry.getValue().size()));

        int count = 0;
        for (int i = sortedTransactions.size() - 1; i >= 0; i--) {
            if (count >= 10) {
                break;
            }
            Map.Entry<String, Set<String>> entry = sortedTransactions.get(i);
            System.out.println("Txid: " + entry.getKey() + ", Ancestry Set Size: " + (entry.getValue().size()-1));
            count++;
        }
    }
}
