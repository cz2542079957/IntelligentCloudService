import cv2
import torch
from dev import net
import numpy as np
from torch.autograd import Variable
from torch.nn import functional as F


def checkBcgColor(args):
    if sum(args) > 128 * 4:
        return True
    else:
        return False


if __name__ == "__main__":
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    model = torch.load('./model/model.pkl')  # 加载模型
    model = model.to(device)
    model.eval()  # 把模型转为test模式

    img = cv2.imread('./data/img_16.png', 0)  # 以灰度图的方式读取要预测的图片
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

    img = torch.tensor(img).to(device)
    img = img.view(1, 28 * 28)
    output = model(img)
    print(output)
    prob = F.softmax(output, dim=1)
    prob = Variable(prob)
    prob = prob.cpu().numpy()
    print(np.argmax(prob))
