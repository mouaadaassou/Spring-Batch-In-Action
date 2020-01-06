package io.nodom.batch.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class ParamValidator implements JobParametersValidator {

  @Override
  public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
    String name = jobParameters.getString("name");
    if (!StringUtils.hasText(name) || name.trim().length() < 5) {
      throw new JobParametersInvalidException("Name parameter is missing or length is less than 5 characters");
    }
  }
}
