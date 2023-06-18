import matplotlib.pyplot as plt

def lineplotMD(x_data, y1_data, y2_data, y3_data, y4_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
#    ax.plot(x_data, y1_data, '--go', lw=2, label=('Формула для пакетной системы'))
    ax.plot(x_data, y2_data, '--bo', lw=2, label=('Экспоненциальное распределение с 1'))
    ax.plot(x_data, y3_data, '--ro', lw=2, label=('Фиксированное значение 1'))
    ax.plot(x_data, y4_data, '--mo', lw=2, label=('Равномерное распределение с 1'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)

def main():
    data = []
    with open("/Users/da.vasilyev/Desktop/Projects/Model/DForDifferentDistribution.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    print(data)
    dataX = []
    dataY1 = []
    dataY2 = []
    dataY3 = []
    dataY4 = []

    for line in data:
        dataX.append(line.pop(0))
        dataY1.append(line.pop(0))
        dataY2.append(line.pop(0))
        dataY3.append(line.pop(0))
        dataY4.append(line.pop(0))

    print(dataX)
    print(dataY1)
    print(dataY2)
    print(dataY3)
    print(dataY4)

    lineplotMD(dataX, dataY1, dataY2, dataY3, dataY4, "Входная интенсивность, " + chr(955), "Средняя задержка, D, квант", "Среднее значение задержки от входной интенсивности\nдля различных законов распределения")
    plt.ylim(bottom=0)
    plt.ylim(top=11)
    plt.legend()

    plt.show()

if __name__ == '__main__':
    main()