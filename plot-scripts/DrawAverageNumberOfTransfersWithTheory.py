import matplotlib.pyplot as plt

def lineplotMD(x_data, y_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    y2_data = []
    a = 0.8
    for x in x_data:
#        y2_data.append(pow(0.5, x+1))
        if (x == 0):
            y2_data.append(1 - 0.5*a)
        else:
            y2_data.append(pow(0.5*a, x)*(1 - 0.5*a))

    print(y2_data)
    ax.plot(x_data, y2_data, '-go', lw=3, label=('Теоретические значения'))
    ax.plot(x_data, y_data, '--bo', lw=3, label=('Практические значения'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)


def main():
    data = []
    with open("../data/AverageNumberOfWorkTransfersWithTheory.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    print(data)
    dataX = []
    dataY = []

    for line in data:
        dataX.append(line.pop(0))
        dataY.append(line.pop(0))

    print(dataX)
    print(dataY)

    lineplotMD(dataX, dataY, "Кол-во перемещений, n, раз", "Вероятность перемещения задачи, Pr{n}", "Вероятность, что задача переместиться заданное\nкол-во переходов при вероятности переноса задачи 0.8")
#    lineplotMD(dataX, dataY, "Кол-во перемещений, n, раз", "Вероятность перемещения задачи, Pr{n}", "Вероятность, что пользователь совершит заданное\nкол-во переходов")
    plt.legend()

    plt.show()


if __name__ == '__main__':
    main()
