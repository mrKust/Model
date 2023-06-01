import matplotlib.pyplot as plt

def lineplotMD(x_data, y_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    y2_data = []
    a = 0.2
    for x in x_data:
        y2_data.append(pow(0.5, x+1))
#        if (x == 0):
#            y2_data.append(1 - 0.5*a)
#        else:
#            y2_data.append(pow(0.5*a, x)*(1 - 0.5*a))

    print(y2_data)
    ax.plot(x_data, y2_data, 'g', lw=4, label=('Теоретические значения'))
    ax.plot(x_data, y_data, 'b--', lw=4, label=('Практические значения'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)


def main():
    data = []
    with open("D:\Pereezd\Labs\Научка\Model\AverageNumberOfWorkTransfersWithTheory.txt") as f:
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

    lineplotMD(dataX, dataY, "Кол-во передач", "Среднее кол-во переходов пользователя", "Среднее кол-во переходов пользователя")
    plt.legend()

    plt.show()


if __name__ == '__main__':
    main()
