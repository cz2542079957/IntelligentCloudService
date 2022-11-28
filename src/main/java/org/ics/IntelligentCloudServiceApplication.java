package org.ics;

import ai.djl.Application;
import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.CenterCrop;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.*;
import org.apache.tomcat.util.http.ResponseUtil;
import org.ics.utils.HerbUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class IntelligentCloudServiceApplication
{
    public static void main(String[] args) throws ModelNotFoundException, MalformedModelException, IOException, TranslateException
    {
        SpringApplication.run(IntelligentCloudServiceApplication.class, args);

        Path modeldir = Paths.get("src/main/resources/model/model02.pth");
        Translator<NDList, Long> translator = new NoBatchifyTranslator<NDList, Long>() {
            @Override
            public NDList processInput(TranslatorContext translatorContext, NDList inputs) throws Exception {
                NDManager ndManager = translatorContext.getNDManager();
                NDArray ndArray = ndManager.create(new float[2048]).reshape(1,1,2048,1);
                //ndArray作为输入
                System.out.println(ndArray);
                return new NDList(ndArray);
            }
            @Override
            public Long processOutput(TranslatorContext translatorContext, NDList ndList) throws Exception {
                System.out.println("process: " + ndList);
                System.out.println("process-1:" + ndList.get(0));
                System.out.println("process-2:" + ndList.get(0).argMax());
                NDArray tmp = ndList.get(0).argMax();
                Long label =  tmp.max().getLong();
                return  label;
            }
        };
        Criteria<NDList, Long> criteria = Criteria.builder()
                .setTypes(NDList.class,Long.class)
                .optModelPath(modeldir)
                .optTranslator(translator)
                .build();
        Predictor<NDList, Long> predictor = criteria.loadModel().newPredictor();

        NDManager manager = NDManager.newBaseManager();
        NDArray array = manager.randomUniform(0, 1, new Shape(2048));
        NDList testarray = new NDList(array);
        Long result = predictor.predict(testarray);
        System.out.println("result:" + result);
    }
}
