package com.tsm.it.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.tsm.it.resource.ClientResource;
import com.tsm.it.resource.MessageResource;
import com.tsm.template.TemplateApplication;
import com.tsm.template.model.Message;
import com.tsm.template.util.ClientTestBuilder;
import com.tsm.template.util.MessageTestBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TemplateApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"it"})
@FixMethodOrder(MethodSorters.JVM)
public class MessagesControllerIT extends BaseTestIT {

    private static final String INVALID_MESSAGE_SIZE_MESSAGE = "The message must be between 2 and 300 characters.";

    private static final String INVALID_SUBJECT_SIZE_MESSAGE = "The subject must be between 2 and 30 characters.";

    private static final String INVALID_SENDER_NAME_SIZE_MESSAGE = "The sender name must be between 2 and 20 characters.";

    public static final String MESSAGE_POST_URL = "/api/v1/messages/{clientToken}";

    public static final String MESSAGE_GET_URL = "/api/v1/messages/{clientToken}/find";

    @LocalServerPort
    private int port;

    private static String host = "http://localhost";

    @Value(value = "${it.test.email}")
    private String itTestEmail;


    @Before
    public void setUp() {
        RestAssured.port = port;
        header = getHeader();
        header.put("Referer", host);
    }

    @Ignore
    @Test
    public void save_NullMessageGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().message(null);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .header("Access-Control-Allow-Origin", is(client.getHosts().iterator().next()))
                .header("Access-Control-Allow-Methods", is("POST, GET, OPTIONS"))
                .header("Access-Control-Max-Age", is("3600"))
                .header("Access-Control-Allow-Headers", is("Origin, X-Requested-With, Content-Type, Accept"))
                .header("Access-Control-Expose-Headers", is("Location"))
                .body(MESSAGE_CHECK_KEY, is("The message is required."), MESSAGE_FIELD_KEY, is("message"));
    }

    @Test
    public void save_EmptyMessageGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().message("");

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is(INVALID_MESSAGE_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is("message"));
    }

    @Test
    public void save_SmallMessageGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().message("m");

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is(INVALID_MESSAGE_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is("message"));
    }

    @Test
    public void save_LargeMessageGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().message(MessageTestBuilder.LARGE_MESSAGE);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is(INVALID_MESSAGE_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is("message"));
    }

    //
    @Test
    public void save_NullSubjectGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().subject(null);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is("The subject is required."), MESSAGE_FIELD_KEY, is("subject"));
    }

    @Test
    public void save_EmptySubjectGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().subject("");

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is(INVALID_SUBJECT_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is("subject"));
    }

    @Test
    public void save_SmallSubjectGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().subject(MessageTestBuilder.SMALL_SUBJECT);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is(INVALID_SUBJECT_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is("subject"));
    }

    @Test
    public void save_LargeSubjectGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().subject(MessageTestBuilder.LARGE_SUBJECT);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is(INVALID_SUBJECT_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is("subject"));
    }

    //

    @Test
    public void save_NullSenderNameGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().senderName(null);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is("The sender name is required."), MESSAGE_FIELD_KEY, is("senderName"));
    }

    @Test
    public void save_EmptySenderNameGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().senderName("");

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is(INVALID_SENDER_NAME_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is("senderName"));
    }

    @Test
    public void save_SmallSenderNameGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields()
                .senderName(MessageTestBuilder.SMALL_SENDER_NAME);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is(INVALID_SENDER_NAME_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is("senderName"));
    }

    @Test
    public void save_LargeSenderNameGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields()
                .senderName(MessageTestBuilder.LARGE_SENDER_NAME);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is(INVALID_SENDER_NAME_SIZE_MESSAGE), MESSAGE_FIELD_KEY, is("senderName"));
    }

    //

    @Test
    public void save_NullSenderEmailGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().senderEmail(null);

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is("The sender email is required."), MESSAGE_FIELD_KEY, is("senderEmail"));
    }

    @Test
    public void save_InvalidSenderEmailGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields().senderEmail("");

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE_CHECK_KEY, is("Invalid email."), MESSAGE_FIELD_KEY, is("senderEmail"));
    }

    @Test
    public void save_NotFoundClientGiven_ShouldReturnError() {
        // Set Up
        MessageResource resource = MessageResource.build().assertFields();

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, ClientTestBuilder.CLIENT_TOKEN).then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void save_SendEmailClientGiven_ShouldSendEmail() {
        // Set Up
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().assertFields();

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .post(MESSAGE_POST_URL, client.getToken()).then().statusCode(HttpStatus.OK.value())
                .header("Access-Control-Allow-Methods", is("POST, GET, OPTIONS"))
                .header("Access-Control-Max-Age", is("3600"))
                .header("Access-Control-Allow-Headers", is("Origin, X-Requested-With, Content-Type, Accept"))
                .header("Access-Control-Expose-Headers", is("Location")).body("message", is(resource.getMessage()))
                .body("status", is(Message.MessageStatus.SENT.name())).body("senderName", is(resource.getSenderName()))
                .body("senderEmail", is(resource.getSenderEmail())).body("subject", is(resource.getSubject()));
    }

    @Test
    public void findById_NotFoundClientGiven_ShouldReturnError() {
        // Set Up
        Map<String, String> tokenHeader = getTokenHeader();
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().headers(tokenHeader).create(client.getToken());

        // Do Test
        given().headers(tokenHeader).body(resource).contentType(ContentType.JSON).when()
                .get("/api/v1/messages/{clientToken}/{id}", ClientTestBuilder.CLIENT_TOKEN, resource.getId()).then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Ignore
    public void findById_NotFoundMessageGiven_ShouldReturnError() {
        // Set Up
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().create(client.getToken());

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .get("/api/v1/messages/{clientToken}/{id}", client.getToken(), RandomStringUtils.randomAlphanumeric(1000))
                .then().statusCode(HttpStatus.NOT_FOUND.value()).body("message", is("Message not found."));
    }

    @Test
    @Ignore
    public void findById_IdMessageFoundGiven_ShouldReturnMessage() {
        // Set Up
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(getTokenHeader()).create();
        MessageResource resource = MessageResource.build().create(client.getToken());

        // Do Test
        given().headers(header).body(resource).contentType(ContentType.JSON).when()
                .get("/api/v1/messages/{clientToken}/{id}", client.getToken(), resource.getId()).then()
                .statusCode(HttpStatus.OK.value()).body("message", is(resource.getMessage()))
                .body("status", is(Message.MessageStatus.SENT.name())).body("senderName", is(resource.getSenderName()))
                .body("senderEmail", is(resource.getSenderEmail())).body("subject", is(resource.getSubject()));
    }

    @Test
    public void findAll_InvalidSearchParamGiven_ShouldReturnError() {
        // Set Up
        header.putAll(getTokenHeader());
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(getTokenHeader()).create();

        // Do Test
        given().headers(header).contentType(ContentType.JSON).when().queryParam("search", "name:user")
                .get(MESSAGE_GET_URL, client.getToken()).then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("The search params are invalid."));
    }

    @Test
    public void findAll_NotFoundValueGiven_ShouldReturnEmptyContent() {
        // Set Up
        header.putAll(getTokenHeader());
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(getTokenHeader()).create();

        // Do Test
        given().headers(header).contentType(ContentType.JSON).when().queryParam("search", "subject:" + RandomStringUtils.random(5, true, true))
                .get(MESSAGE_GET_URL, client.getToken()).then().statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

    @Test
    public void findAll_EqualSubjectGiven_ShouldReturnMessage() {
        // Set Up
        Map<String, String> tokenMap = getTokenHeader();
        header.putAll(tokenMap);
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(tokenMap).create();
        MessageResource resource = MessageResource.build().headers(tokenMap).create(client.getToken());

        // Do Test
        given().headers(header).contentType(ContentType.JSON).when().queryParam("search", "subject:" + resource.getSubject())
                .get(MESSAGE_GET_URL, client.getToken()).then().statusCode(HttpStatus.OK.value())
                .body("size()", is(1), "[0].id", is(resource.getId().intValue()));
    }

    @Test
    public void findAll_EqualStatusGiven_ShouldReturnMessage() {
        // Set Up
        Map<String, String> tokenMap = getTokenHeader();
        header.putAll(tokenMap);
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(tokenMap).create();
        MessageResource.build().headers(tokenMap).create(client.getToken());

        // Do Test
        given().headers(header).contentType(ContentType.JSON).when().queryParam("search", "status:" + Message.MessageStatus.SENT)
                .get(MESSAGE_GET_URL, client.getToken()).then().statusCode(HttpStatus.OK.value())
                .body("size()", is(greaterThan(0)));
    }

    @Test
    public void findAll_EqualStatusNotFoundGiven_ShouldReturnEmptyContent() {
        // Set Up
        Map<String, String> tokenMap = getTokenHeader();
        header.putAll(tokenMap);
        ClientResource client = ClientResource.build().emailRecipient(itTestEmail).headers(tokenMap).create();
        MessageResource resource = MessageResource.build().headers(tokenMap).create(client.getToken());

        // Do Test
        given().headers(header).contentType(ContentType.JSON).when().queryParam("search", "status:" + Message.MessageStatus.NOT_SENT)
                .get(MESSAGE_GET_URL, client.getToken()).then().statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }

}
