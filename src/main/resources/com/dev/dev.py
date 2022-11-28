# 引入头文件
import cv2
import torch
from torch import nn
from torch.nn import functional as F
from torch import optim

import torchvision
from matplotlib import pyplot as plt
from torch.utils.data import DataLoader
import numpy as np

# 加载数据
batch_size = 64
train_dataset = torchvision.datasets.MNIST("./root", train=True, download=True,
                                           transform=torchvision.transforms.Compose(
                                               [torchvision.transforms.ToTensor(), torchvision.transforms.Normalize(
                                                   (0.1307), (0.3081))]))
train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)

test_dataset = torchvision.datasets.MNIST("./root", train=False, download=True,
                                          transform=torchvision.transforms.Compose([torchvision.transforms.ToTensor(),
                                                                                    torchvision.transforms.Normalize(
                                                                                        (0.1307), (0.3081))]))
test_loader = DataLoader(test_dataset, batch_size=batch_size, shuffle=False)


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


# super args
EPOCH = 10

device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
model = net()
model.to(device)
criterion = torch.nn.CrossEntropyLoss().to(device)
optimizer = optim.SGD(model.parameters(), lr=0.01, momentum=0.5)


def train():
    acc_array = []
    for epoch in range(EPOCH):
        # 正确率
        for batch_index, (inputs, labels) in enumerate(train_loader):
            inputs = inputs.view(inputs.size(0), 28 * 28).to(device)
            labels = labels.to(device)
            output = model(inputs)
            output = output.to(device)
            loss = criterion(output, target=labels)
            loss.backward()

            optimizer.step()
            optimizer.zero_grad()
            if batch_index % 50 == 0:
                print(loss)
            # loss = F.mse_loss(output, labels)

        total_correct = 0
        for batch_index, (inputs, labels) in enumerate(test_loader):
            inputs = inputs.view(inputs.size(0), 28 * 28).to(device)
            labels = labels.to(device)
            output = model(inputs)
            output = output.to(device)
            pred = output.argmax(dim=1).to(device)
            correct = pred.eq(labels).sum().float().item()
            total_correct += correct

        total_num = len(test_loader.dataset)
        acc = total_correct / total_num
        acc_array.append(acc)
        print("acc:" + str(acc))

    print(range(EPOCH), acc_array)
    plt.plot(np.array(range(EPOCH)), np.array(acc_array))
    plt.show()

    # 保存
    torch.save(model, "model/model.pkl")
    print("模型保存完成")


if __name__ == "__main__":
    train()
