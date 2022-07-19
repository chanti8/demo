package com.example.sendEmail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling

public class SendEmailApplication implements CommandLineRunner {

	@Autowired
	private EmailService emailService;

	@Autowired
	private DashboardService dashboardService;

	public static void main(String[] args) {
		SpringApplication.run(SendEmailApplication.class, args);
	}

	@Override
	public void run(String... args){

		JSONArray buildDataArray = new JSONArray();
		JSONObject staticDataObject = new JSONObject();
		JSONArray dashboards = new JSONArray();
		dashboards = dashboardService.allDashboards("https://neusamiksha.onedev.neustar.biz/api/dashboard");

		int cnt = 0;
		String text = "<html><table width='100%' border='1' align='center'><tr align='center'><td><b>S.No.<b></td><td><b>Product Line<b></td><td><b>Git Repo<b></td><td><b>Code Coverage<b></td><td><b>Code Security Issues<b></td><td><b>Blocker Issues<b></td><td><b>Critical Issues<b></td><td><b>Major Issues<b></td><td><b>Sonar URL<b></td><td><b>Maintainers<b></td><td><b>Scan Date<b></td></tr>";
		for (Object obj : dashboards){
			JSONObject jobj = (JSONObject) obj;
			JSONArray owners = new JSONArray();
			String cid = (String) jobj.get("componentId");
			String tit = (String) jobj.get("title");
			String[] tit1 = tit.split(" ");
			String titl = tit1[0];
			String url = "https://neusamiksha.onedev.neustar.biz/api/quality/static-analysis?componentId="+cid+"&max=1";
			System.out.print(tit+" - "+System.lineSeparator());
			String rext = (String) jobj.get("repo_extn");
			//System.out.print(rext+"-"+jobj.get("repUl")+System.lineSeparator());
			staticDataObject = dashboardService.scanInfo(url);
			if (staticDataObject.size() != 0){
				cnt = cnt + 1;
				System.out.print(staticDataObject+System.lineSeparator());
				owners = dashboardService.getOwners("https://git.nexgen.neustar.biz/api/v4/projects/"+rext+"/members");
				text = text+"<tr align='center'><td>"+cnt+"</td><td>"+titl+"</td><td>"+staticDataObject.get("name")+"</td><td>"+staticDataObject.get("coverage")+"</td><td>"+staticDataObject.get("violations")+"</td><td>"+staticDataObject.get("blocker_violations")+"</td><td>"+staticDataObject.get("critical_violations")+"</td><td>"+staticDataObject.get("major_violations")+"</td><td>"+staticDataObject.get("url")+"</td><td>";
				String tmp = "";
				for (Object ownr : owners){
					tmp = tmp+"@"+ownr+" ";
				}
				text = text + tmp+"</td><td>"+staticDataObject.get("ScanDate")+"</td></tr>";
			}
			else {System.out.print("Null Value"+System.lineSeparator());}
		}
		text = text+"</table></html>";
		//System.out.print(text+System.lineSeparator());
		emailService.sendMailWithInlineResources("07ea034a.team.neustar@amer.teams.ms", "NeuSamiksha Dashboard - Sonar Scan Report",text);
		//emailService.sendMailWithInlineResources("duminy2010226@gmail.com", "NeuSamiksha Dashboard Scan Report",text);
		//System.out.print(dashboards);
		//buildDataArray = dashboardService.buildInfo("https://neusamiksha.onedev.neustar.biz/api/build?componentId=625687212ab79c000a59fca7");
		//staticDataObject = dashboardService.scanInfo("https://neusamiksha.onedev.neustar.biz/api/quality/static-analysis?componentId=625687212ab79c000a59fca7&max=1");

		//System.out.print(buildDataArray);
		//System.out.print(staticDataObject);

		/*String st = (String) staticDataObject.get("alert_status");
		String urll = (String) staticDataObject.get("url");
		String nm = (String) staticDataObject.get("name");
		//String st = "OKay";
		if (!st.equals("OK")){
			String str1 = "This is an Alert Message regarding Pipeline - "+nm;
			String str2 = "For more details refer following links:";
			String str3 = "Neusamiksha Dashboard Link - https://neusamiksha.onedev.neustar.biz/#/dashboard/6283539c2ab79c000a5a5272";
			String str4 = "Sonar dashboard Link - "+urll;
			String str = str1+System.lineSeparator()+str2+System.lineSeparator()+str3+System.lineSeparator()+str4;
			emailService.sendPreConfiguredMail(str);
		}
		else {
			System.out.print("Dashboard doesn't have any Alerts!!!");
		}*/
		//emailService.sendMail("idiot8b@gmail.com", "Hi", "Ho ho ho");
		//emailService.sendPreConfiguredMail(text);
		//emailService.sendMailWithInlineResources("idiot8b@gmail.com", "NeuSamiksha Dashboard Status", "C:\\\\Users\\\\hr186\\\\Documents\\\\WorkSpace\\\\sample2.json");

		//String to, String subject, String body, String fileToAttach
		//System.out.print("------------------------------> Calling");
		//emailService.sendMailWithAttachment("idiot8b@gmail.com", "NeuSamiksha Dashboard Status", "Please find the attached file!!!", "C:\\Users\\hr186\\Documents\\WorkSpace\\sample2.json");
	}

}
