import matplotlib.pyplot as plt

def lineplot(x_data, y_data, y2_data, y3_data, y4_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()

    ax.plot(x_data, y_data, '-.bo', lw=3, label=('Среднее количество переходов пользователя fix1'))
    ax.plot(x_data, y2_data, '-.go', lw=3, label=('Среднее количество переносов задачи fix1'))
    ax.plot(x_data, y3_data, ':mo', lw=3, label=('Среднее количество переходов пользователя exp1'))
    ax.plot(x_data, y4_data, ':ro', lw=3, label=('Среднее количество переносов задачи exp1'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)


def main():
    data = []
    with open("/Users/da.vasilyev/Desktop/Projects/Model/AverageNumberOfTransfersFromLambaIn.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    print(data)
    dataX = []
    dataY = []
    dataY2 = []
    dataY3 = []
    dataY4 = []

    for line in data:
        dataX.append(line.pop(0))
        dataY.append(line.pop(0))
        dataY2.append(line.pop(0))
        dataY3.append(line.pop(0))
        dataY4.append(line.pop(0))

    print(dataX)
    print(dataY)

    lineplot(dataX, dataY, dataY2, dataY3, dataY4, "Входная интенсивность, " + chr(955), "Среднее кол-во перемещений, n, шт", "Среднее кол-во переходов от входной интенсивности")
    plt.legend()

    plt.show()


if __name__ == '__main__':
    main()
