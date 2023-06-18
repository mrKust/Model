import matplotlib.pyplot as plt

def lineplot(x_data, y_data, y2_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()

    ax.plot(x_data, y_data, '--bo', lw=3, label=('Экспоненциальное распределение с 1)'))
#    ax.plot(x_data, y_data, '--bo', lw=3, label=('With Transfer time'))
    ax.plot(x_data, y2_data, '--go', lw=3, label=('Фиксированное значение 1)'))
#    ax.plot(x_data, y2_data, '--go', lw=3, label=('Without Transfer time'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)


def main():
    data = []
    with open("/Users/da.vasilyev/Desktop/Projects/Model/tmp.txt") as f:
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

    lineplot(dataX, dataY, dataY2, "Входная интенсивность, " + chr(955), "Средняя задержка, D, квант", "Средняя задержка в зависимости от закона формирования задачи")
    plt.legend()

    plt.show()


if __name__ == '__main__':
    main()
