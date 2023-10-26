package com.example.bitgo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
public class CallApiToGetData {
    final static String URL_BLOCK_HASH = "https://blockstream.info/api/block-height";
    final static String URL_BLOCK_TXNLIST = "https://blockstream.info/api/block/";
    RestTemplate restTemplate = new RestTemplate();

    public String getHashedIdOfBlock(String blockHeight) {
        try {
            String hashIdOfBlock = restTemplate.getForObject(URL_BLOCK_HASH + "/" + blockHeight, String.class);
            System.out.println(hashIdOfBlock);
            return hashIdOfBlock;
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }


    public ArrayList<String> getAllTxnIdsForBlock(String blockHash) {
        try {
            ResponseEntity<ArrayList<String>> response = restTemplate.exchange(
                    URL_BLOCK_TXNLIST + "/" + blockHash + "/txids",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ArrayList<String>>() {}
            );
            ArrayList<String> txnIds = response.getBody();
            System.out.println("Total Number of txns for this block are: " + txnIds.size());
            return txnIds;
        } catch (RestClientException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public ArrayList<JSONObject> getTxnDataFromIndex(int startIndex, String blockHash, int numberTxnIds) {
        try {
            if(startIndex<numberTxnIds) {
                ResponseEntity<String> response = restTemplate.exchange(
                        URL_BLOCK_TXNLIST + "/" + blockHash + "/txs/" + startIndex,
                        HttpMethod.GET,
                        null,
                        String.class
                );
                ArrayList<JSONObject> txnData = new ArrayList<>();
                JSONArray transactions = new JSONArray(response.getBody());
                for (int i = 0; i < transactions.length(); i++) {
                    JSONObject transaction = transactions.getJSONObject(i);
                    txnData.add(transaction);
                }
                return txnData;
            }
            else {
                return new ArrayList<>();
            }
        } catch (RestClientException | JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
