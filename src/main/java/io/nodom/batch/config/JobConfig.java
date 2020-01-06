package io.nodom.batch.config;

import io.nodom.batch.dto.EmployeeDto;
import io.nodom.batch.reader.EmployeeReader;
import io.nodom.batch.repository.EmployeeRepository;
import io.nodom.batch.writer.EvenItemWriter;
import io.nodom.batch.writer.OddItemWriter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;


@Slf4j
@Configuration
public class JobConfig {

  private JobBuilderFactory jobBuilderFactory;
  private StepBuilderFactory stepBuilderFactory;
  private EmployeeRepository employeeRepository;


  @Autowired
  public JobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
      EmployeeRepository employeeRepository) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
    this.employeeRepository = employeeRepository;
  }

  @StepScope
  @Bean("employeeItemReader")
  public ItemReader<List<EmployeeDto>> employeeItemReader(
      @Value("#{jobParameters['lines']}") String lines) {
    return new EmployeeReader(employeeRepository, lines);
  }

  @StepScope
  @Bean("evenEmployeeItemWriter")
  public FlatFileItemWriter<List<EmployeeDto>> evenEmployeeItemWriter(
      @Value("#{jobParameters['evenFileName']}") String evenReporName) {
    return new EvenItemWriter(new FileSystemResource(evenReporName));
  }

  @StepScope
  @Bean("oddEmployeeItemWriter")
  public FlatFileItemWriter<List<EmployeeDto>> oddEmployeeItemWriter(
      @Value("#{jobParameters['oddFileName']}") String oddReportName) {
    return new OddItemWriter(new FileSystemResource(oddReportName));
  }

  @Bean("compositeItemWriter")
  public CompositeItemWriter<List<EmployeeDto>> compositeItemWriter(
      @Qualifier(("oddEmployeeItemWriter")) FlatFileItemWriter<List<EmployeeDto>> oddEmployeeItemWriter,
      @Qualifier(("evenEmployeeItemWriter")) FlatFileItemWriter<List<EmployeeDto>> evenEmployeeItemWriter) {
    return new CompositeItemWriterBuilder<List<EmployeeDto>>()
        .delegates(oddEmployeeItemWriter, evenEmployeeItemWriter)
        .build();
  }

  @Bean("employeeStep")
  public Step employeeStep(
      @Qualifier("employeeItemReader") ItemReader<List<EmployeeDto>> employeeItemReader,
      @Qualifier("compositeItemWriter") CompositeItemWriter<List<EmployeeDto>> compositeItemWriter) {
    return this.stepBuilderFactory.get("employeeStep")
        .<List<EmployeeDto>, List<EmployeeDto>>chunk(100)
        .reader(employeeItemReader)
        .writer(compositeItemWriter)
        .build();
  }

  @Bean("employeeJob")
  public Job empployeeJob(@Qualifier("employeeStep") Step employeeStep) {
    return this.jobBuilderFactory.get("employeeJob")
        .incrementer(new RunIdIncrementer())
        .start(employeeStep)
        .build();
  }
}
