package dev.findfirst.core.service;

import java.util.ArrayList;
import java.util.List;

import dev.findfirst.core.model.RobotAgent;

import org.springframework.stereotype.Service;

@Service
public class WebCheckService {

  // Find out whether the robots.txt file of a website allows scraping
  public boolean isScrapable(String url) {

    // call getRobotsTxt (return true for status code 404 and false for 5XX and other 4XX codes)
    // call parseRobots
    // if RobotAgent list empty, return true
    // call findMostSpecificAgent
    // check Allow/Disallow of the found agent

    return true;
  }

  // returns a RobotsTxtResponse so that isScrapable can deal with different status codes
  private RobotsTxtResponse getRobotsTxt(String url) {

    // issue a GET request to [DOMAIN]/robots.txt

    return new RobotsTxtResponse(200, "");
  }

  private List<RobotAgent> parseRobots(String robotsTxt) {

    // create entries for every line that can be parsed

    return new ArrayList<>();
  }

  private RobotAgent findMostSpecificAgent(String url, List<RobotAgent> agents) {

    RobotAgent currentMostSpecific = new RobotAgent("*", true, "");

    /*
     * iterate through all entries (skip entries with a user-agent other than '*') and check if they
     * are more specific than the current one
     */

    return currentMostSpecific;
  }


  private class RobotsTxtResponse {

    final private int statusCode;
    final private String text;

    private RobotsTxtResponse(int statusCode, String text) {
      this.statusCode = statusCode;
      this.text = text;
    }
  }
}
