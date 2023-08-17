import matplotlib.pyplot as plt

def lineplotMD(x_data, y1_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    ax.plot(x_data, y1_data, '--bo', lw=2, label=('При lambdaIn = 0.3'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)

def main():
    data = []
    with open("/Users/da.vasilyev/Desktop/Projects/Model/AverageDFromA.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    print(data)
    dataX = []
    dataY11 = []

    for line in data:
        dataX.append(line.pop(0))
        dataY11.append(line.pop(0))

    lineplotMD(dataX, dataY11, "Вероятность переноса, a", "Средняя задержка, D, квант", "Среднее значение задержки от вероятности переноса")
    plt.legend()

    plt.show()

if __name__ == '__main__':
    main()