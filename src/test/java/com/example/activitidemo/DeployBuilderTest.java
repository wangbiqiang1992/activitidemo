package com.example.activitidemo;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.data.DeploymentDataManager;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class DeployBuilderTest extends ActivitidemoApplicationTests {

    @Resource
    private RepositoryService repositoryService;

    @Test
    public void addString(){
        String resource = "common.bpmn";
        String text = readTxtFile("D:/project/activitidemo/src/main/resources/processes/chapter3/common.bpmn");
        DeploymentBuilder db = repositoryService.createDeployment().addString(resource,text);
        Deployment deploy = db.deploy();
        Assert.assertNotNull(deploy);
    }

    private static String readTxtFile(String filepath) {
        StringBuilder builder = new StringBuilder();
        InputStreamReader read = null;
        try {
            File file = new File(filepath);
            if(file.isFile() && file.exists()){
                read = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTx = null;
                while ((lineTx = bufferedReader.readLine())!=null){
                    builder.append(lineTx);
                }
                return builder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                read.close();
            } catch (IOException e) {
            }
        }
        return "";
    }
}
