import matplotlib.pyplot as plt

def lineplot(x_data, y_data, y2_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()

    ax.plot(x_data, y2_data, 'g', lw=3, label=('Среднее количество переносов задачи'))
    ax.plot(x_data, y_data, 'b--', lw=3, label=('Среднее количество переходов пользователя'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)


def main():
    data = []
    with open("D:\Pereezd\Labs\Научка\Model\AverageNumberOfTransfersFromLambaIn.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    print(data)
    dataX = []
    dataY = []
    dataY2 = []

    for line in data:
        dataX.append(line.pop(0))
        dataY.append(line.pop(0))
        dataY2.append(line.pop(0))

    print(dataX)
    print(dataY)

    lineplot(dataX, dataY, dataY2, "Входная интенсивность, " + chr(955), "Среднее кол-во перемещений, n, шт", "Среднее кол-во переходов от входной интенсивности")
    plt.legend()

    plt.show()


if __name__ == '__main__':
    main()
