package com.haxi.mh.model;

/**
 * Created by Administrator on 2017/8/13.
 */

public class MyAffairsYibanbiBean {
    private String attr2;
    private String comoleteTime;
    private String name;
    private String startTime;
    private int remindernum;
    private int rn;


    /***********/
    private String createTime;
    private String completeTime;

    /*********/
    private String title;
    private String processTime;
    private String currentLink;
    private String createUser;
    private String assigneeName;
    /*****************************/

    private String procedureName;
    private String remindTime;
    private String reminderdName;
    private String remindreContent;

    private String remindenodeName;
    private String remindreStatus;
    private String id;
    private String processInstanceId;
    private String code;
    private String descn;

    private String categoryName;
    private String proecssDefinitionId;
    private String businessKey;
    private String applyTime;
    private String sickLeaveId;
    private String satrtTime;

    private int reminderNum;

    private String processStarter;
    /**************/
    private String remindContent;
    private String procedureId;
    private String action;
    private String reminderName;
    private String assignee;
    private String remidereTime;


    private String category;
    private String remind_flag;
    private String examinationInfo;
    private String taskInfoId;

    /***新增字段***/
    private String humanTaskId;

    private String time;
    //1案件已点评 2案件未点评
    private int ifCaseAnswer;


    /*
    * 风险警示
    * {"caseName":"检察长审批",
    * "caseInfoid":"67501",
    * "riskName":"李纲",
    * "riskCode":"r001",
    * "createTime":"2018-05-16 19:16:51",
    * "mechanism":"0000",
    * "mechanismName":"新乡市人民检察院"}
    * */
    private String caseName;
    private String caseInfoid;
    private String riskName;
    private String riskCode;
    private String mechanism;
    private String mechanismName;
    private String isCasetRisk;//isCasetRisk":"0" 代表有风险警示 ，无风险警示信息时不会返回该字段

    private String reviewName;
    private String deptMechName;
    private String caseInfoId;

    public String getCaseInfoId() {
        return caseInfoId;
    }

    public void setCaseInfoId(String caseInfoId) {
        this.caseInfoId = caseInfoId;
    }

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public String getDeptMechName() {
        return deptMechName;
    }

    public void setDeptMechName(String deptMechName) {
        this.deptMechName = deptMechName;
    }

    public String getMechanism() {
        return mechanism;
    }

    public void setMechanism(String mechanism) {
        this.mechanism = mechanism;
    }

    public String getMechanismName() {
        return mechanismName;
    }

    public void setMechanismName(String mechanismName) {
        this.mechanismName = mechanismName;
    }

    public String getIsCasetRisk() {
        return isCasetRisk;
    }

    public void setIsCasetRisk(String isCasetRisk) {
        this.isCasetRisk = isCasetRisk;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseInfoid() {
        return caseInfoid;
    }

    public void setCaseInfoid(String caseInfoid) {
        this.caseInfoid = caseInfoid;
    }

    public String getRiskName() {
        return riskName;
    }

    public void setRiskName(String riskName) {
        this.riskName = riskName;
    }

    public String getRiskCode() {
        return riskCode;
    }

    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }

    public int getIfCaseAnswer() {
        return ifCaseAnswer;
    }

    public void setIfCaseAnswer(int ifCaseAnswer) {
        this.ifCaseAnswer = ifCaseAnswer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHumanTaskId() {
        return humanTaskId;
    }

    public void setHumanTaskId(String humanTaskId) {
        this.humanTaskId = humanTaskId;
    }

    public String getTaskInfoId() {
        return taskInfoId;
    }

    public void setTaskInfoId(String taskInfoId) {
        this.taskInfoId = taskInfoId;
    }

    public String getExaminationInfo() {
        return examinationInfo;
    }

    public void setExaminationInfo(String examinationInfo) {
        this.examinationInfo = examinationInfo;
    }

    public String getRemind_flag() {
        return remind_flag;
    }

    public void setRemind_flag(String remind_flag) {
        this.remind_flag = remind_flag;
    }

    public String getRemidereTime() {
        return remidereTime;
    }

    public void setRemidereTime(String remidereTime) {
        this.remidereTime = remidereTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    private String topFlow;

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getTopFlow() {
        return topFlow;
    }

    public void setTopFlow(String topFlow) {
        this.topFlow = topFlow;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    private String taskId;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public String getRemindContent() {
        return remindContent;
    }

    public void setRemindContent(String remindContent) {
        this.remindContent = remindContent;
    }

    public String getProcessStarter() {
        return processStarter;
    }

    public void setProcessStarter(String processStarter) {
        this.processStarter = processStarter;
    }

    public int getReminderNum() {
        return reminderNum;
    }

    public void setReminderNum(int reminderNum) {
        this.reminderNum = reminderNum;
    }

    public String getSatrtTime() {
        return satrtTime;
    }

    public void setSatrtTime(String satrtTime) {
        this.satrtTime = satrtTime;
    }

    public String getSickLeaveId() {
        return sickLeaveId;
    }

    public void setSickLeaveId(String sickLeaveId) {
        this.sickLeaveId = sickLeaveId;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getProecssDefinitionId() {
        return proecssDefinitionId;
    }

    public void setProecssDefinitionId(String proecssDefinitionId) {
        this.proecssDefinitionId = proecssDefinitionId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescn() {
        return descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemindreStatus() {
        return remindreStatus;
    }

    public void setRemindreStatus(String remindreStatus) {
        this.remindreStatus = remindreStatus;
    }

    public String getRemindenodeName() {
        return remindenodeName;
    }

    public void setRemindenodeName(String remindenodeName) {
        this.remindenodeName = remindenodeName;
    }

    public int getRemindernum() {
        return remindernum;
    }

    public void setRemindernum(int remindernum) {
        this.remindernum = remindernum;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public String getReminderdName() {
        return reminderdName;
    }

    public void setReminderdName(String reminderdName) {
        this.reminderdName = reminderdName;
    }

    public String getRemindreContent() {
        return remindreContent;
    }

    public void setRemindreContent(String remindreContent) {
        this.remindreContent = remindreContent;
    }

    public String getHandleLink() {
        return handleLink;
    }

    public void setHandleLink(String handleLink) {
        this.handleLink = handleLink;
    }

    private String handleLink;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getCurrentLink() {
        return currentLink;
    }

    public void setCurrentLink(String currentLink) {
        this.currentLink = currentLink;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getComoleteTime() {
        return comoleteTime;
    }

    public void setComoleteTime(String comoleteTime) {
        this.comoleteTime = comoleteTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRn() {
        return rn;
    }

    public void setRn(int rn) {
        this.rn = rn;
    }
}
