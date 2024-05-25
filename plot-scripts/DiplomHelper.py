import matplotlib.pyplot as plt


def lineplot(x_data, y_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    ax.plot(x_data, y_data, 'b', lw=4)
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)


def lineplotLambda(x_data, y_data, y2_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    x_data.pop(0)
    y_data.pop(0)
    ax.plot(x_data, y2_data, '-go', lw=3, label=('Входной поток'))
    ax.plot(x_data, y_data, '--bo', lw=3,label=('Выходной поток'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    
def lineplotMD(x_data, y_data, y2_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    x2_data = []
    for x in x_data:
        if (x <= 1) :
            x2_data.append(x)
    ax.plot(x2_data, y2_data, '-go', lw=3, label=('Теоретические значения'))
    ax.plot(x2_data, y_data, '--bo', lw=3, label=('Практические значения'))
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)

def main():
    data = []
    with open("../modeling-service/model.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    print(data)
    dataX = []
    dataY1 = []
    dataY2 = []
    dataY3 = []
    dataY4 = []
    dataY5 = []
    dataY6 = []
    dataY7 = []

    for line in data:
        dataX.append(line.pop(0))
        dataY1.append(line.pop(0))
        dataY2.append(line.pop(0))
        dataY3.append(line.pop(0))
        dataY4.append(line.pop(0))
        dataY5.append(line.pop(0))
        if (dataX[-1] < 1.0):
            dataY6.append(line.pop(0))
            dataY7.append(line.pop(0))

    print("x") 
    print(dataX)
    print(dataY1)
    print(dataY2)
    print(dataY3)
    print("age of inf") 
    print(dataY4)

#     lineplot(dataX, dataY2, chr(955), "medium length of work", "medium length of work")
#     lineplot(dataX, dataY3, chr(955), "transfersNum / T", "transfers per T")
    lineplotMD(dataX, dataY5, dataY4, "age of information theor", "age of information modeling",
               "medium age of inforamtion")
    plt.legend()
    lineplotMD(dataX, dataY6, dataY7, "Входная интенсивность, " + chr(955), "Средняя задержка, D, квант", "Среднее значение задержки от входной интенсивности")
    plt.legend()
    lineplotLambda(dataX, dataY1, dataX, "Входная интенсивность, " + chr(955), "Выходная интенсивность, " + chr(955), "Среднее значение выходной интенсивности от\nвходной интенсивности")
    plt.legend()

    plt.show()

if __name__ == '__main__':
    main()
