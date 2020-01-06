package io.nodom.batch.aggregator;

import io.nodom.batch.dto.EmployeeDto;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.transform.LineAggregator;

public class EmployeeAggregator implements LineAggregator<List<EmployeeDto>> {

  private static final String LINE_SEPARATOR = "\n";

  @Override
  public String aggregate(List<EmployeeDto> employee) {
    return employee.stream()
        .map(e -> e.toString())
        .reduce("", (s, employeeDto) -> StringUtils.join(s, employeeDto, LINE_SEPARATOR));
  }
}
