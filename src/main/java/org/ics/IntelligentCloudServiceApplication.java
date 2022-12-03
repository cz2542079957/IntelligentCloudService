package org.ics;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.*;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("org.ics.dao")
public class IntelligentCloudServiceApplication
{
    public static void main(String[] args) throws ModelNotFoundException, MalformedModelException, IOException, TranslateException, InterruptedException
    {
        SpringApplication.run(IntelligentCloudServiceApplication.class, args);
    }
}
