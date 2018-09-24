import requests, re, xlwt


# 获取页面
def getHtml(url):
    try:
        r = requests.get(url, timeout=30)
        r.raise_for_status()
        r.encoding = r.apparent_encoding
        print("请求服务器成功！\n")
        return r.text
    except:
        print("请求失败")


# 解析页面 正则表达式提取信息
def parsePage(ilt, html):
    try:
        plt = re.findall(r'\"view_price\"\:\"[\d\.]*\"', html)
        tlt = re.findall(r'\"raw_title\"\:\".*?\"', html)
        detail_url = re.findall(r'\"detail_url\"\:\".*?\"', html)
        print("解析成功!\n")
        for i in range(len(plt)):
            price = eval(plt[i].split(':')[1])
            title = eval(tlt[i].split(':')[1])
            detail_url = eval(detail_url[i].split(':')[1])
            ilt.append([price, title, detail_url])
    except:
        print("解析失败!\n")


# 写入Excel
def Write_Excel(ilt):
    print("正在写入Exel表格....")
    # 创建工作簿指定编码
    file = xlwt.Workbook(encoding='utf-8')
    # 创建表
    table = file.add_sheet("淘宝商品信息")
    count = 0
    # print(tplt.format("序号","价格","商品名称"))
    value = ["序号", "价格", "商品名称", "detail_url"]
    for i in range(len(value)):
        table.write(count, i + 1, value[i])
    for g in ilt:
        count += 1
        value = [count, float(g[0]), g[1][0:10], g[2]]
        for j in range(4):
            table.write(count, j + 1, value[j])
        # Write_Excel(count,value=[g[0],g[1]],f=file)
    file.save("淘宝商品信息.xls")
    print("写入成功！")
    # print(tplt.format(count,g[0],g[1]))


# 把数据写入Excel表

def main():
    goods = input("请输入需要爬取的淘宝商品名称：")
    # 爬取的页面深度
    depth = int(input("请输入爬取的页面数量（建议不超过5页）："))
    start_url = "https://s.taobao.com/search?q=" + goods
    inforList = []
    for i in range(depth):
        try:
            # 翻页，起始页面s，在a起始页面上进行翻页
            url = start_url + '&s=' + str(44 * i)
            print("正在请求服务器商品链接第%d页，请稍等......" % (i + 1))
            html = getHtml(url)
            print("正在解析第%d页，请稍等......" % (i + 1))
            parsePage(inforList, html)
        except:
            continue
    Write_Excel(inforList)
    # printGoodsList(inforList)


main()
