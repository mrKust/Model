import matplotlib.pyplot as plt

def lineplotLambda(x_data, y1_data, y2_data, y3_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    ax.plot(x_data, x_data, 'g', lw=3, label=('Входной поток'))
    ax.plot(x2_data, y1_data, 'b-.', lw=3, label=('При a = 0.2'))
    ax.plot(x2_data, y2_data, 'r-.', lw=3, label=('При a = 0.5'))
    ax.plot(x2_data, y3_data, 'g-.', lw=3, label=('При a = 0.8'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)

def lineplotMD(x_data, y1_data, y2_data, y3_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    x2_data = []
    for x in x_data:
        if (x <= 1) :
            x2_data.append(x)
    ax.plot(x2_data, y1_data, 'b-.', lw=3, label=('При a = 0.2'))
    ax.plot(x2_data, y2_data, 'r-.', lw=3, label=('При a = 0.5'))
    ax.plot(x2_data, y3_data, 'g-.', lw=3, label=('При a = 0.8'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)

def main():
    data = []
    with open("/Users/da.vasilyev/Desktop/Projects/Model/AverageParamsFromA.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    print(data)
    dataX = []
    dataY11 = []
    dataY12 = []
    dataY21 = []
    dataY22 = []
    dataY31 = []
    dataY32 = []

    for line in data:
        dataX.append(line.pop(0))
        dataY11.append(line.pop(0))
        dataY21.append(line.pop(0))
        dataY31.append(line.pop(0))
        if (dataX[-1] < 1.0):
            dataY12.append(line.pop(0))
            dataY22.append(line.pop(0))
            dataY32.append(line.pop(0))

    lineplotMD(dataX, dataY12, dataY22, dataY32, "Входная интенсивность, " + chr(955), "Средняя задержка, D, квант", "Среднее значение задержки от входной интенсивности")
    plt.legend()
    lineplotLambda(dataX, dataY11, dataY21, dataY31, "Входная интенсивность, " + chr(955), "Выходная интенсивность, " + chr(955), "Среднее значение выходной интенсивности от входной интенсивности")
    plt.legend()

    plt.show()

if __name__ == '__main__':
    main()