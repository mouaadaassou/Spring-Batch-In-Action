package io.nodom.batch.writer;

import io.nodom.batch.aggregator.EmployeeAggregator;
import io.nodom.batch.dto.EmployeeDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.core.io.Resource;

public class OddItemWriter extends FlatFileItemWriter<List<EmployeeDto>> {

  private Resource resource;

  public OddItemWriter(Resource resource) {
    super();
//    this.resource = resource;
    this.setLineAggregator(new EmployeeAggregator());
    this.setResource(resource);
  }

  @Override
  public String doWrite(List<? extends List<EmployeeDto>> items) {
    StringBuilder lines = new StringBuilder();
    List<EmployeeDto> collect = (items.get(0)).stream()
        .filter(item -> item.getId() % 2 != 0)
        .collect(Collectors.toList());
    lines.append(this.lineAggregator.aggregate(collect)).append(this.lineSeparator);
    return lines.toString();
  }

}
