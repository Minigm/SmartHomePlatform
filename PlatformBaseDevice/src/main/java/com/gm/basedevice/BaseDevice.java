package com.gm.basedevice;

public class BaseDevice {
    private String owner;
    private String id;
    private String type;
    private String company;
    private String project;
    private String name;
    private String[] acts;

    public BaseDevice(String owner,String name, String company, String project, String type, String id,String[] acts){
        this.owner = owner;
        this.name = name;
        this.type = type;
        this.company = company;
        this.project = project;
        this.id = id;
        this.acts = acts.clone();
    }

    public String getOwner() {
        return owner;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getProject() {
        return project;
    }

    public String getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }

    public String[] getActs() {
        return acts;
    }

    public String getAct(String actName){
        for (int i = 0;i < acts.length;i++){
            if (actName.equals(acts[i])){
                return acts[i+1];
            }
        }
        return "";
    }

    public void setAct(String actName, String value){
        for (int i = 0;i < acts.length;i++){
            if (actName.equals(acts[i])){
                acts[i+1] = value;
            }
        }
    }
}
