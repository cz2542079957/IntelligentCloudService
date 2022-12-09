import numpy as np
import torch
from PIL import Image
from torch import nn
import math
import sys
import os


class ResidualBlock(nn.Module):
    def __init__(self, channels):
        super(ResidualBlock, self).__init__()
        self.conv1 = nn.Conv2d(channels, channels, kernel_size=3, padding=1)
        self.bn1 = nn.BatchNorm2d(channels)
        self.prelu = nn.PReLU(channels)
        self.conv2 = nn.Conv2d(channels, channels, kernel_size=3, padding=1)
        self.bn2 = nn.BatchNorm2d(channels)

    def forward(self, x):
        short_cut = x
        x = self.conv1(x)
        x = self.bn1(x)
        x = self.prelu(x)

        x = self.conv2(x)
        x = self.bn2(x)

        return x + short_cut


class UpsampleBLock(nn.Module):
    def __init__(self, in_channels, up_scale):
        super(UpsampleBLock, self).__init__()
        self.conv = nn.Conv2d(in_channels, in_channels * up_scale ** 2, kernel_size=3, padding=1)
        self.pixel_shuffle = nn.PixelShuffle(up_scale)
        self.prelu = nn.PReLU(in_channels)

    def forward(self, x):
        x = self.conv(x)
        x = self.pixel_shuffle(x)
        x = self.prelu(x)
        return x


class Generator(nn.Module):
    def __init__(self, scale_factor, num_residual=16):
        upsample_block_num = int(math.log(scale_factor, 2))

        super(Generator, self).__init__()

        self.block_in = nn.Sequential(
            nn.Conv2d(3, 64, kernel_size=9, padding=4),
            nn.PReLU(64)
        )

        self.blocks = []
        for _ in range(num_residual):
            self.blocks.append(ResidualBlock(64))
        self.blocks = nn.Sequential(*self.blocks)

        self.block_out = nn.Sequential(
            nn.Conv2d(64, 64, kernel_size=3, padding=1),
            nn.BatchNorm2d(64)
        )

        self.upsample = [UpsampleBLock(64, 2) for _ in range(upsample_block_num)]
        self.upsample.append(nn.Conv2d(64, 3, kernel_size=9, padding=4))
        self.upsample = nn.Sequential(*self.upsample)

    def forward(self, x):
        x = self.block_in(x)
        short_cut = x
        x = self.blocks(x)
        x = self.block_out(x)

        upsample = self.upsample(x + short_cut)
        return torch.tanh(upsample)


def cvtColor(image):
    if len(np.shape(image)) == 3 and np.shape(image)[2] == 3:
        return image
    else:
        image = image.convert('RGB')
        return image


def preprocess_input(x):
    x /= 255
    x -= 0.5
    x /= 0.5
    return x


def postprocess_output(x):
    x *= 0.5
    x += 0.5
    x *= 255
    return x


class SRGAN(object):
    def __init__(self, model_path, scale_factor=4):
        self.__dict__.update()
        self.model_path = model_path
        self.scale_factor = scale_factor
        self.generate()

    def generate(self):
        self.net = Generator(self.scale_factor)
        self.net.load_state_dict(torch.load(self.model_path))
        self.net.eval()
        print('{} model, and classes loaded.'.format(self.model_path))

    def detect_image(self, image):
        image = cvtColor(image)
        image_data = np.expand_dims(np.transpose(preprocess_input(np.array(image, dtype='float32')), [2, 0, 1]), 0)

        with torch.no_grad():
            image_data = torch.from_numpy(image_data).type(torch.FloatTensor)
            hr_image = self.net(image_data)[0]
            hr_image = hr_image.cpu().data.numpy().transpose(1, 2, 0)
            hr_image = postprocess_output(hr_image)

        hr_image = Image.fromarray(np.uint8(hr_image))
        return hr_image



if __name__ == "__main__":
    model_path = sys.argv[1]
    img_path = sys.argv[2]
    output =  sys.argv[3]
    srgan = SRGAN(model_path)
    image = Image.open(img_path)
    r_image = srgan.detect_image(image)
    r_image.save(output)
    print(output)

