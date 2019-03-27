package com.example.activitidemo.controller.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Administrator
 */
public class StartFlowVO implements Serializable {
    private String processDefinitionKey;
    private Map<String,Object> paramMap;

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}
