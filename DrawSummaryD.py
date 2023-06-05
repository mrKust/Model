import matplotlib.pyplot as plt

def lineplotMD(x_data, y_data, y2_data, y3_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    ax.plot(x_data, y2_data, '-go', lw=3, label=('С доп временем'))
    ax.plot(x_data, y_data, '--bo', lw=3, label=('Без доп времени'))
    ax.plot(x_data, y3_data, ':ro', lw=3, label=('Без доп времени + время на трансферы'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)


def main():
    data = []
    with open("D:\Pereezd\Labs\Научка\Model\AverageNumberWorkTransfers.txt") as f:
        num = 0
        for line in f:
            if num == 0:
                num += 1
                continue
            data.append([float(x) for x in line.split()])

    print(data)
    dataX = []
    dataY1 = []
    dataY2 = []
    dataY3 = []


    for line in data:
        dataX.append(line.pop(0))
        dataY1.append(float(line.pop(0)))
        dataY2.append(float(line.pop(0)))
        dataY3.append(float(line.pop(0)) + dataY1[-1])

    print(dataX)
    print(dataY1)
    print(dataY2)
    print(dataY3)

    lineplotMD(dataX, dataY1, dataY2, dataY3, "Входная интенсивность, " + chr(955), "Средняя задержка, D, квант)", "Сравнение значение средней задержки для случая с дополнительным\nвременем на перенос задачи и без времени на перенос")
    plt.legend()
    plt.legend()

    plt.show()


if __name__ == '__main__':
    main()
