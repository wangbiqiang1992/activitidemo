package com.example.activitidemo.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/test")
public interface ActivitiConsumerService {

    /**
     * 流程demo
     * @return
     */
    @RequestMapping(value="/activitiDemo",method= RequestMethod.GET)
    public boolean startActivityDemo();

}
