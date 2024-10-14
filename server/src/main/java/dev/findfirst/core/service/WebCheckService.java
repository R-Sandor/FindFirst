package dev.findfirst.core.service;

import java.util.List;

import dev.findfirst.core.service.RobotsFetcher.RobotsTxtResponse;

import crawlercommons.robots.SimpleRobotRules;
import crawlercommons.robots.SimpleRobotRulesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WebCheckService {

  private final RobotsFetcher robotsFetcher;

  public WebCheckService(@Autowired RobotsFetcher robotsFetcher) {
    this.robotsFetcher = robotsFetcher;
  }

  // Find out whether the robots.txt file of a website allows scraping
  public boolean isScrapable(String url) {
    RobotsTxtResponse robotsTxtResponse = robotsFetcher.getRobotsTxt(url);
    SimpleRobotRulesParser parser = new SimpleRobotRulesParser();
    SimpleRobotRules rules;
    if (robotsTxtResponse.statusCode() >= 400 && robotsTxtResponse.statusCode() <= 599) {
      rules = parser.failedFetch(robotsTxtResponse.statusCode());
    } else {
      rules = parser.parseContent(url, robotsTxtResponse.text(), robotsTxtResponse.contentType(),
          List.of());
    }
    return rules.isAllowed(url);
  }
}
