package com.example.sendEmail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "report")
public class ServiceSettings {

    private String cron;
    private String startDate;
    private String endDate;
    private  String durl;
    private String toEmail;

    public String getCron(){
        return cron;
    }

    public void setCron(){
        this.cron = cron;
    }

    public String getStartDate(){
        return startDate;
    }

    public void setStartDate(){
        this.startDate = startDate;
    }

    public String getEndDate(){
        return endDate;
    }

    public void setEndDate(){
        this.endDate = endDate;
    }

    public String getDurl(){
        return durl;
    }

    public void setDurl(){
        this.durl = durl;
    }

    public String getToEmail(){
        return toEmail;
    }

    public void setToEmail(){
        this.toEmail = toEmail;
    }
}
