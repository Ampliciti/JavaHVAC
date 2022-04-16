package com.ampliciti.javahvac.dao;

import com.ampliciti.javahvac.exceptions.RESTException;
import java.util.HashMap;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Interface for a REST based DAO.
 * 
 * @author Jeffrey DeYoung
 */
public interface RESTDao {

  /**
   * Does an http DELETE call.
   *
   * @param path to DELETE
   * @throws RESTException
   */
  void doDeleteCall(String path) throws RESTException;

  /**
   * Does an http GET call.
   *
   * @param path to GET
   * @return JSONObject Representing the response. If the route returns no body, the object will be
   *         empty.
   * @throws RESTException
   */
  JSONObject doGetCall(String path) throws RESTException;

  
    /**
   * Does an http GET call.
   *
   * @param path to GET
   * @param ignoreSSLProblems Unsafe operation that will allow you to override ssl certificate issues when testing locally or for extremely low-stakes calls.
   * @return JSONObject Representing the response. If the route returns no body, the object will be
   *         empty.
   * @throws RESTException
   */
  JSONObject doGetCall(String path, boolean ignoreSSLProblems) throws RESTException;
  
  /**
   * Does an HTTP POST call.
   *
   * @param path URL to post to.
   * @param toPost JSONObject of data to POST.
   * @return JSONObject representing the response. If the route returns a null body, the object will
   *         be empty.
   * @throws RESTException
   */
  JSONAware doPostCall(String path, JSONObject toPost) throws RESTException;

  /**
   * Does an HTTP POST call with headers
   *
   * @param path URL to post to.
   * @param toPost JSONObject of data to POST.
   * @return JSONObject representing the response. If the route returns a null body, the object will
   *         be empty.
   * @throws RESTException
   */
  JSONAware doPostCall(String path, JSONObject toPost, HashMap<String, String> headers)
      throws RESTException;

  /**
   * Does an HTTP PUT call.
   *
   * @param path URL to put to.
   * @param toPost JSONObject of data to PUT.
   * @return JSONObject representing the response. If the route returns no body the object will be
   *         empty.
   * @throws RESTException
   */
  JSONObject doPutCall(String path, JSONObject toPost) throws RESTException;

}
