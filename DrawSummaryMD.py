import csv

import matplotlib.pyplot as plt

def lineplotSummaryMD(x_data, y1_data, y2_data, y3_data, y4_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    ax.plot(x_data, y1_data, 'g', lw=4, label=('Теория'))
    ax.plot(x_data, y2_data, 'b--', lw=4, label=('Фиксированная 1'))
    ax.plot(x_data, y3_data, 'r--', lw=4, label=('exp(1)'))
    ax.plot(x_data, y4_data, 'k--', lw=4, label=('exp(1/2)+exp(1/2)'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)


def main():

    dataLambdaIn = []
    dataDTheor = []
    dataDFix = []
    dataDExp1 = []
    dataDExp05 = []

    data = []
    with open("D:\Pereezd\Labs\Научка\Model\sizeExp1\model.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    dataX = []
    for line in data:
        dataX.append(line.pop(0))
        line.pop(0)
        line.pop(0)
        line.pop(0)
        if (dataX[-1] < 1.0):
            dataDExp1.append(line.pop(0))
            line.pop(0)

    data = []
    with open("D:\Pereezd\Labs\Научка\Model\sizeExp0505\model.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    dataX = []
    for line in data:
        dataX.append(line.pop(0))
        line.pop(0)
        line.pop(0)
        line.pop(0)
        if (dataX[-1] < 1.0):
            dataDExp05.append(line.pop(0))
            line.pop(0)

    data = []
    with open("D:\Pereezd\Labs\Научка\Model\sizeFix\model.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    dataX = []
    for line in data:
        dataX.append(line.pop(0))
        line.pop(0)
        line.pop(0)
        line.pop(0)
        if (dataX[-1] < 1.0):
            dataDFix.append(line.pop(0))
            line.pop(0)

    data = []
    with open("D:\Pereezd\Labs\Научка\Model\sizeTheor\model.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    dataX = []
    for line in data:
        dataX.append(line.pop(0))
        line.pop(0)
        line.pop(0)
        line.pop(0)
        if (dataX[-1] < 1.0):
            dataDTheor.append(line.pop(0))
            line.pop(0)

    lineplotSummaryMD(dataLambdaIn, dataDTheor, dataDFix, dataDExp1, dataDExp05, chr(955), "D(" + chr(955) + ")",
                      "D(" + chr(955) + ")")
    plt.legend()
    plt.show()


if __name__ == '__main__':
    main()
