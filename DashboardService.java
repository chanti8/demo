package com.example.sendEmail;
import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service("dashboardService")
public class DashboardService {

    public Date getScanDate(Long mil){
        Timestamp ts = new Timestamp(mil);
        Date dt = ts;
        return dt;
    }

    public JSONArray getOwners(String gurl){
        String odata = getData(gurl, "h7jD-R76jnH_Gy8J59Sx", "PRIVATE-TOKEN");
        //System.out.print(odata);
        JSONArray ownrs = new JSONArray();
        JSONParser parser3 = new JSONParser();
        try{
            Object obj8 = (Object) parser3.parse(odata);
            JSONArray jsnArray7 = (JSONArray) obj8;
            for(Object obj6 : jsnArray7){
                JSONObject resdata8 = (JSONObject) obj6;
                Long al = (Long) resdata8.get("access_level");
                int al1 = al.intValue();
                if (al1 == 40) {
                    String own_nam = (String) resdata8.get("name");
                    ownrs.add(own_nam);
                }
            }
        }catch (ParseException e6){
                System.out.print("Error in parsing gitlab owners data ");
        }
        return ownrs;
    }

    public JSONArray allDashboards(String nurl){
        String ddata = getData(nurl, "apiToken UGFzc3dvcmRJc0F1dGhUb2tlbjp7ImFwaUtleSI6IkJDZDR3dGhNUmJMWjQyaVpkT05TU3c9PSJ9Cg==", "Authorization");
        JSONArray allds = new JSONArray();
        JSONParser parser2 = new JSONParser();
        try {
            Object obj2 = (Object) parser2.parse(ddata);
            JSONArray jsnArray2 = (JSONArray) obj2;
            for(Object obj3 : jsnArray2){
                JSONObject resdata3 = (JSONObject) obj3;
                String title = (String) resdata3.get("title");
                if (resdata3.get("type").equals("Team") && (title.contains("ipaas") || title.contains("OMS") || title.contains("si "))){
                //if (resdata3.get("type").equals("Team") && title.contains("ipaas")){
                    JSONObject app = (JSONObject) resdata3.get("application");
                    JSONArray comp = (JSONArray) app.get("components");
                    JSONObject jobj1 = new JSONObject();
                    for (Object ob : comp){
                        JSONObject jb = (JSONObject) ob;
                        jobj1.put("componentId",jb.get("id"));

                        // for SCM URL
                        JSONObject colitem = (JSONObject) jb.get("collectorItems");
                        JSONArray scm_data = (JSONArray) colitem.get("SCM");
                        if(scm_data != null && scm_data.size() > 0 ) {
                            for (Object scm_tmp : scm_data) {
                                JSONObject scm_tmp1 = (JSONObject) scm_tmp;
                                JSONObject scm_options = (JSONObject) scm_tmp1.get("options");
                                String ur = (String) scm_options.get("url");
                                jobj1.put("repUl", ur);
                                String ur1 = "";
                                if (ur.contains("8022")) {
                                    ur1 = ur.replace("https://git.nexgen.neustar.biz:8022/", "");
                                    ur1 = ur1.replace("https://git.nexgen.neustar.biz/8022/", "");
                                } else {
                                    ur1 = ur.replace("https://git.nexgen.neustar.biz/", "");
                                }
                                //String[] urtmp = ur.split(":");
                                //String ur1 = urtmp[1];
                                //String ur2 = ur1.replace("8022/", "");
                                //System.out.print(title+System.lineSeparator());
                                //System.out.print(ur+System.lineSeparator());
                                //System.out.print(ur1+System.lineSeparator());
                                String ur3 = ur1.replace("/", "%2F");
                                jobj1.put("repo_extn", ur3);
                            }
                        }

                    }
                    jobj1.put("id",resdata3.get("id"));
                    jobj1.put("title",resdata3.get("title"));
                    jobj1.put("applicationName",app.get("name"));
                    allds.add(jobj1);
                }
            }
        }catch (ParseException e2){
            System.out.print("Could not parse response of all dashboards ");
        }

        return allds;
    }

    public JSONArray buildInfo(String burl){
        String bdata = getData(burl, "apiToken UGFzc3dvcmRJc0F1dGhUb2tlbjp7ImFwaUtleSI6IkJDZDR3dGhNUmJMWjQyaVpkT05TU3c9PSJ9Cg==", "Authorization");
        JSONArray jsonArray = new JSONArray();
        JSONParser parser1 = new JSONParser();
        try {
            JSONObject jsnobj1 = (JSONObject) parser1.parse(bdata);
            JSONArray jsnArray1 = (JSONArray) jsnobj1.get("result");
            for(Object obj1 : jsnArray1){
                JSONObject resdata1 = (JSONObject) obj1;
                JSONObject jobj = new JSONObject();
                jobj.put("build_number",resdata1.get("number"));
                jobj.put("build_url",resdata1.get("buildUrl"));
                jobj.put("build_status",resdata1.get("buildStatus"));
                jsonArray.add(jobj);
            }
        }catch (ParseException e1){
                System.out.print("Could not parse response ");
            }
        return jsonArray;
    }

    public JSONObject scanInfo(String surl) {
        String sdata = getData(surl, "apiToken UGFzc3dvcmRJc0F1dGhUb2tlbjp7ImFwaUtleSI6IkJDZDR3dGhNUmJMWjQyaVpkT05TU3c9PSJ9Cg==", "Authorization");
        JSONObject jsonArray1 = new JSONObject();
        JSONParser parser = new JSONParser();

        try{
            JSONObject jsnobj = (JSONObject) parser.parse(sdata);
            if (jsnobj.containsKey("result")){
                JSONArray jsnArray = (JSONArray) jsnobj.get("result");
                for(Object obj : jsnArray){
                    JSONObject resdata = (JSONObject) obj;
                    jsonArray1.put("url",resdata.get("url"));
                    jsonArray1.put("name",resdata.get("name"));
                    jsonArray1.put("ScanDate",getScanDate(Long.valueOf(resdata.get("timestamp").toString())));
                    JSONArray jsnAry = (JSONArray) resdata.get("metrics");
                    for (Object obj1 : jsnAry){
                        JSONObject mdata = (JSONObject) obj1;
                        jsonArray1.put(mdata.get("name"),mdata.get("formattedValue"));
                    }
                }
            }
        }catch (ParseException e){
            System.out.print(" parse error in scan data ");
        }

        return jsonArray1;
    }

    private String getData(String url, String basicAuth, String authType){
        URL secUrl;
        try{
            secUrl = new URL(url);
            URLConnection uc;
            uc = secUrl.openConnection();
            uc.setRequestProperty("X-Requested-With", "Curl");
            //String basicAuth = "apiToken UGFzc3dvcmRJc0F1dGhUb2tlbjp7ImFwaUtleSI6IkJDZDR3dGhNUmJMWjQyaVpkT05TU3c9PSJ9Cg==";
            //uc.setRequestProperty("Authorization", basicAuth);
            uc.setRequestProperty(authType, basicAuth);

            BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String lin = null;
            while ((lin = reader.readLine()) != null) {
                builder.append(lin);
                builder.append(System.getProperty("line.separator"));
            }
            String result = builder.toString();
            //System.out.println(result);

            return result;

        }catch (IOException e) {
            System.out.print("***************************** Error in parsing url data output ****************************");
            e.printStackTrace();

            return "";
        }
    }

}
