package StepDefinition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import hooks.Gist;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class APIStepdefs {
    private final String token1 = "ACCESS_TOKEN";
    private final String baseUrl = "https://api.github.com/gists";
    private final RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri(baseUrl).build();
    private Response response;
    private String gistId;
    private String forkedGistId;
    private final String fileName = "newFile.txt";

   public RequestSpecification getGivenAuth() {
       Properties properties = new Properties();
       String file = "test.properties";
       InputStream props = getClass().getClassLoader().getResourceAsStream(file);
       try {
           properties.load(props);
       } catch (IOException e) {
           e.printStackTrace();
          }
       String token = properties.getProperty(token1);
       return given().spec(requestSpecification).auth().oauth2(token);
   }

       @Given("^I am able to access the url$")
    public void iLoadWebPage() {
        //System.out.println("access is successful");
        response=given().get(baseUrl);
        //System.out.println("response"+response.prettyPrint());
    }

    @Then("^I verify status code$")
    public void iVerifyStatusCode() {
        response=given().get(baseUrl);
        Assert.assertEquals(response.getStatusCode(),200);
    }

    @Then("^I list all public gists$")
    public void iListAllPublicGists() {
        Response response = getGivenAuth().get("/public").andReturn();
        Gist[] gists = response.as(Gist[].class);
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(Arrays.stream(gists).allMatch(Gist::isPublic));
    }

    @When("^I create a new gist$")
    public void iCreateANewGist() {
        Response response  = getGivenAuth().body(getGist()).with().contentType("application/json").post().andReturn();
        Assert.assertEquals(response.statusCode(), 201);

        //save to update and remove this gist later
        gistId = response.as(Gist.class).getId();
        //System.out.println(gistId);

    }

    private Gist getGist() {
        Gist gist = new Gist();
        gist.setDescription("The description for this gist");
        gist.setPublic(true);
        gist.setFile(fileName, new Gist("String file content"));
        return gist;
    }

    @Then("^I verify newly created gist$")
    public void iVerifyNewlyCreatedGist() {
        Response response = getGivenAuth().get("/" + gistId).andReturn();
        System.out.println(response.prettyPrint());
        Gist gist = response.as(Gist.class);
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(gist.getFiles().containsKey(fileName));

    }

    @When("^I get a single gist for gistId \"([^\"]*)\"$")
    public void iGetASingleGistForGistId(String gistId) throws Throwable {
        Response response = getGivenAuth().get("/" + gistId).andReturn();
        Assert.assertEquals(response.statusCode(), 200);
   }

    @When("^I get a single gist for invalid \"([^\"]*)\"$")
    public void iGetASingleGistForInvalid(String gistId) throws Throwable {
        Response response = getGivenAuth().get("/" + gistId).andReturn();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Then("^I update details for gist description \"([^\"]*)\"$")
    public void iUpdateDetailsForGistDescription(String newDescription) throws Throwable {
        Gist gist = getGist();
        String oldDescription = gist.getDescription();
        String actualDescription;
        Response response;

        gist.setDescription(newDescription);
        response = getGivenAuth().body(gist).with().contentType("application/json").patch("/" + gistId).andReturn();
        actualDescription = response.as(Gist.class).getDescription();

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotEquals(actualDescription, oldDescription);
        Assert.assertEquals(actualDescription, newDescription);
    }

    @Then("^I delete the created gist$")
    public void iDeleteTheCreatedGist() {
        Response response = getGivenAuth().delete("/" + gistId).andReturn();
        Assert.assertEquals(response.statusCode(), 204);
    }

    @Then("^I star the gist with gistId \"([^\"]*)\"$")
    public void iStarTheGistWithGistId(String gistId) throws Throwable {
       String starUrl = "/" + gistId + "/star";
        Response response  = getGivenAuth().put(starUrl).andReturn();
        Assert.assertEquals(response.statusCode(), 204);
    }

    @Then("^I verify the gist with gistId \"([^\"]*)\" is starred$")
    public void iVerifyTheGistWithGistIdIsStarred(String gistId) throws Throwable {
        String starUrl = "/" + gistId + "/star";
        Response response  = getGivenAuth().get(starUrl).andReturn();
        Assert.assertEquals(response.statusCode(), 204);
    }

    @Then("^I unstar the gist with gistId \"([^\"]*)\"$")
    public void iUnstarTheGistWithGistId(String gistId) throws Throwable {
        String starUrl = "/" + gistId + "/star";
        Response response  = getGivenAuth().delete(starUrl).andReturn();
        Assert.assertEquals(response.statusCode(), 204);
    }

    @Then("^I star the invalid gist \"([^\"]*)\"$")
    public void iStarTheInvalidGist(String gistId) throws Throwable {
        String starUrl = "/" + gistId + "/star";
        Response response  = getGivenAuth().delete(starUrl).andReturn();
        Assert.assertEquals(response.statusCode(), 404);
    }

    @Then("^I fork the gist with gistId \"([^\"]*)\"$")
    public void iForkTheGistWithGistId(String gistId) throws Throwable {
        Response response  = getGivenAuth().post("/" + gistId + "/forks").andReturn();
        Gist forkedGist = response.as(Gist.class);
        Assert.assertEquals(response.statusCode(), 201);
        forkedGistId=forkedGist.getId();
    }

    @Then("^I verify the gist with gistId \"([^\"]*)\" is forked$")
    public void iVerifyTheGistWithGistIdIsForked(String gistId) throws Throwable {
        Response response  = getGivenAuth().get("/" + gistId + "/forks").andReturn();
        final Gist[] forkedGists = response.as(Gist[].class);
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(Arrays.stream(forkedGists).anyMatch(m -> m.getId().equals(forkedGistId)));
    }

    @Then("^I remove fork for gistId \"([^\"]*)\"$")
    public void iRemoveForkForGistId(String arg0) throws Throwable {
        Response response = getGivenAuth().delete("/" + forkedGistId).andReturn();
        Assert.assertEquals(response.statusCode(), 204);
    }
}

