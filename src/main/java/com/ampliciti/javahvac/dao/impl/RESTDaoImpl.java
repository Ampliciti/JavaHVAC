/*
 * Copyright (C) 2018-2022 jeffrey
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package com.ampliciti.javahvac.dao.impl;

import com.ampliciti.javahvac.exceptions.RESTException;
import com.ampliciti.javahvac.dao.RESTDao;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author jeffrey
 */
public class RESTDaoImpl implements RESTDao {

  /**
   * String holding our content type: "application/json".
   */
  private static final String JSON_CONTENT = "application/json";

  /**
   * Logger for this class.
   */
  private Logger logger = Logger.getLogger(this.getClass());

  /**
   * Our http client.
   */
  private HttpClient client;

  /**
   * Our insecure http client.
   */
  private HttpClient insecureClient;


  /**
   * Host we are trying to talk to with REST.
   */
  private URL host;

  /**
   * Configurations for requests
   */
  private RequestConfig rc;

  /**
   * Constructor.
   *
   * @param host Host we are trying to talk to with REST.
   */
  public RESTDaoImpl(URL host) {
    client = HttpClientBuilder.create().build();
    insecureClient = buildInsecureClient();
    this.host = host;
    // future: security
    // if (base64Encode) {
    // this.authToken = new String(Base64.encodeBase64(authToken.getBytes()));
    // } else {
    // this.authToken = authToken;
    // }

    // super long timeouts, because we are lazy and would rather not fail just for really slow nodes
    rc = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(120000)
        .setConnectionRequestTimeout(120000).build();
  }

