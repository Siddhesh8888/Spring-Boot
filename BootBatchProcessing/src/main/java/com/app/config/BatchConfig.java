package com.app.config;

import com.app.model.Product;
import com.app.repository.ProductRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {
    @Autowired
    private ProductRepository productRepository;
    @Bean
    public Job jobBean(JobRepository jobRepository, JobCompletionNoificationImpl listener,Step steps)
    {
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(steps)
                .build();
    }

    @Bean
    public Step steps(JobRepository jobRepository, JpaTransactionManager transactionManager, ItemWriter<Product> writer)
    {
        return new StepBuilder("jobStep", jobRepository)
                .<Product,Product>chunk(5,transactionManager)
                .reader(fileItemReader())
                .processor(itemProcessor())
                .writer(writer)
                .build();

    }
//    @Bean
//    public Step steps(JobRepository jobRepository, DataSourceTransactionManager transactionManager, ItemWriter<Product> writer)
//    {
//        return new StepBuilder("jobStep", jobRepository)
//                .<Product,Product>chunk(5,transactionManager)
//                .reader(fileItemReader())
//                .processor(itemProcessor())
//                .writer(writer)
//                .build();
//
//    }

    @Bean
    public ItemProcessor<Product,Product> itemProcessor()
    {
        return new CustomItemProcessor();
    }

    @Bean
    public FlatFileItemReader<Product> fileItemReader()
    {
        return new FlatFileItemReaderBuilder<Product>()
                .name("itemReader")
                .resource(new ClassPathResource("data.csv"))
                .linesToSkip(1)
                .delimited()
                .names("productId","title","description","price","discount")
                .targetType(Product.class)
                .build();
    }


//    @Bean
//    public ItemWriter<Product> itemWriter(DataSource dataSource)
//    {
//        return new JdbcBatchItemWriterBuilder<Product>()
//                .sql("INSERT INTO product(product_id, title, description, price, discount, discounted_price) values(:productId, :title, :description, :price, :discount, :discountedPrice)")
//                .dataSource(dataSource)
//                .beanMapped()
//                .build();
//    }

    //By using Data jpa
    @Bean
    public ItemWriter<Product> itemWriter()
    {
        return new RepositoryItemWriterBuilder<Product>()
                .repository(productRepository)
                .methodName("save")
                .build();
    }


}
