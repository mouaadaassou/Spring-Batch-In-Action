package io.nodom.batch.dto;


import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

  private Long id;
  private String firstName;
  private String lastName;
  private Date date;

  @Override
  public String toString() {
    return String.format("%s; %s; %s; %s", this.id, this.firstName, this.lastName, this.date);
  }
}
