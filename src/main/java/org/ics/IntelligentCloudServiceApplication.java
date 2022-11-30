package org.ics;

import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.*;
import org.ics.utils.python.CheckPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@SpringBootApplication
public class IntelligentCloudServiceApplication
{

    public static void main(String[] args) throws ModelNotFoundException, MalformedModelException, IOException, TranslateException, InterruptedException
    {
        SpringApplication.run(IntelligentCloudServiceApplication.class, args);

        String param = "C:/Users/chenzhen/Desktop/CODING/pythonProject/dev/00000000.png";
        String[] command = new String[]{"C:\\Users\\chenzhen\\anaconda3\\envs\\pytouch\\python.exe", "src/main/resources/org/ics/pythonUtils/checkPassword.py", param};


    }
}
