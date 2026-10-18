import matplotlib.pyplot as plt


def lineplot(x_data, y_data, y_labels, lane_colors, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    label_index = 0
    for y_dots in y_data:
        ax.plot(x_data, y_dots, lane_colors[label_index], lw=3, label=y_labels[label_index])
        label_index += 1
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.grid(True)

def main():
    data = []
    with open("../modeling-service/modelPREEMPTIVE.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    print(data)
    dataXPREEMPTIVE = []
    dataY1PREEMPTIVE = []
    dataY2PREEMPTIVE = []
    dataY3PREEMPTIVE = []
    dataY4PREEMPTIVE = []
    dataY5PREEMPTIVE = []
    dataY6PREEMPTIVE = []
    dataY7PREEMPTIVE = []
    dataY8PREEMPTIVE = []
    dataY9PREEMPTIVE = []
    dataY10PREEMPTIVE = []

    for line in data:
        dataXPREEMPTIVE.append(line.pop(0))
        dataY1PREEMPTIVE.append(line.pop(0))
        dataY2PREEMPTIVE.append(line.pop(0))
        dataY3PREEMPTIVE.append(line.pop(0))
        dataY4PREEMPTIVE.append(line.pop(0))
        dataY5PREEMPTIVE.append(line.pop(0))
        dataY6PREEMPTIVE.append(line.pop(0))
        if (dataXPREEMPTIVE[-1] < 1.0):
            dataY7PREEMPTIVE.append(line.pop(0))
            dataY8PREEMPTIVE.append(line.pop(0))
            dataY9PREEMPTIVE.append(line.pop(0))
            dataY10PREEMPTIVE.append(line.pop(0))

    data = []
    with open("../modeling-service/modelNON_PREEMPTIVE.txt") as f2:
        for line in f2:
            data.append([float(x) for x in line.split()])

    print(data)
    dataXNON_PREEMPTIVE = []
    dataY1NON_PREEMPTIVE = []
    dataY2NON_PREEMPTIVE = []
    dataY3NON_PREEMPTIVE = []
    dataY4NON_PREEMPTIVE = []
    dataY5NON_PREEMPTIVE = []
    dataY6NON_PREEMPTIVE = []
    dataY7NON_PREEMPTIVE = []
    dataY8NON_PREEMPTIVE = []
    dataY9NON_PREEMPTIVE = []
    dataY10NON_PREEMPTIVE = []

    for line in data:
        dataXNON_PREEMPTIVE.append(line.pop(0))
        dataY1NON_PREEMPTIVE.append(line.pop(0))
        dataY2NON_PREEMPTIVE.append(line.pop(0))
        dataY3NON_PREEMPTIVE.append(line.pop(0))
        dataY4NON_PREEMPTIVE.append(line.pop(0))
        dataY5NON_PREEMPTIVE.append(line.pop(0))
        dataY6NON_PREEMPTIVE.append(line.pop(0))
        if (dataXNON_PREEMPTIVE[-1] < 1.0):
            dataY7NON_PREEMPTIVE.append(line.pop(0))
            dataY8NON_PREEMPTIVE.append(line.pop(0))
            dataY9NON_PREEMPTIVE.append(line.pop(0))
            dataY10NON_PREEMPTIVE.append(line.pop(0))

    lineplot(dataXNON_PREEMPTIVE,
             [dataY5PREEMPTIVE, dataY5NON_PREEMPTIVE],
             ['C вытесняющим подходом', 'С невытесняющим подходом'],
             ['-go', '--bo'],
             "Входная интенсивность, " + chr(955),
             "Средний возраст информации,\nAoI, ед.времени",
             "Средний возраст информации"
             )
    plt.legend()

    lineplot(dataXNON_PREEMPTIVE,
             [dataY8PREEMPTIVE, dataY8NON_PREEMPTIVE],
             ['C вытесняющим подходом', 'С невытесняющим подходом'],
             ['-go', '--bo'],
             "Входная интенсивность, " + chr(955),
             "Средняя задержка, D, квант",
             "Среднее значение задержки от входной интенсивности"
             )
    plt.legend()

    lineplot(dataXNON_PREEMPTIVE,
             [dataXNON_PREEMPTIVE, dataY1PREEMPTIVE, dataY1NON_PREEMPTIVE],
             ['Входной поток', 'Выходной поток с вытесняющим подходом', 'Выходной поток с невытесняющим подходом'],
             ['-yo', '--go', '--bo'],
             "Входная интенсивность, " + chr(955),
             "Выходная интенсивность, " + chr(955),
             "Зависимость выходной интенсивности от\nвходной интенсивности"
             )
    plt.legend()

    lineplot(dataXNON_PREEMPTIVE,
             [dataY6PREEMPTIVE, dataY6NON_PREEMPTIVE],
             ['C вытесняющим подходом', 'С невытесняющим подходом'],
             ['-go', '--bo'],
             "Входная интенсивность, " + chr(955),
             "Процент потерянных задач, %",
             "Зависимость процента потерянных задач от\nвходной интенсивности"
             )
    plt.legend()

    plt.show()


if __name__ == '__main__':
    main()
