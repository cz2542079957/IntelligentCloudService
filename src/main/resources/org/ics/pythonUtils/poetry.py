import os
import sys
import numpy as np
import torch as t
from torch import nn
import io

"""从二进制文件中获取诗歌数据"""


def get_data(opt):  # 读取二进制numpy文件
    if os.path.exists(opt.pickle_path):  # 判断路径是否存在，即tang.npz这个文件是否存在（npz为二进制文件）
        data = np.load(opt.pickle_path, allow_pickle=True)  # 读取tang.npz中的数据存到data中
        data, word2ix, ix2word = data['data'], data['word2ix'].item(), data['ix2word'].item()  # 分类获取数据
        return data, word2ix, ix2word


class PoetryModel(nn.Module):
    def __init__(self, vocab_size, embedding_dim, hidden_dim):
        super(PoetryModel, self).__init__()
        self.hidden_dim = hidden_dim
        self.embeddings = nn.Embedding(vocab_size, embedding_dim)  # 权重矩阵为vocab_size*embedding_dim，即8293*128
        self.lstm = nn.LSTM(embedding_dim, self.hidden_dim, num_layers=2)  # 第三个参数为网络层数
        self.linear1 = nn.Linear(self.hidden_dim, vocab_size)  # 全连接层，生成一个词汇表，词汇表的数值是概率，代表这句话下一个位置这个单词出现的概率

    def forward(self, input, hidden=None):
        seq_len, batch_size = input.size()  # input是一个一个的数字，seq_len表示每一句话多长（有多少单词），batcg_size表示一次处理几个句子
        if hidden is None:  # 一开始设置h_0和c_0都为0
            h_0 = input.data.new(2, batch_size, self.hidden_dim).fill_(
                0).float()  # 隐藏元，维度为（2*batch_size*hidden_dim），全部填充为0，这里的shape为(2,1,256)
            c_0 = input.data.new(2, batch_size, self.hidden_dim).fill_(0).float()
        else:
            h_0, c_0 = hidden
        embeds = self.embeddings(input)  # (1,1,128)
        output, hidden = self.lstm(embeds, (h_0, c_0))  # output为(1,1,256),hidden中包含了同为(1,1,256)的h_0和c_0
        output = self.linear1(output.view(seq_len * batch_size, -1))  # 全连接后变成(1,8293)
        return output, hidden


class Config(object):
    data_path = 'data/'  # 诗歌的文本文件存放路径
    pickle_path = 'tang.npz'  # 预处理好的二进制文件,包含data，形状为(57580,125)，共57580首诗歌，每首诗歌长度125，不够补空格，多余丢弃
    category = 'poet.tang'  # 类别，唐诗还是宋诗歌(poet.song)
    max_gen_len = 200  # 生成诗歌最长长度
    prefix_words = '细雨鱼儿出,微风燕子斜。'  # 不是诗歌的组成部分，用来控制生成诗歌的意境
    start_words = '闲云潭影日悠悠'  # 诗歌开始
    acrostic = True  # 是否是藏头诗
    model_path = None  # 预训练模型路径


opt = Config()


def generate(model, start_words, ix2word, word2ix, prefix_words=None):
    results = list(start_words)
    start_word_len = len(start_words)
    input = t.Tensor([word2ix['<START>']]).view(1, 1).long()  # 手动设置第一个词为<START>
    hidden = None
    if prefix_words:  # 意境诗句存在，这里主要用于训练记忆hidden，生成的output无用
        for word in prefix_words:
            output, hidden = model(input, hidden)
            input = input.data.new([word2ix[word]]).view(1, 1)
    for i in range(opt.max_gen_len):  # 最大的句子长度
        output, hidden = model(input, hidden)  # 前边几个output无用，因为默认使用前缀诗句
        if i < start_word_len:  # 如果小于前缀句子的长度
            w = results[i]  # 取前缀对应位置的字
            input = input.data.new([word2ix[w]]).view(1, 1)  # 取出前缀诗句对应的字的下标，形成1*1的矩阵
        else:  # 已经输出完前缀了，该保存output中计算的字了
            top_index = output.data[0].topk(1)[1][0].item()  # 保存output中概率最大的那个字的下标
            w = ix2word[top_index]  # 取出这个下标对应的字
            results.append(w)  # 生成的诗句序列加入刚生成的结果
            input = input.data.new([top_index]).view(1, 1)  # 把当前字的下标扩充成1*1当作下一次的输入
        if w == '<EOP>':  # 如果预测到了结束，删掉并退出
            del results[-1]
            break
    return results


