/**
 *
 */
package com.wdpr.ee.authz;

import static org.junit.Assert.fail;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.wdpr.ee.authz.scopevalidation.KeystoneDeserializer;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class KeystoneDeserializerTest
{
    private static final Logger LOG = LogManager.getLogger(KeystoneDeserializerTest.class);

    /**
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
    }

    /**
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.wdpr.ee.authz.scopevalidation.KeystoneDeserializer#KeystoneDeserializer()}.
     */
    //@Test
    public void testKeystoneDeserializer()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.wdpr.ee.authz.scopevalidation.KeystoneDeserializer#abilities(String)}.
     * @throws JSONException
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @Test
    public void testJsonToObject() throws JSONException, JsonParseException, JsonMappingException, IOException
    {
        String json = "{\"keystone_response\":{\"GetKeystoneAssertionBySessionResult\":{\"ApplicationId\":\"4a3abbd5-d4c0-4a15-8d31-33e53e356f4d\""
    + ",\"AuthenticationInfo\":{\"AuthTime\":\"3/6/2015 11:27:37 PM\",\"SessionId\":\"cc2f5336-e532-4658-a337-7517e680dd8e\""
    + ",\"SessionToken\":\"Fyns54jh8f7GS12B+gSUDaXlUJXqq1osrIHzZVEZswIEv8lpdyMgDOOpFPd+YLxn\"},\"CurrentServerTime\":\"3/6/2015 11:27:37 PM\""
    + ",\"DirectoryAttributes\":[{\"DirectoryAttribute\":[{\"AttributeName\":\"uid\",\"AttributeValue\":\"*Attribute Not Available*\""
    + ",\"Directory\":\"ed\"}]}],\"Roles\":[{\"Role\":[{\"Attributes\":{\"Attribute\":[]},\"ConditionalExpression\":null,"
    + "\"ConditionalExpressionText\":null,\"DecisionSources\":null,\"FunctionalAbilities\":"
    + "{\"FunctionalAbility\":[{\"Attributes\":{\"Attribute\":[]},\"ConditionalExpression\":null,\"ConditionalExpressionText\":null,"
    + "\"EntityAccess\":[],\"Id\":\"dba4add9-2aca-42a6-b7ad-0efbb60424fc\",\"Name\":\"eTagMCOGreet\"},"
    + "{\"Attributes\":{\"Attribute\":[]},\"ConditionalExpression\":null,\"ConditionalExpressionText\":null"
    + ",\"EntityAccess\":[],\"Id\":\"c387a630-7d6a-4251-be48-e180ae3e227e\",\"Name\":\"eTagMCOQueueEntry\"}]},"
    + "\"Id\":\"b6a622aa-7296-45a5-ab8b-023c5bf52e7a\",\"Name\":\"TestArrivalOnlyRole\"}]}],"
    + "\"SecuredEntities\":[{\"Enitity\":[]}],\"ValidUntil\":\"3/15/2015 7:27:37 AM\"}},"
    + "\"assertion_category\":\"CAST_MEMBER\",\"client_id\":\"GBTS-RES.DEV-STAGE\",\"client_taxonomy_id\":1733,"
    + "\"scope\":\"edgesvcs:4f68dcf4e4b07739fe944587 edgesvcs:4f68ec04e4b07739fe944595 edgesvcs:4f68f0cfe4b07739fe944596 "
    + "edgesvcs:4f9e9cc1e4b0ebe1d98027cc edgesvcs:4f9ea47be4b0ebe1d98027ce edgesvcs:4f9eb745e4b0ebe1d98027d6 "
    + "edgesvcs:4f9eb79fe4b0ebe1d98027da edgesvcs:4f9eb7c9e4b0ebe1d98027dc edgesvcs:4fb3d810e4b048c4a74ad9d8 "
    + "edgesvcs:50170626e4b0d337bfe27f64 edgesvcs:50180576e4b0200501d87938 edgesvcs:50241af4e4b0e7d8b5980292 "
    + "edgesvcs:50241b65e4b0200501d8797f edgesvcs:5033bda6e4b051caead959a9 edgesvcs:503e46cce4b0ac11794633ae "
    + "edgesvcs:503e46f9e4b0ac11794633af TestArrivalOnlyRole%3AeTagMCOGreet TestArrivalOnlyRole%3AeTagMCOQueueEntry\","
    + "\"affiliate_name\":\"GBTS\"}";
        KeystoneDeserializer deser = new KeystoneDeserializer();
        List<String> abilities = deser.abilities(json);
        LOG.info(abilities);
    }
}
