package com.ampliciti.javahvac.dao;

import com.ampliciti.javahvac.RESTException;
import java.util.HashMap;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Interface for a REST based DAO.
 * @author Jeffrey DeYoung
 */
public interface RESTDao {

  /**
   * Does an http DELETE call.
   *
   * @param url to DELETE
   * @throws RESTException
   */
  void doDeleteCall(String url) throws RESTException;

  /**
   * Does an http GET call.
   *
   * @param url to GET
   * @return JSONObject Representing the response. If the route returns no
   * body, the object will be empty.
   * @throws RESTException
   */
  JSONObject doGetCall(String url) throws RESTException;

  /**
   * Does an HTTP POST call.
   *
   * @param url URL to post to.
   * @param toPost JSONObject of data to POST.
   * @return JSONObject representing the response. If the route returns a null
   * body, the object will be empty.
   * @throws RESTException
   */
  JSONAware doPostCall(String url, JSONObject toPost) throws RESTException;

  /**
   * Does an HTTP POST call with headers
   *
   * @param url URL to post to.
   * @param toPost JSONObject of data to POST.
   * @return JSONObject representing the response. If the route returns a null
   * body, the object will be empty.
   * @throws RESTException
   */
  JSONAware doPostCall(String url, JSONObject toPost, HashMap<String, String> headers) throws RESTException;

  /**
   * Does an HTTP PUT call.
   *
   * @param url URL to put to.
   * @param toPost JSONObject of data to PUT.
   * @return JSONObject representing the response. If the route returns no
   * body the object will be empty.
   * @throws RESTException
   */
  JSONObject doPutCall(String url, JSONObject toPost) throws RESTException;
  
}
