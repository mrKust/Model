import matplotlib.pyplot as plt

def lineplotLambda(x_data, y1_data, y2_data, y3_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    ax.plot(x_data, x_data, '--go', lw=2, label=('Входной поток'))
    ax.plot(x_data, y1_data, '--bo', lw=2, label=('При a = 0.2'))
    ax.plot(x_data, y2_data, '--ro', lw=2, label=('При a = 0.5'))
    ax.plot(x_data, y3_data, '--mo', lw=2, label=('При a = 0.8'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)

def lineplotMD(x_data, y1_data, y2_data, y3_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    x_data = x_data[:len(x_data) - 2]
    y1_data = y1_data[:len(y1_data) - 2]
    y2_data = y2_data[:len(y2_data) - 2]
    y3_data = y3_data[:len(y3_data) - 2]
    ax.plot(x_data, y1_data, '--bo', lw=2, label=('При a = 0.2'))
    ax.plot(x_data, y2_data, '--ro', lw=2, label=('При a = 0.5'))
    ax.plot(x_data, y3_data, '--mo', lw=2, label=('При a = 0.8'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)

def main():
    data = []
    with open("../data/AverageParamsFromA.txt") as f:
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
        dataY12.append(line.pop(0))
        dataY21.append(line.pop(0))
        dataY22.append(line.pop(0))
        dataY31.append(line.pop(0))
        dataY32.append(line.pop(0))

    lineplotMD(dataX, dataY12, dataY22, dataY32, "Входная интенсивность, " + chr(955), "Средняя задержка, D, квант", "Среднее значение задержки от входной интенсивности")
    plt.legend()
    lineplotLambda(dataX, dataY11, dataY21, dataY31, "Входная интенсивность, " + chr(955), "Выходная интенсивность, " + chr(955), "Среднее значение выходной интенсивности от входной интенсивности")
    plt.legend()

    plt.show()

if __name__ == '__main__':
    main()