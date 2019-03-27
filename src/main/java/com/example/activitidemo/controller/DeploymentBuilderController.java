package com.example.activitidemo.controller;

import com.example.activitidemo.controller.vo.StartFlowVO;
import com.example.activitidemo.controller.vo.TaskVO;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/activiti")
public class DeploymentBuilderController {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;

    @RequestMapping("/deploy")
    public Object depolyProcess(@RequestParam("file") MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        DeploymentBuilder db = repositoryService.createDeployment().addInputStream(file.getName(),inputStream);
        return db.deploy();
    }

    @RequestMapping("/startFlow")
    public Object startFlow(@RequestBody StartFlowVO startFlowVO){
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(startFlowVO.getProcessDefinitionKey(),startFlowVO.getParamMap());
        return processInstance.getProcessDefinitionId();
    }

    @RequestMapping("/queryTasks")
    public Object queryTasks(@RequestParam("assignee")String assignee){
        List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
        return list.stream().map(this::convertTask2TaskVO).collect(Collectors.toList());
    }

    private TaskVO convertTask2TaskVO(Task task){
        TaskVO taskVO = new TaskVO();
        taskVO.setTaskId(task.getId());
        taskVO.setTaskName(task.getName());
        taskVO.setCreateTime(DateFormatUtils.format(task.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        taskVO.setProcessInstanceId(task.getProcessInstanceId());
        taskVO.setAssignee(task.getAssignee());
        return taskVO;
    }
}
