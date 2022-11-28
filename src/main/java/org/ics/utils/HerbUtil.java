package org.ics.utils;

import ai.djl.Device;
import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Normalize;
import ai.djl.modality.cv.transform.RandomResizedCrop;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.translate.Translator;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HerbUtil
{

    //规定输入尺寸
    private static final int INPUT_SIZE = 32;

    //标签文件 一种类别名字占一行
    private List<String> herbNames;

    //用于识别
    Predictor<Image, Classifications> predictor;

    //模型
    private Model model;

    public HerbUtil()
    {
        //加载标签到herbNames中
        this.loadHerbNames();
        //初始化模型工作
        this.init();
    }

    private void loadHerbNames()
    {
        BufferedReader reader = null;
        herbNames = new ArrayList<>();
        try
        {
            InputStream in = HerbUtil.class.getClassLoader().getResourceAsStream("labels/mnist.txt");
            reader = new BufferedReader(new InputStreamReader(in));
            String name = null;
            while ((name = reader.readLine()) != null)
            {
                herbNames.add(name);
            }
            System.out.println(herbNames);
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init()
    {
        ImageClassificationTranslator translator = ImageClassificationTranslator.builder()
                //下面的transform根据自己的改
                .addTransform(new Resize(INPUT_SIZE))
                .addTransform(new ToTensor())
                .addTransform(new Normalize(
                        new float[]{0.5f, 0.5f, 0.5f},
                        new float[]{0.5f, 0.5f, 0.5f}))
                //如果你的模型最后一层没有经过softmax就启用它
                .optApplySoftmax(true)
                //载入所有标签进去
                .optSynset(herbNames)
                //最终显示概率最高的5个
                .optTopK(5)
                .build();
        //创建模型
        Model model = Model.newInstance("model", Device.cpu());
        try
        {
            InputStream inputStream = HerbUtil.class.getClassLoader().getResourceAsStream("model/model.plk");
            if (inputStream == null)
            {
                throw new RuntimeException("找不到模型文件");
            }
            model.load(inputStream);

            predictor = model.newPredictor(translator);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private Image resizeImage(InputStream inputStream) {
        BufferedImage input = null;
        try {
            input = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int iw = input.getWidth(), ih = input.getHeight();
        int w = 224, h = 224;
        double scale = Math.min(1. *  w / iw, 1. * h / ih);
        int nw = (int) (iw * scale), nh = (int) (ih * scale);
        java.awt.Image img;
        //只有太长或太宽才会保留横纵比，填充颜色
        boolean needResize = 1. * iw / ih > 1.4 || 1. * ih / iw > 1.4;
        if (needResize) {
            img = input.getScaledInstance(nw, nh, BufferedImage.SCALE_SMOOTH);
        } else {
            img = input.getScaledInstance(INPUT_SIZE, INPUT_SIZE, BufferedImage.SCALE_SMOOTH);
        }
        BufferedImage out = new BufferedImage(INPUT_SIZE, INPUT_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics g = out.getGraphics();
        //先将整个224*224区域填充128 128 128颜色
        g.setColor(new Color(128, 128, 128));
        g.fillRect(0, 0, INPUT_SIZE, INPUT_SIZE);
        out.getGraphics().drawImage(img, 0, needResize ? (INPUT_SIZE - nh) / 2 : 0, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            ImageIO.write(out, "jpg", imageOutputStream);
            //去D盘看效果
            //ImageIO.write(out, "jpg", new File("D:\\out.jpg"));
            InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
            return ImageFactory.getInstance().fromInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("图片转换失败");
        }
    }

    // 预测
    public List<Classifications.Classification> predict(InputStream inputStream) {
        List<Classifications.Classification> result = new ArrayList<>();
        Image input = this.resizeImage(inputStream);
        try {
            Classifications output = predictor.predict(input);
            System.out.println("推测为：" + output.best().getClassName()
                    + ", 概率：" + output.best().getProbability());
            System.out.println(output);
            result = output.topK();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}