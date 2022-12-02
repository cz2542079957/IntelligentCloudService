import cv2
from torch import nn
import torch
import numpy as np
from torch.nn import functional as F
import sys


def numpy_conv(inputs, filter):
    L = inputs.shape[0]
    filter_size = filter.shape[0]
    result = np.zeros((L - filter_size + 1, L - filter_size + 1))
    for r in range(0, L - filter_size + 1):
        for c in range(0, L - filter_size + 1):
            # 池化大小的输入区域
            cur_input = inputs[r:r + filter_size,
                        c:c + filter_size]
            # 和核进行乘法计算
            cur_output = cur_input * filter
            # 再把所有值求和
            conv_sum = np.sum(cur_output)
            # 当前点输出值
            result[r, c] = conv_sum
    return result


img = cv2.imread(sys.argv[1])
img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
img = cv2.resize(img, (28, 28))

kernel = [[1, 1.2, 1], [1, 1.2, 1], [1, 1.2, 1]]
kernel = np.array(kernel)
output = numpy_conv(img, kernel)
output = numpy_conv(output, kernel)
output = numpy_conv(output, kernel)
output = numpy_conv(output, kernel)
res = ""
for i in range(4):
    for j in range(4):
        res += str(output[i * 6][j * 6])[:5]
# 返回80位的解码结果
print(res)
