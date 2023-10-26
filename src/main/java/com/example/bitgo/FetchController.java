package com.example.bitgo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(
        value = "fetch")
public class FetchController {
    @Autowired
    TxnsService txnsService;

    @RequestMapping(value = "/block/{blockHeight}", method = RequestMethod.GET)
    public void getMerchantAndPromotionDetails(@PathVariable String blockHeight) {
        System.out.println("Calling txn api for block height : "+ blockHeight);
        txnsService.getTxnData(blockHeight);
    }
}
