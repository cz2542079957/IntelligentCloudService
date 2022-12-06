import heapq
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

classes = {0: "苹果", 1: "观赏鱼", 2: "宝宝", 3: "熊", 4: "海狸", 5: "床", 6: "蜜蜂", 7: "甲虫",
           8: "自行车", 9: "瓶子", 10: "碗", 11: "男孩", 12: "桥", 13: "公共汽车", 14: "蝴蝶", 15: "骆驼",
           16: "罐头", 17: "城堡", 18: "毛毛虫", 19: "牛", 20: "椅子", 21: "黑猩猩", 22: "闹钟",
           23: "云", 24: "蟑螂", 25: "沙发", 26: "螃蟹", 27: "鳄鱼", 28: "杯子", 29: "恐龙",
           30: "海豚", 31: "大象", 32: "比目鱼", 33: "森林", 34: "狐狸", 35: "女孩", 36: "仓鼠",
           37: "房子", 38: "袋鼠", 39: "键盘", 40: "灯", 41: "割草机", 42: "豹", 43: "狮子",
           44: "蜥蜴", 45: "龙虾", 46: "人", 47: "枫树", 48: "摩托车", 49: "山", 50: "老鼠",
           51: '蘑菇', 52: '橡树', 53: '橘子', 54: '兰花', 55: '水獭', 56: '棕榈树', 57: '梨',
           58: '小货车', 59: '松树', 60: '平原', 61: '盘子', 62: '罂粟', 63: '豪猪', 64: '鼠貂',
           65: "兔子", 66: "浣熊", 67: "雷", 68: "道路", 69: "火箭", 70: "玫瑰", 71: "海", 72: "密封",
           73: "鲨鱼", 74: "鼩鼱", 75: "臭鼬", 76: "摩天大楼", 77: "蜗牛", 78: "蛇", 79: "蜘蛛",
           80: "松鼠", 81: "有轨电车", 82: "向日葵", 83: "甜椒", 84: "桌子", 85: "坦克",
           86: "电话", 87: "电视", 88: "老虎", 89: "拖拉机", 90: "火车", 91: "鳟鱼", 92: "郁金香",
           93: "乌龟", 94: "衣柜", 95: "鲸鱼", 96: "柳树", 97: "狼", 98: "女人", 99: "虫子"}

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
if __name__ == "__main__":
    model = torch.load(sys.argv[1])  # 加载模型
    model.eval()  # 把模型转为test模式

    # 加载自己的图片数据集
    img = cv2.imread(sys.argv[2])  # 加载图片
    image_tensor = myTransforms(img).float()
    image_tensor = image_tensor.unsqueeze_(0)
    input = Variable(image_tensor)
    input = input
    output = model(input)
    print(" > ".join(list(map(lambda x: classes[x], output[0].data.cpu().numpy().argsort()[:-4:-1]))))
