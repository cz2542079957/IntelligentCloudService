# 引入库
import torch
import torchvision
from torch.utils.data import DataLoader
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import matplotlib.pyplot as plt
from PIL import Image
import matplotlib.image as image
import cv2
import time
import os
import sys


# 4 层 卷积神网络
class CNNModel(nn.Module):
    def __init__(self):
        super(CNNModel, self).__init__()

        # Convolution layer 1
        self.conv1 = nn.Conv2d(in_channels=1, out_channels=32, kernel_size=5, stride=1, padding=0)
        self.relu1 = nn.ReLU()
        self.batch1 = nn.BatchNorm2d(32)

        self.conv2 = nn.Conv2d(in_channels=32, out_channels=32, kernel_size=5, stride=1, padding=0)
        self.relu2 = nn.ReLU()
        self.batch2 = nn.BatchNorm2d(32)
        self.maxpool1 = nn.MaxPool2d(kernel_size=2, stride=2)
        self.conv1_drop = nn.Dropout(0.25)

        # Convolution layer 2
        self.conv3 = nn.Conv2d(in_channels=32, out_channels=64, kernel_size=3, stride=1, padding=0)
        self.relu3 = nn.ReLU()
        self.batch3 = nn.BatchNorm2d(64)

        self.conv4 = nn.Conv2d(in_channels=64, out_channels=64, kernel_size=3, stride=1, padding=0)
        self.relu4 = nn.ReLU()
        self.batch4 = nn.BatchNorm2d(64)
        self.maxpool2 = nn.MaxPool2d(kernel_size=2, stride=2)
        self.conv2_drop = nn.Dropout(0.25)

        # Fully-Connected layer 1

        self.fc1 = nn.Linear(576, 256)
        self.fc1_relu = nn.ReLU()
        self.dp1 = nn.Dropout(0.5)

        # Fully-Connected layer 2
        self.fc2 = nn.Linear(256, 10)

    def forward(self, x):
        # conv layer 1 的前向计算，3行代码
        out = self.conv1(x)
        out = self.relu1(out)
        out = self.batch1(out)

        out = self.conv2(out)
        out = self.relu2(out)
        out = self.batch2(out)

        out = self.maxpool1(out)
        out = self.conv1_drop(out)

        # conv layer 2 的前向计算，4行代码
        out = self.conv3(out)
        out = self.relu3(out)
        out = self.batch3(out)

        out = self.conv4(out)
        out = self.relu4(out)
        out = self.batch4(out)

        out = self.maxpool2(out)
        out = self.conv2_drop(out)

        # Flatten拉平操作
        out = out.view(out.size(0), -1)

        # FC layer的前向计算（2行代码）
        out = self.fc1(out)
        out = self.fc1_relu(out)
        out = self.dp1(out)

        out = self.fc2(out)

        return F.log_softmax(out, dim=1)


# 加载模型
model_path = sys.argv[1]
network = torch.jit.load(model_path)
network.eval()


# 图片处理
def imageProcess(img):
    # 处理图片
    data_transform = torchvision.transforms.Compose(
        [torchvision.transforms.Resize(32),  # 边长转换为 32
         torchvision.transforms.CenterCrop(28),
         torchvision.transforms.ToTensor(),
         torchvision.transforms.Normalize((0.1307,), (0.3081,))])

    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)  # 灰度处理
    retval, dst = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)  # 二值化
    fanse = cv2.bitwise_not(dst)  # 黑白反转

    # 将BGR图像转变成RGB图像：即将cv2.imread转换成Image.open
    imgs = Image.fromarray(cv2.cvtColor(fanse, cv2.COLOR_BGR2RGB))
    imgs = imgs.convert('L')  # 将三通道图像转换成单通道灰度图像
    imgs = data_transform(imgs)  # 处理图像
    return imgs


# 预测单张图片
path = sys.argv[2]
with torch.no_grad():
    img = cv2.imread(path)  # 预测图片
    imgs = imageProcess(img)
    if imgs.shape == torch.Size([1, 28, 28]):
        imgs = torch.unsqueeze(imgs, dim=0)  # 在最前面增加一个维度
    output = network(imgs)
    print(output.data.max(dim=1, keepdim=True)[1].item())
