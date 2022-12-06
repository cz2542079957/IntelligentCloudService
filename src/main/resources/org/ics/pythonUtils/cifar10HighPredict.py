import cv2
import torch
import torchvision.models
from torchvision import transforms
import numpy as np
from torch.autograd import Variable
from torch.nn import functional as F
from PIL import Image
import sys
import io

myTransforms = transforms.Compose([
    transforms.ToPILImage(mode='RGB'),
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize((0.485, 0.456, 0.406), (0.229, 0.224, 0.225))])

classes = ('飞机', '汽车', '鸟', '猫',
           '鹿', '狗', '青蛙', '房子', '船', '卡车')

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
if __name__ == "__main__":
    model = torch.jit.load(sys.argv[1])  # 加载模型
    model.eval()  # 把模型转为test模式

    # 加载自己的图片数据集
    img = cv2.imread(sys.argv[2])  # 加载图片
    image_tensor = myTransforms(img).float()
    image_tensor = image_tensor.unsqueeze_(0)
    input = Variable(image_tensor)
    input = input
    output = model(input)
    # 选择最大概率结果
    index = output.data.cpu().numpy().argmax()
    print(classes[index])
