package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;

  public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  // ItemReader: 入力データを提供
  @Bean
  public ItemReader<String> reader() {
    return new ItemReader<>() {
      private final List<String> data = Arrays.asList("John", "Jane", "Foo", "Bar");
      private int index = 0;

      @Override
      public String read() {
        if (index < data.size()) {
          String item = data.get(index++);
          System.out.println("ItemReader read: " + item);
          return item;
        } else {
          System.out.println("ItemReader read: null (end of data)");
          return null; // nullを返すと処理が終了
        }
      }
    };
  }


  // ItemProcessor: データを加工
  @Bean
  public ItemProcessor<String, String> processor() {
    return item -> {
      System.out.println("ItemProcessor processor: " + item); // 文字列 "ItemProcessor" を出力
      return "ItemProcessor processor:Hello, " + item + "!";
    };
  }

  // ItemWriter: 加工されたデータを出力
  @Bean
  public ItemWriter<String> writer() {
    return items -> items.forEach(item -> System.out.println("ItemWriter write:" + item));
  }

  // Stepの定義
  @Bean
  public Step step1(ItemReader<String> reader, ItemProcessor<String, String> processor, ItemWriter<String> writer) {
    return stepBuilderFactory.get("step1")
        .<String, String>chunk(2) // チャンクサイズを2に設定
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  // Jobの定義
  @Bean
  public Job job(Step step1) {
    return jobBuilderFactory.get("job")
        .incrementer(new RunIdIncrementer()) // ジョブパラメータを自動的にインクリメント
        .start(step1)
        .build();
  }
}
