package com.parker.batchlab.config;

import com.parker.batchlab.model.Gender;
import com.parker.batchlab.model.User;
import com.parker.batchlab.repositories.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, Step step) {
        return jobBuilderFactory.get("user-loader-job")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step step(StepBuilderFactory stepBuilderFactory, ItemReader<User> csvReader,
                     RecordProcessor processor, UserWriter writer) {
        return stepBuilderFactory.get("step")
                .<User, User>chunk(100)
                .reader(csvReader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(false)
                .build();
    }

    @Bean
    public FlatFileItemReader<User> csvReader(@Value("${inputFile}") String inputFile) {
        return new FlatFileItemReaderBuilder<User>()
                .name("csv-reader")
                .resource(new ClassPathResource(inputFile))
                .delimited()
                .names("id", "first_name", "last_name", "email", "gender", "ip_address")
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{setTargetType(User.class);}})
                .build();
    }

    @Bean
    public RepositoryItemReader<User> repositoryReader (UserRepository userRepository) {
        return new RepositoryItemReaderBuilder<User>()
                .repository(userRepository)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .name("repository-reader")
                .build();
    }

    @Component
    public static class RecordProcessor implements ItemProcessor<User, User> {

        @Override
        public User process(User user) {
            user.setFirstName(user.getFirstName());
            user.setLastName(user.getLastName());
            user.setEmail(user.getEmail());
            user.setGender(user.getGender().getGenderType());
            user.setIpAddress(user.getIpAddress());
            return user;
        }
    }

    @Component
    public static class UserWriter implements ItemWriter<User> {
        @Autowired
        private UserRepository userRepository;


        @Override
        public void write(List<? extends User> users) {
            userRepository.saveAll(users);
        }
    }
}