def gen_acrostic(model, start_words, ix2word, word2ix, prefix_words=None):
    results = []  # 用于存储生成的诗歌
    start_word_len = len(start_words)  # 用于生成藏头诗的诗句的长度（有几个字生成几句）
    input = (t.Tensor([word2ix['<START>']]).view(1, 1).long())  # word2ix['<START>']=8291，一开始input先赋值<START>的序号
    hidden = None
    index = 0  # 用来指示已经生成了多少句藏头诗
    pre_word = '<START>'  # 用来表示上一个词，第一个词设置为'<START>'
    if prefix_words:  # 如果设置了意境诗句，其中逗号和句号也包括在内
        for word in prefix_words:  # 遍历这句诗的每一个字
            output, hidden = model(input,
                                   hidden)  # input是1*1矩阵，只包含上一个字，用于输进去进行降维，和一个空的hidden记忆，输出关于当前字的记忆hidden和当前的输出output，这个output貌似没用
            input = (input.data.new([word2ix[word]])).view(1,
                                                           1)  # 将当前遍历的字的编号取出形成一个1*1矩阵赋值给input，input.data.new([word2ix[word]])是将一个数字变成一维tensor，view(1,1)是变成2维tensor
    for i in range(opt.max_gen_len):  # 生成诗歌最长长度
        output, hidden = model(input, hidden)  # 对之前意境的最后一个字进行处理，这里其实是句号,output代表对下一个字的预测概率
        top_index = output.data[0].topk(1)[1][0].item()  # topk(1)取出向量中一个最大值，返回概率和对应下标
        w = ix2word[top_index]  # 获取几率最大的下标对应的字
        if (pre_word in {'。', '！', '<START>'}):  # 如果遇到句号叹号或者开始符号，藏头的词送进去生成
            if index == start_word_len:  # index用来指示已经生成了多少句藏头诗，如果相等则代表已经生成完了，退出循环
                break
            else:  # 把藏头的词作为输入送入模型
                w = start_words[index]  # 取藏头诗中第index个下标对应的字（这里如果是每一句开头，会抛弃上一句最后取出来的最大概率的w，重新覆盖了）
                index += 1
                input = (input.data.new([word2ix[w]])).view(1, 1)  # 取对应字的数字编号作为一个1*1矩阵，作为下一个词的输入
        else:  # 否则的话，把上一次预测是词作为下一个词输入
            input = (input.data.new([word2ix[w]])).view(1, 1)
        results.append(w)  # 结果诗句添加当前字
        pre_word = w  # 将w赋值给变量作为前一个单词
    return results


def gen(kwargs):
    for k, v in kwargs.items():  # 遍历出来的k是键，v是值
        setattr(opt, k, v)  # 设置属性值，第一个参数是对象，第二个参数是属性，第三个参数是属性值
    data, word2ix, ix2word = get_data(opt)  # 第一个参数每一行是一首诗对应的字的下标，第二个参数是每个字对应的序号，第三个参数是每个序号对应的字
    model = PoetryModel(len(word2ix), 128, 256)
    map_location = lambda s, l: s  # 这三句貌似是将模型加载在GPU上
    state_dict = t.load(opt.model_path, map_location=map_location)
    model.load_state_dict(state_dict)

    gen_poetry = gen_acrostic if opt.acrostic else generate  # 是不是藏头诗，gen_acrostic为将提示句拆成每句第一个字生成藏头诗，generate为将提示句作为第一句
    result = gen_poetry(model, opt.start_words, ix2word, word2ix,
                        opt.prefix_words)  # ix2word为每个序号对应的字，word2ix为每个字对应的序号，prefix_words为师哥意境
    print(''.join(result))


def test():
    gen({'model_path': sys.argv[1], 'pickle_path': sys.argv[2], 'start_words': sys.argv[3],
         'prefix_words': sys.argv[4], 'acrostic': True, 'nouse_gpu': False})


# 开始测试

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
test()
