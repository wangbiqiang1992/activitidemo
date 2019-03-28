package com.example.activitidemo;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.converter.util.InputStreamProvider;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.data.DeploymentDataManager;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.InputStreamResource;

import javax.annotation.Resource;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeployBuilderTest extends ActivitidemoApplicationTests {

    @Resource
    private RepositoryService repositoryService;

    @Test
    public void addString() {
        String resource = "common.bpmn";
        String text = readTxtFile("D:/project/activitidemo/src/main/resources/processes/chapter3/common.bpmn");
        DeploymentBuilder db = repositoryService.createDeployment().addString(resource, text);
        Deployment deploy = db.deploy();
        Assert.assertNotNull(deploy);
    }

    private static String readTxtFile(String filepath) {
        StringBuilder builder = new StringBuilder();
        InputStreamReader read = null;
        try {
            File file = new File(filepath);
            if (file.isFile() && file.exists()) {
                read = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTx = null;
                while ((lineTx = bufferedReader.readLine()) != null) {
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

    private BpmnModel getBpmnModel() {
        SequenceFlow flow1 = new SequenceFlow();
        flow1.setId("flow1");
        flow1.setName("flow1");
        flow1.setSourceRef("start1");
        flow1.setTargetRef("userTask1");

        SequenceFlow flow2 = new SequenceFlow();
        flow2.setId("flow2");
        flow2.setName("flow2");
        flow2.setSourceRef("userTask1");
        flow2.setTargetRef("userTask2");


        SequenceFlow flow3 = new SequenceFlow();
        flow3.setId("flow3");
        flow3.setName("flow3");
        flow3.setSourceRef("userTask3");
        flow3.setTargetRef("endEvent");

        StartEvent startEvent = new StartEvent();
        startEvent.setId("start1");
        startEvent.setName("开始节点");
        startEvent.setOutgoingFlows(Collections.singletonList(flow1));


        UserTask userTask1 = new UserTask();
        userTask1.setId("userTask1");
        userTask1.setName("任务节点1");
        userTask1.setIncomingFlows(Collections.singletonList(flow1));
        userTask1.setOutgoingFlows(Collections.singletonList(flow2));

        UserTask userTask2 = new UserTask();
        userTask2.setId("userTask2");
        userTask2.setName("任务节点2");
        userTask2.setIncomingFlows(Collections.singletonList(flow2));
        userTask2.setOutgoingFlows(Collections.singletonList(flow3));

        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        endEvent.setName("结束节点");
        endEvent.setIncomingFlows(Collections.singletonList(flow3));
        Process process = new Process();
        process.setId("process1");
        process.addFlowElement(startEvent);
        process.addFlowElement(flow1);
        process.addFlowElement(userTask1);
        process.addFlowElement(flow2);
        process.addFlowElement(userTask2);
        process.addFlowElement(endEvent);
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.addProcess(process);
        return bpmnModel;
    }

    @Test
    public void testBpmnModel() {
        BpmnModel bpmnModel = getBpmnModel();
        DeploymentBuilder db = repositoryService.createDeployment()
                .addBpmnModel("common_bpmnModel.bpmn", bpmnModel);
        Deployment deploy = db.deploy();
        Assert.assertNotNull(deploy);
    }

    @Test
    public void testProcessValidator(){
        BpmnModel bpmnModel = getBpmnModel();
        ProcessValidatorFactory processValidatorFactory = new ProcessValidatorFactory();
        ProcessValidator defaultProcessValidator = processValidatorFactory.createDefaultProcessValidator();
        List<ValidationError> validate = defaultProcessValidator.validate(bpmnModel);
        System.out.println(validate.size());
    }

    @Test
    public void testConvertToXML(){
        BpmnModel bpmnModel = getBpmnModel();
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        String bpmn20Xml = new String(bpmnXMLConverter.convertToXML(bpmnModel), StandardCharsets.UTF_8);
        System.out.println(bpmn20Xml);
    }

    @Test
    public void testConvertToBpmnModel() throws Exception{
        String resource = "processes/chapter3/common.bpmn";
        InputStream xmlStream = this.getClass().getClassLoader().getResourceAsStream(resource);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(xmlStream);
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(reader);
        Assert.assertNotNull(bpmnModel);
        String bpmn20Xml = new String(bpmnXMLConverter.convertToXML(bpmnModel), StandardCharsets.UTF_8);
        System.out.println(bpmn20Xml);
    }
}
