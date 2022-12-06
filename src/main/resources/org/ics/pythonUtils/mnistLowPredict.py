import cv2
import torch
import numpy as np
from torch.autograd import Variable
from torch.nn import functional as F
from torch import nn
import sys;


class net(nn.Module):
    def __init__(self):
        super(net, self).__init__()
        self.fc1 = nn.Linear(28 * 28, 512)
        self.fc2 = nn.Linear(512, 256)
        self.fc3 = nn.Linear(256, 128)
        self.fc4 = nn.Linear(128, 64)
        self.fc5 = nn.Linear(64, 10)

    def forward(self, x):
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = F.relu(self.fc3(x))
        x = F.relu(self.fc4(x))
        x = self.fc5(x)
        return x


def checkBcgColor(args):
    if sum(args) > 128 * 4:
        return True
    else:
        return False


if __name__ == "__main__":
    model = torch.jit.load(sys.argv[1])  # 加载模型
    model.eval()  # 把模型转为test模式

    img = cv2.imread(sys.argv[2], 0)  # 以灰度图的方式读取要预测的图片
    img = cv2.resize(img, (28, 28))

    if checkBcgColor([img[0][0], img[27][0], img[0][27], img[27][27]]):
        for i in range(28):
            for j in range(28):
                img[i][j] = 255 - img[i][j]

    # 显示图像
    # cv2.imshow("img", img)
    # cv2.waitKey(0)

    # 开始预测模型
    img = np.array(img).astype(np.float32)

    img = torch.tensor(img)
    img = img.view(1, 28 * 28)
    output = model(img)
    prob = F.softmax(output, dim=1)
    prob = Variable(prob)
    prob = prob.cpu().numpy()
    print(np.argmax(prob))