  private HttpClient buildInsecureClient() {
    try {
      final SSLContext insecureSslContext = new SSLContextBuilder()
          .loadTrustMaterial(null, (x509CertChain, authType) -> true).build();
      return HttpClientBuilder.create().setSSLContext(insecureSslContext)
          .setConnectionManager(new PoolingHttpClientConnectionManager(RegistryBuilder
              .<ConnectionSocketFactory>create()
              .register("http", PlainConnectionSocketFactory.INSTANCE).register("https",
                  new SSLConnectionSocketFactory(insecureSslContext, NoopHostnameVerifier.INSTANCE))
              .build()))
          .build();
    } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
      logger.warn("Warning, cannot build insecure HTTP Client!", e);
      return null;// terrible exception handling, but I'm literally wasting daylight right now.
    }
  }

  /**
   * Gets the host URL associated with this class.
   *
   * @return
   */
  public URL getHost() {
    return this.host;
  }

  /**
   * Does an http GET call.
   *
   * @param path to GET
   * @return JSONObject Representing the response. If the route returns no body, the object will be
   *         empty.
   * @throws RESTException
   */
  @Override
  public JSONObject doGetCall(String path) throws RESTException {
    return doGetCall(path, false);
  }

  /**
   * Does an http GET call.
   *
   * @param path to GET
   * @param ignoreSSLProblems Unsafe operation that will allow you to override ssl certificate
   *        issues when testing locally or for extremely low-stakes calls.
   * @return JSONObject Representing the response. If the route returns no body, the object will be
   *         empty.
   * @throws RESTException
   */
  @Override
  public JSONObject doGetCall(String path, boolean ignoreSSLProblems) throws RESTException {
    String responseString = "";
    HttpGet request = null;
    try {
      URL fullPath = new URL(host, path);
      logger.debug("Attempting to GET: " + fullPath.toExternalForm());
      request = new HttpGet(fullPath.toURI());
      request.setConfig(rc);
      // add request header
      request.addHeader(HttpHeaders.CONTENT_TYPE, JSON_CONTENT);

      // add auth if specified
      // future -- if I feel like it
      // if (config.getSecToken() != null) {
      // request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + config.getSecToken());
      // }
      HttpResponse response;
      if (ignoreSSLProblems) {
        logger.warn("Warning, making an insecure request to " + fullPath.toExternalForm());
        response = insecureClient.execute(request);
      } else {
        response = client.execute(request);
      }
      int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode < 200 || responseCode >= 300) {
        throw new RESTException("Error when doing a GET call agaist: " + path, response);
      }

      if (response.getEntity() != null) {
        responseString = IOUtils.toString(response.getEntity().getContent());
        logger.debug("Result from GET call: " + responseString);
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(responseString);
      } else {
        return new JSONObject();
      }

    } catch (ParseException pe) {
      throw new RESTException("Could not parse JSON: " + responseString, pe);
    } catch (IOException e) {
      throw new RESTException("Problem contacting REST service for GET, URL: " + path, e);
    } catch (URISyntaxException e) {
      throw new RESTException("Invalid URL: " + path, e);
    } finally {
      if (request != null) {
        request.reset();
      }
    }

  }

  /**
   * Does an HTTP POST call.
   *
   * @param path URL to post to.
   * @param toPost JSONObject of data to POST.
   * @return JSONObject representing the response. If the route returns a null body, the object will
   *         be empty.
   * @throws RESTException
   */
  @Override
  public JSONAware doPostCall(String path, JSONObject toPost) throws RESTException {
    HttpPost request = null;
    String responseString = "";
    try {
      URL fullPath = new URL(host, path);
      logger.debug("Attempting to POST: " + fullPath.toExternalForm() + ", payload: "
          + toPost.toJSONString());
      request = new HttpPost(fullPath.toURI());
      request.setConfig(rc);
      // add request header
      request.addHeader(HttpHeaders.CONTENT_TYPE, JSON_CONTENT);

      // add auth if specified
      // future
      // add the post data
      request.setEntity(new StringEntity(toPost.toJSONString()));
      HttpResponse response = client.execute(request);
      int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode < 200 || responseCode >= 300) {
        throw new RESTException("Error when doing a POST call agaist: " + path + ". JSON posted: "
            + toPost.toJSONString(), response);
      }

      if (response.getEntity() != null) {
        responseString = IOUtils.toString(response.getEntity().getContent());
        logger.debug("Result from POST call: " + responseString);
        JSONParser parser = new JSONParser();
        try {
          return (JSONObject) parser.parse(responseString);
        } catch (ClassCastException e) {
          return (JSONArray) parser.parse(responseString);
        }
      } else {
        return new JSONObject();
      }

    } catch (ParseException pe) {
      throw new RESTException("Could not parse JSON: " + responseString, pe);
    } catch (IOException e) {
      throw new RESTException("Problem contacting REST service for POST", e);
    } catch (URISyntaxException e) {
      throw new RESTException("Invalid URL: " + path, e);
    } finally {
      if (request != null) {
        request.reset();
      }
    }

  }

  /**
   * Does an HTTP POST call with headers
   *
   * @param path URL to post to.
   * @param toPost JSONObject of data to POST.
   * @return JSONObject representing the response. If the route returns a null body, the object will
   *         be empty.
   * @throws RESTException
   */
  @Override
  public JSONAware doPostCall(String path, JSONObject toPost, HashMap<String, String> headers)
      throws RESTException {

    String responseString = "";
    HttpPost request = null;
    try {
      URL fullPath = new URL(host, path);

      logger.debug("Attempting to POST: " + fullPath.toExternalForm() + ", payload: "
          + toPost.toJSONString());
      request = new HttpPost(fullPath.toURI());
      request.setConfig(rc);
      // add request header
      request.addHeader(HttpHeaders.CONTENT_TYPE, JSON_CONTENT);

      // add auth if specified
      // future
      // if (config.getSecToken() != null) {
      // request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + config.getSecToken());
      // }
      // check for headers if present add them to the request
      if (headers != null && !headers.isEmpty()) {
        headers.entrySet();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
          request.addHeader(entry.getKey(), entry.getValue());
        }
      }

      // add the post data
      request.setEntity(new StringEntity(toPost.toJSONString()));
      HttpResponse response = client.execute(request);
      int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode < 200 || responseCode >= 300) {
        throw new RESTException("Error when doing a POST call against: " + path + ". JSON posted: "
            + toPost.toJSONString(), response);
      }

      if (response.getEntity() != null) {
        responseString = IOUtils.toString(response.getEntity().getContent());
        logger.debug("Result from POST call: " + responseString);
        JSONParser parser = new JSONParser();
        try {
          return (JSONObject) parser.parse(responseString);
        } catch (ClassCastException e) {
          return (JSONArray) parser.parse(responseString);
        }
      } else {
        return new JSONObject();
      }

    } catch (ParseException pe) {
      throw new RESTException("Could not parse JSON: " + responseString, pe);
    } catch (IOException e) {
      throw new RESTException("Problem contacting REST service for POST", e);
    } catch (URISyntaxException e) {
      throw new RESTException("Invalid URL: " + path, e);
    } finally {
      if (request != null) {
        request.reset();
      }
    }

  }

  /**
   * Does an HTTP PUT call.
   *
   * @param path URL to put to.
   * @param toPost JSONObject of data to PUT.
   * @return JSONObject representing the response. If the route returns no body the object will be
   *         empty.
   * @throws RESTException
   */
  @Override
  public JSONObject doPutCall(String path, JSONObject toPost) throws RESTException {
    String responseString = "";
    HttpPut request = null;
    try {
      URL fullPath = new URL(host, path);
      logger.debug("Attempting to PUT: " + fullPath.toExternalForm() + ", payload: "
          + toPost.toJSONString());
      request = new HttpPut(fullPath.toURI());
      request.setConfig(rc);
      // add request header
      request.addHeader(HttpHeaders.CONTENT_TYPE, JSON_CONTENT);
      // add auth if specified
      // future
      // if (config.getSecToken() != null) {
      // request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + config.getSecToken());
      // }

      // add the put data
      request.setEntity(new StringEntity(toPost.toJSONString()));
      HttpResponse response = client.execute(request);
      int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode < 200 || responseCode >= 300) {
        throw new RESTException(
            "Error when doing a PUT call against: " + path + ". JSON put: " + toPost.toJSONString(),
            response);
      }

      if (response.getEntity() != null) {
        responseString = IOUtils.toString(response.getEntity().getContent());
        logger.debug("Result from PUT call: " + responseString);
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(responseString);
      } else {
        return new JSONObject();
      }

    } catch (ParseException pe) {
      throw new RESTException("Could not parse JSON: " + responseString, pe);
    } catch (IOException e) {
      throw new RESTException("Problem contacting REST service for POST", e);
    } catch (URISyntaxException e) {
      throw new RESTException("Invalid URL: " + path, e);
    } finally {
      if (request != null) {
        request.reset();
      }
    }

  }

  /**
   * Does an http DELETE call.
   *
   * @param path to DELETE
   * @throws RESTException
   */
  @Override
  public void doDeleteCall(String path) throws RESTException {
    HttpDelete request = null;
    try {
      URL fullPath = new URL(host, path);
      logger.debug("Attempting to DELETE: " + fullPath.toExternalForm());
      request = new HttpDelete(fullPath.toURI());
      request.setConfig(rc);
      // add request header
      request.addHeader(HttpHeaders.CONTENT_TYPE, JSON_CONTENT);
      // add auth if specified
      // future
      // if (config.getSecToken() != null) {
      // request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + config.getSecToken());
      // }

      HttpResponse response = client.execute(request);
      int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode < 200 || responseCode >= 300) {
        throw new RESTException("Error when doing a DELETE call agaist: " + path, response);
      }
    } catch (IOException e) {
      throw new RESTException("Problem contacting REST service for DELETE, URL: " + path, e);
    } catch (URISyntaxException e) {
      throw new RESTException("Invalid URL: " + path, e);
    } finally {
      if (request != null) {
        request.reset();
      }
    }

  }

}
