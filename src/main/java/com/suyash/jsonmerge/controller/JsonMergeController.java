package com.suyash.jsonmerge.controller;

import com.jsontypedef.jtd.Schema;
import com.suyash.jsonmerge.service.JsonMergeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonMergeController {

    @Autowired
    private JsonMergeService jsonMergeService;

    @GetMapping(path = "/jsonmerge")
    public Schema test() {

        System.out.println("Inside test");
        return jsonMergeService.makeSchema();
    }

}
