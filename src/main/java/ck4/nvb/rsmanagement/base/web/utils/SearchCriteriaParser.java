package ck4.nvb.rsmanagement.base.web.utils;

import ck4.nvb.rsmanagement.base.application.exception.InvalidFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchCriteriaParser {

  private static final Pattern pattern =
      Pattern.compile("([\\w.]+?)(:|<>|<|>|<:|>:|~|\\(\\))([\\w\\s(),.:-]+?),");

  public static List<SearchCriteria> parse(List<String> filter) {
    List<SearchCriteria> criterias = new ArrayList<>();
    if (null != filter) {
      List<SearchCriteria> collect =
          filter.parallelStream().map(SearchCriteriaParser::validateFilterPattern).toList();
      criterias.addAll(collect);
    }
    return criterias;
  }

  private static SearchCriteria validateFilterPattern(String filter) {
    Matcher matcher = pattern.matcher(filter + ",");
    if (matcher.find()) {
      SearchOperator operator = SearchOperator.getOperator(matcher.group(2));
      if (operator == null) {
        throw new InvalidFormatException("Invalid operator format: " + matcher.group(2));
      }
      return new SearchCriteria(matcher.group(1), operator, matcher.group(3));
    } else {
      throw new InvalidFormatException("Invalid filter format: " + filter);
    }
  }
}
