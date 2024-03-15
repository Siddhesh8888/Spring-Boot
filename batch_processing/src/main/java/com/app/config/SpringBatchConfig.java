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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
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
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public ItemReader<Product> reader()
    {
        return new FlatFileItemReaderBuilder<Product>()
                .resource(new ClassPathResource("data.csv"))
                .name("csvReader")
                .linesToSkip(1)
                .lineMapper(lineMapper())
                .build();
    }


    private LineMapper<Product> lineMapper()
    {
        DefaultLineMapper lineMapper = new DefaultLineMapper();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("productId","title","description","price","discount");

        BeanWrapperFieldSetMapper<Product> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Product.class);

        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.setLineTokenizer(lineTokenizer);
        return lineMapper;
    }

    @Bean
    public ItemProcessor<Product,Product> processor()
    {
        return new CustomItemProcessor();
    }

    @Bean
    public ItemWriter<Product> writer()
    {
        return new RepositoryItemWriterBuilder<Product>()
                .repository(productRepository)
                .methodName("save")
                .build();
    }

    @Bean
    public Step step1()
    {
        return new StepBuilder("csv-step",jobRepository)
                .<Product,Product>chunk(5,transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job(Step step)
    {
        return new JobBuilder("job",jobRepository).start(step).build();
    }
}
