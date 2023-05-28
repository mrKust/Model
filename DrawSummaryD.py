import matplotlib.pyplot as plt

def lineplotMD(x_data, y_data, y2_data, y3_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    ax.plot(x_data, y2_data, 'g', lw=4, label=('С доп временем'))
    ax.plot(x_data, y_data, 'b--', lw=4, label=('Без доп времени'))
    ax.plot(x_data, y3_data, 'r--', lw=4, label=('Без доп времени + время на трансферы'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)


def main():
    data = []
    with open("/Users/da.vasilyev/Desktop/Projects/Model/AverageNumberWorkTransfers.txt") as f:
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

    lineplotMD(dataX, dataY1, dataY2, dataY3, chr(955), "D(" + chr(955) + ")", "D(" + chr(955) + ")")
    plt.legend()
    plt.legend()

    plt.show()


if __name__ == '__main__':
    main()
