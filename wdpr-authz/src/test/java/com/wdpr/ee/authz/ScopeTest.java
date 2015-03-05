package com.wdpr.ee.authz;

import com.wdpr.ee.authz.model.AuthDO;
import com.wdpr.ee.authz.model.Scope;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

/**
 * Scope Test
 */
public class ScopeTest
{
    private static final Logger LOG = LogManager.getLogger(ScopeTest.class);
    //private static final String jsonFilePath = "C:\\Users\\georn021\\Desktop\\scope.json";
    private static final String jsonFilePath2 = "scope-out.json";

    /**
     *
     */
    @Test
    public void testScope()
    {
        ScopeTest sc = new ScopeTest();

        sc.met();

        // TODO Auto-generated method stub
        Scope sd = new Scope();
        // AuthDO pattern= (new Scope()).new AuthDO();
        AuthDO pattern = new AuthDO();
        pattern.setId(01);
        pattern.setAuthToken(false);

        pattern.setAuthType("pattern");
        String[] required_scopes =
        { "content-access", "attractions" };
        pattern.setScopesRequired(required_scopes);
        pattern.setUrlPattern("/attractions*");

        List<AuthDO> pats = new ArrayList<>();
        pats.add(pattern);

        pattern.setId(02);

        String[] required_scopes2 =
        { "authenticated" };
        pattern.setScopesRequired(required_scopes2);
        pattern.setUrlPattern("/profile*");
        pattern.setAuthToken(true);
        pats.add(pattern);
        // pats.add(null);
        sd.setAuthorization(pats);

        ObjectMapper mapper = new ObjectMapper();

        try
        {
            InputStream scopeStream = ScopeTest.class.getClassLoader().getResourceAsStream("scope.json");
            //File jsonFile = new File(jsonFilePath);
            File jsonFile2 = new File(jsonFilePath2);

            Scope scope = mapper.readValue(scopeStream, Scope.class);

            Map<String, AuthDO> scopeMAp = new HashMap<>();

            for (AuthDO patt : scope.getAuthorization())
            {
                if (patt != null)
                {
                    scopeMAp.put(patt.getUrlPattern(), patt);
                    LOG.info("patt  :" + patt.getUrlPattern());
                }
            }

            mapper.writeValue(jsonFile2, scope);
            scopeStream.close();

            // LOG.info(mapper.writeValueAsString(student));
        }
        catch (JsonGenerationException ex)
        {
            LOG.error(ex);
        }
        catch (JsonMappingException ex)
        {
            LOG.error(ex);
        }
        catch (IOException ex)
        {
            LOG.error(ex);
        }
    }

    /**
     *
     */
    public void met()
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File(getClass().getClassLoader().getResource("scope.json")
                    .getFile());

            Map<String, AuthDO> scopeMAp = new HashMap<>();

            Scope scope = mapper.readValue(jsonFile, Scope.class);

            LOG.info(" path :" + jsonFile.getAbsolutePath());

            for (AuthDO patt : scope.getAuthorization())
            {
                if (patt != null)
                {
                    scopeMAp.put(patt.getUrlPattern(), patt);
                }
            }

            LOG.info(" path :" + jsonFile.getCanonicalPath());
            LOG.info(" path :" + jsonFile.getAbsolutePath());
        }
        catch (JsonGenerationException ex)
        {
            LOG.error(ex);
        }
        catch (JsonMappingException ex)
        {
            LOG.error(ex);
        }
        catch (IOException ex)
        {
            LOG.error(ex);
        }
        catch (Exception e)
        {
            LOG.info(e);
        }
    }
}
