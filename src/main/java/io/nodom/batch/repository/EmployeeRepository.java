package io.nodom.batch.repository;

import io.nodom.batch.dto.EmployeeDto;
import io.nodom.batch.domain.Employees;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employees, Long> {

  @Query(value = "SELECT new io.nodom.batch.dto.EmployeeDto(emp.id as id, emp.firstName as firstName,"
      + " emp.lastName as lastName, emp.date as date) FROM Employees AS emp")
  List<EmployeeDto> findAllEmployees(Pageable pageable);
}
