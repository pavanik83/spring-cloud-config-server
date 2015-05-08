package com.wdpr.ee.authz;

import static org.junit.Assert.fail;

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

import com.wdpr.ee.authz.model.AuthDO;
import com.wdpr.ee.authz.model.AuthDO.Scope;
import com.wdpr.ee.authz.model.ScopeRequired;

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
        ScopeRequired sd = new ScopeRequired();
        // AuthDO pattern= (new Scope()).new AuthDO();
        AuthDO pattern = new AuthDO();
        pattern.setId(01);
        pattern.setAuthToken(false);

        pattern.setAuthType("pattern");
        String[] required_scopes =
        { "content-access", "attractions" };
        Scope required = new Scope();
        required.setMethod("GET");
        required.setScopesRequired(required_scopes);
        Scope[] requireds = {required};
        pattern.setScopesRequired(requireds);
        pattern.setUrlPattern("/attractions*");

        List<AuthDO> pats = new ArrayList<>();
        pats.add(pattern);

        pattern.setId(02);

        String[] required_scopes2 =
        { "authenticated" };
        Scope required2 = new Scope();
        required2.setMethod("GET");
        required2.setScopesRequired(required_scopes);
        Scope[] requireds2 = {required2};
        pattern.setScopesRequired(requireds2);
        pattern.setUrlPattern("/profile*");
        pattern.setAuthToken(true);
        pats.add(pattern);
        // pats.add(null);
        sd.setAuthorization(pats);

        ObjectMapper mapper = new ObjectMapper();

        InputStream scopeStream = null;
        try
        {
            scopeStream = ScopeTest.class.getClassLoader().getResourceAsStream("scope.json");
            //File jsonFile = new File(jsonFilePath);
            File jsonFile2 = new File(jsonFilePath2);

            ScopeRequired scope = mapper.readValue(scopeStream, ScopeRequired.class);

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
            fail(ex.getMessage());
        }
        catch (JsonMappingException ex)
        {
            LOG.error(ex);
            fail(ex.getMessage());
        }
        catch (IOException ex)
        {
            LOG.error(ex);
            fail(ex.getMessage());
        }
        finally
        {
            if (scopeStream != null)
            {
                try
                {
                    scopeStream.close();
                }
                catch (Exception ex)
                {
                    LOG.error(ex);
                    fail(ex.getMessage());
                }
            }
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

            ScopeRequired scope = mapper.readValue(jsonFile, ScopeRequired.class);

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
            fail(ex.getMessage());
        }
        catch (JsonMappingException ex)
        {
            LOG.error(ex);
            fail(ex.getMessage());
        }
        catch (IOException ex)
        {
            LOG.error(ex);
            fail(ex.getMessage());
        }
        catch (Exception e)
        {
            LOG.info(e);
            fail(e.getMessage());
        }
    }
}
