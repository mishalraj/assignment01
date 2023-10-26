package com.example.bitgo;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryBlockDataService {
    private Map<String, ArrayList<JSONObject>> inMemoryBlockData = new HashMap<>();

    public void putData(String key, ArrayList<JSONObject> value) {
        inMemoryBlockData.put(key, value);
    }

    public ArrayList<JSONObject> getData(String key) {
        return inMemoryBlockData.get(key);
    }

    public void removeData(String key) {
        inMemoryBlockData.remove(key);
    }
}
