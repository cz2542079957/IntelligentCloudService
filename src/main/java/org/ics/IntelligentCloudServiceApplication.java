package org.ics;
 
import jakarta.annotation.Resource;
import org.ics.utils.python.MNISTPredict;
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

    public static void main(String[] args) throws IOException, InterruptedException
    {
        SpringApplication.run(IntelligentCloudServiceApplication.class, args);
    }
}
