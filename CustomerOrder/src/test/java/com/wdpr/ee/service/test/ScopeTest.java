package com.wdpr.ee.service.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class ScopeTest {
    private  static final  String jsonFilePath= "C:\\Users\\georn021\\Desktop\\scope.json";
    private  static final  String jsonFilePath2= "C:\\Users\\georn021\\Desktop\\scope-out.json";
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("scope.json");
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ScopeData sd= new ScopeData();
        Pattern pattern= new Pattern();
        pattern.setId(01);
        pattern.setToken_auth(false);

        pattern.setType("pattern");
        String[] required_scopes= {"content-access",
                "attractions"};
        pattern.setRequired_scopes(required_scopes);
        pattern.setUrl_pattern("/attractions*");

        List<Pattern> pats=  new ArrayList<>();
        pats.add(pattern);

        pattern.setId(02);

        String[] required_scopes2 = { "authenticated" };
        pattern.setRequired_scopes(required_scopes2);
        pattern.setUrl_pattern("/profile*");
        pattern.setToken_auth(true);
        pats.add(pattern);
        //pats.add(null);
        sd.setAuthorization(pats);




        ObjectMapper mapper = new ObjectMapper();


        try {




            File jsonFile = new File(jsonFilePath);
            File jsonFile2 = new File(jsonFilePath2);

            ScopeData scope =mapper.readValue(jsonFile, ScopeData.class);

            Map<String, Pattern> scopeMAp= new HashMap<>();

            for (Pattern patt: scope.getAuthorization()){
                if(patt !=  null){
                    scopeMAp.put(patt.getUrl_pattern(), patt);


                }
                System.out.println("patt  :"+patt.getUrl_pattern());
            }

            mapper.writeValue(jsonFile2, scope);

            //System.out.println(mapper.writeValueAsString(student));

        } catch (JsonGenerationException ex) {

            ex.printStackTrace();

        } catch (JsonMappingException ex) {

            ex.printStackTrace();

        } catch (IOException ex) {

            ex.printStackTrace();

        }

    }

}
