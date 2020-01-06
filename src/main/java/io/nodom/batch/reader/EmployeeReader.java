package io.nodom.batch.reader;

import io.nodom.batch.dto.EmployeeDto;
import io.nodom.batch.repository.EmployeeRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.data.domain.PageRequest;

public class EmployeeReader implements ItemReader<List<EmployeeDto>> {

  private static final int NUMBER_OF_ITEMS = 10_000;

  private EmployeeRepository employeeRepository;
  private List<List<EmployeeDto>> employeeList = new ArrayList<>();

  public EmployeeReader(EmployeeRepository employeeRepository, String lines) {
    int numberOfItems = lines == null ? NUMBER_OF_ITEMS : Integer.valueOf(lines);
    this.employeeRepository = employeeRepository;
    this.employeeList
        .add(this.employeeRepository.findAllEmployees(PageRequest.of(0, numberOfItems)));
  }

  @Override
  public List<EmployeeDto> read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (!this.employeeList.isEmpty()) {
      return this.employeeList.remove(0);
    }
    return null;
  }
}
