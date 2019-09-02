package co.elastic.apm.agent.spring.webflux;

import co.elastic.apm.agent.MockReporter;
import co.elastic.apm.agent.bci.ElasticApmAgent;
import co.elastic.apm.agent.configuration.SpyConfiguration;
import co.elastic.apm.agent.impl.ElasticApmTracer;
import co.elastic.apm.agent.impl.ElasticApmTracerBuilder;
import co.elastic.apm.agent.impl.transaction.Transaction;
import co.elastic.apm.agent.spring.webflux.config.WebFluxApplication;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.SpringApplication;

import java.util.Collections;
import java.util.List;

public class AnnotatedHandlerInstrumentationTest {

    private static MockReporter reporter;
    private static ElasticApmTracer tracer;

    @BeforeClass
    @BeforeAll
    public static void setUpAll() {
        reporter = new MockReporter();
        tracer = new ElasticApmTracerBuilder()
            .configurationRegistry(SpyConfiguration.createSpyConfig())
            .reporter(reporter)
            .build();
        ElasticApmAgent.initInstrumentation(tracer, ByteBuddyAgent.install(),
            Collections.singletonList(new AnnotatedHandlerInstrumentation()));

        SpringApplication.run(WebFluxApplication.class);
    }

    @AfterClass
    @AfterAll
    public static void afterAll() {
        ElasticApmAgent.reset();
    }

    @After
    public void after() {
        reporter.reset();
    }

    @Test
    public void shouldDoGetRequest() throws Exception {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpGet request = new HttpGet("http://localhost:8080/test");
        final HttpResponse response = client.execute(request);
        final int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.SC_OK);

        final List<Transaction> transactions = reporter.getTransactions();
        Assert.assertEquals(transactions.size(), 1);
        Assert.assertEquals(transactions.get(0).getNameAsString(), "GET /test");
    }

    @Test
    public void shouldDoPostRequest() throws Exception {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost request = new HttpPost("http://localhost:8080/test");
        final HttpResponse response = client.execute(request);
        final int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.SC_OK);

        final List<Transaction> transactions = reporter.getTransactions();
        Assert.assertEquals(transactions.size(), 1);
        Assert.assertEquals(transactions.get(0).getNameAsString(), "POST /test");
    }

    @Test
    public void shouldDoPutRequest() throws Exception {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPut request = new HttpPut("http://localhost:8080/test");
        final HttpResponse response = client.execute(request);
        final int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.SC_OK);

        final List<Transaction> transactions = reporter.getTransactions();
        Assert.assertEquals(transactions.size(), 1);
        Assert.assertEquals(transactions.get(0).getNameAsString(), "PUT /test");
    }

    @Test
    public void shouldDoDeleteRequest() throws Exception {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpDelete request = new HttpDelete("http://localhost:8080/test");
        final HttpResponse response = client.execute(request);
        final int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.SC_OK);

        final List<Transaction> transactions = reporter.getTransactions();
        Assert.assertEquals(transactions.size(), 1);
        Assert.assertEquals(transactions.get(0).getNameAsString(), "DELETE /test");
    }

    @Test
    public void shouldDoPatchRequest() throws Exception {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPatch request = new HttpPatch("http://localhost:8080/test");
        final HttpResponse response = client.execute(request);
        final int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.SC_OK);

        final List<Transaction> transactions = reporter.getTransactions();
        Assert.assertEquals(transactions.size(), 1);
        Assert.assertEquals(transactions.get(0).getNameAsString(), "PATCH /test");
    }

    @Test
    public void shouldDoChainedGetRequest() throws Exception {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpGet request = new HttpGet("http://localhost:8080/test/chained");
        final HttpResponse response = client.execute(request);
        final int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.SC_OK);

        final List<Transaction> transactions = reporter.getTransactions();
        Assert.assertEquals(transactions.size(), 1);
        Assert.assertEquals(transactions.get(0).getNameAsString(), "GET /test/chained");
    }

    @Test
    public void shouldDoChainedPostRequest() throws Exception {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost request = new HttpPost("http://localhost:8080/test/chained");
        final HttpResponse response = client.execute(request);
        final int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.SC_OK);

        final List<Transaction> transactions = reporter.getTransactions();
        Assert.assertEquals(transactions.size(), 1);
        Assert.assertEquals(transactions.get(0).getNameAsString(), "POST /test/chained");
    }

    @Test
    public void shouldDoChainedPutRequest() throws Exception {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPut request = new HttpPut("http://localhost:8080/test/chained");
        final HttpResponse response = client.execute(request);
        final int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.SC_OK);

        final List<Transaction> transactions = reporter.getTransactions();
        Assert.assertEquals(transactions.size(), 1);
        Assert.assertEquals(transactions.get(0).getNameAsString(), "PUT /test/chained");
    }

    @Test
    public void shouldDoChainedDeleteRequest() throws Exception {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpDelete request = new HttpDelete("http://localhost:8080/test/chained");
        final HttpResponse response = client.execute(request);
        final int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.SC_OK);

        final List<Transaction> transactions = reporter.getTransactions();
        Assert.assertEquals(transactions.size(), 1);
        Assert.assertEquals(transactions.get(0).getNameAsString(), "DELETE /test/chained");
    }

    @Test
    public void shouldDoChainedPatchRequest() throws Exception {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPatch request = new HttpPatch("http://localhost:8080/test/chained");
        final HttpResponse response = client.execute(request);
        final int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode, HttpStatus.SC_OK);

        final List<Transaction> transactions = reporter.getTransactions();
        Assert.assertEquals(transactions.size(), 1);
        Assert.assertEquals(transactions.get(0).getNameAsString(), "PATCH /test/chained");
    }
}

