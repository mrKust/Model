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
    with open("../modeling-service/modelPREEMPTIVE0.2.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    print(data)
    dataXPREEMPTIVE02 = []
    dataY1PREEMPTIVE02 = []
    dataY2PREEMPTIVE02 = []
    dataY3PREEMPTIVE02 = []
    dataY4PREEMPTIVE02 = []
    dataY5PREEMPTIVE02 = []
    dataY6PREEMPTIVE02 = []
    dataY7PREEMPTIVE02 = []
    dataY8PREEMPTIVE02 = []
    dataY9PREEMPTIVE02 = []
    dataY10PREEMPTIVE02 = []

    for line in data:
        dataXPREEMPTIVE02.append(line.pop(0))
        dataY1PREEMPTIVE02.append(line.pop(0))
        dataY2PREEMPTIVE02.append(line.pop(0))
        dataY3PREEMPTIVE02.append(line.pop(0))
        dataY4PREEMPTIVE02.append(line.pop(0))
        dataY5PREEMPTIVE02.append(line.pop(0))
        dataY6PREEMPTIVE02.append(line.pop(0))
        if (dataXPREEMPTIVE02[-1] < 1.0):
            dataY7PREEMPTIVE02.append(line.pop(0))
            dataY8PREEMPTIVE02.append(line.pop(0))
            dataY9PREEMPTIVE02.append(line.pop(0))
            dataY10PREEMPTIVE02.append(line.pop(0))

    data = []
    with open("../modeling-service/modelNON_PREEMPTIVE0.2.txt") as f2:
        for line in f2:
            data.append([float(x) for x in line.split()])

    print(data)
    dataXNON_PREEMPTIVE02 = []
    dataY1NON_PREEMPTIVE02 = []
    dataY2NON_PREEMPTIVE02 = []
    dataY3NON_PREEMPTIVE02 = []
    dataY4NON_PREEMPTIVE02 = []
    dataY5NON_PREEMPTIVE02 = []
    dataY6NON_PREEMPTIVE02 = []
    dataY7NON_PREEMPTIVE02 = []
    dataY8NON_PREEMPTIVE02 = []
    dataY9NON_PREEMPTIVE02 = []
    dataY10NON_PREEMPTIVE02 = []

    for line in data:
        dataXNON_PREEMPTIVE02.append(line.pop(0))
        dataY1NON_PREEMPTIVE02.append(line.pop(0))
        dataY2NON_PREEMPTIVE02.append(line.pop(0))
        dataY3NON_PREEMPTIVE02.append(line.pop(0))
        dataY4NON_PREEMPTIVE02.append(line.pop(0))
        dataY5NON_PREEMPTIVE02.append(line.pop(0))
        dataY6NON_PREEMPTIVE02.append(line.pop(0))
        if (dataXNON_PREEMPTIVE02[-1] < 1.0):
            dataY7NON_PREEMPTIVE02.append(line.pop(0))
            dataY8NON_PREEMPTIVE02.append(line.pop(0))
            dataY9NON_PREEMPTIVE02.append(line.pop(0))
            dataY10NON_PREEMPTIVE02.append(line.pop(0))

    data = []
    with open("../modeling-service/modelPREEMPTIVE0.5.txt") as f3:
            for line in f3:
                data.append([float(x) for x in line.split()])

    print(data)
    dataXPREEMPTIVE05 = []
    dataY1PREEMPTIVE05 = []
    dataY2PREEMPTIVE05 = []
    dataY3PREEMPTIVE05 = []
    dataY4PREEMPTIVE05 = []
    dataY5PREEMPTIVE05 = []
    dataY6PREEMPTIVE05 = []
    dataY7PREEMPTIVE05 = []
    dataY8PREEMPTIVE05 = []
    dataY9PREEMPTIVE05 = []
    dataY10PREEMPTIVE05 = []

    for line in data:
        dataXPREEMPTIVE05.append(line.pop(0))
        dataY1PREEMPTIVE05.append(line.pop(0))
        dataY2PREEMPTIVE05.append(line.pop(0))
        dataY3PREEMPTIVE05.append(line.pop(0))
        dataY4PREEMPTIVE05.append(line.pop(0))
        dataY5PREEMPTIVE05.append(line.pop(0))
        dataY6PREEMPTIVE05.append(line.pop(0))
        if (dataXPREEMPTIVE05[-1] < 1.0):
            dataY7PREEMPTIVE05.append(line.pop(0))
            dataY8PREEMPTIVE05.append(line.pop(0))
            dataY9PREEMPTIVE05.append(line.pop(0))
            dataY10PREEMPTIVE05.append(line.pop(0))

    data = []
    with open("../modeling-service/modelNON_PREEMPTIVE0.5.txt") as f4:
        for line in f4:
            data.append([float(x) for x in line.split()])

    print(data)
    dataXNON_PREEMPTIVE05 = []
    dataY1NON_PREEMPTIVE05 = []
    dataY2NON_PREEMPTIVE05 = []
    dataY3NON_PREEMPTIVE05 = []
    dataY4NON_PREEMPTIVE05 = []
    dataY5NON_PREEMPTIVE05 = []
    dataY6NON_PREEMPTIVE05 = []
    dataY7NON_PREEMPTIVE05 = []
    dataY8NON_PREEMPTIVE05 = []
    dataY9NON_PREEMPTIVE05 = []
    dataY10NON_PREEMPTIVE05 = []

    for line in data:
        dataXNON_PREEMPTIVE05.append(line.pop(0))
        dataY1NON_PREEMPTIVE05.append(line.pop(0))
        dataY2NON_PREEMPTIVE05.append(line.pop(0))
        dataY3NON_PREEMPTIVE05.append(line.pop(0))
        dataY4NON_PREEMPTIVE05.append(line.pop(0))
        dataY5NON_PREEMPTIVE05.append(line.pop(0))
        dataY6NON_PREEMPTIVE05.append(line.pop(0))
        if (dataXNON_PREEMPTIVE05[-1] < 1.0):
            dataY7NON_PREEMPTIVE05.append(line.pop(0))
            dataY8NON_PREEMPTIVE05.append(line.pop(0))
            dataY9NON_PREEMPTIVE05.append(line.pop(0))
            dataY10NON_PREEMPTIVE05.append(line.pop(0))

    data = []
    with open("../modeling-service/modelPREEMPTIVE0.8.txt") as f5:
            for line in f5:
                data.append([float(x) for x in line.split()])

    print(data)
    dataXPREEMPTIVE08 = []
    dataY1PREEMPTIVE08 = []
    dataY2PREEMPTIVE08 = []
    dataY3PREEMPTIVE08 = []
    dataY4PREEMPTIVE08 = []
    dataY5PREEMPTIVE08 = []
    dataY6PREEMPTIVE08 = []
    dataY7PREEMPTIVE08 = []
    dataY8PREEMPTIVE08 = []
    dataY9PREEMPTIVE08 = []
    dataY10PREEMPTIVE08 = []

    for line in data:
        dataXPREEMPTIVE08.append(line.pop(0))
        dataY1PREEMPTIVE08.append(line.pop(0))
        dataY2PREEMPTIVE08.append(line.pop(0))
        dataY3PREEMPTIVE08.append(line.pop(0))
        dataY4PREEMPTIVE08.append(line.pop(0))
        dataY5PREEMPTIVE08.append(line.pop(0))
        dataY6PREEMPTIVE08.append(line.pop(0))
        if (dataXPREEMPTIVE08[-1] < 1.0):
            dataY7PREEMPTIVE08.append(line.pop(0))
            dataY8PREEMPTIVE08.append(line.pop(0))
            dataY9PREEMPTIVE08.append(line.pop(0))
            dataY10PREEMPTIVE08.append(line.pop(0))

    data = []
    with open("../modeling-service/modelNON_PREEMPTIVE0.8.txt") as f6:
        for line in f6:
            data.append([float(x) for x in line.split()])

    print(data)
    dataXNON_PREEMPTIVE08 = []
    dataY1NON_PREEMPTIVE08 = []
    dataY2NON_PREEMPTIVE08 = []
    dataY3NON_PREEMPTIVE08 = []
    dataY4NON_PREEMPTIVE08 = []
    dataY5NON_PREEMPTIVE08 = []
    dataY6NON_PREEMPTIVE08 = []
    dataY7NON_PREEMPTIVE08 = []
    dataY8NON_PREEMPTIVE08 = []
    dataY9NON_PREEMPTIVE08 = []
    dataY10NON_PREEMPTIVE08 = []

    for line in data:
        dataXNON_PREEMPTIVE08.append(line.pop(0))
        dataY1NON_PREEMPTIVE08.append(line.pop(0))
        dataY2NON_PREEMPTIVE08.append(line.pop(0))
        dataY3NON_PREEMPTIVE08.append(line.pop(0))
        dataY4NON_PREEMPTIVE08.append(line.pop(0))
        dataY5NON_PREEMPTIVE08.append(line.pop(0))
        dataY6NON_PREEMPTIVE08.append(line.pop(0))
        if (dataXNON_PREEMPTIVE08[-1] < 1.0):
            dataY7NON_PREEMPTIVE08.append(line.pop(0))
            dataY8NON_PREEMPTIVE08.append(line.pop(0))
            dataY9NON_PREEMPTIVE08.append(line.pop(0))
            dataY10NON_PREEMPTIVE08.append(line.pop(0))

    lineplot(dataXNON_PREEMPTIVE02,
#              [dataY5PREEMPTIVE02, dataY5PREEMPTIVE05, dataY5PREEMPTIVE08],
             [dataY5NON_PREEMPTIVE02, dataY5NON_PREEMPTIVE05, dataY5NON_PREEMPTIVE08],
             ['Вероятность переноса 0.2', 'Вероятность переноса 0.5', 'Вероятность переноса 0.8'],
             ['-go', '--bo', '--yo'],
             "Входная интенсивность, " + chr(955),
             "Средний возраст информации,\nAoI, ед.времени",
             "Средний возраст информации"
             )
    plt.legend()

    lineplot(dataXNON_PREEMPTIVE02,
#              [dataY8PREEMPTIVE02, dataY8PREEMPTIVE05, dataY8PREEMPTIVE08],
             [dataY8NON_PREEMPTIVE02, dataY8NON_PREEMPTIVE05, dataY8NON_PREEMPTIVE08],
             ['Вероятность переноса 0.2', 'Вероятность переноса 0.5', 'Вероятность переноса 0.8'],
             ['-go', '--bo', '--yo'],
             "Входная интенсивность, " + chr(955),
             "Средняя задержка, D, квант",
             "Среднее значение задержки от входной интенсивности"
             )
    plt.legend()

    lineplot(dataXNON_PREEMPTIVE02,
#              [dataY1PREEMPTIVE02, dataY1PREEMPTIVE05, dataY1PREEMPTIVE08],
             [dataY1NON_PREEMPTIVE02, dataY1NON_PREEMPTIVE05, dataY1NON_PREEMPTIVE08],
             ['Вероятность переноса 0.2', 'Вероятность переноса 0.5', 'Вероятность переноса 0.8'],
             ['-go', '--bo', '--yo'],
             "Входная интенсивность, " + chr(955),
             "Выходная интенсивность, " + chr(955),
             "Зависимость выходной интенсивности от\nвходной интенсивности"
             )
    plt.legend()

    lineplot(dataXNON_PREEMPTIVE02,
#              [dataY6PREEMPTIVE02, dataY6PREEMPTIVE05, dataY6PREEMPTIVE08],
             [dataY6NON_PREEMPTIVE02, dataY6NON_PREEMPTIVE05, dataY6NON_PREEMPTIVE08],
             ['Вероятность переноса 0.2', 'Вероятность переноса 0.5', 'Вероятность переноса 0.8'],
             ['-go', '--bo', '--yo'],
             "Входная интенсивность, " + chr(955),
             "Процент потерянных задач, %",
             "Зависимость процента потерянных задач от\nвходной интенсивности"
             )
    plt.legend()

    lineplot(dataXNON_PREEMPTIVE02,
#              [dataY2PREEMPTIVE02, dataY2PREEMPTIVE05, dataY2PREEMPTIVE08],
             [dataY2NON_PREEMPTIVE02, dataY2NON_PREEMPTIVE05, dataY2NON_PREEMPTIVE08],
             ['Вероятность переноса 0.2', 'Вероятность переноса 0.5', 'Вероятность переноса 0.8'],
             ['-go', '--bo', '--yo'],
             "Входная интенсивность, " + chr(955),
             "Средний размер задачи, квант",
             "Зависимость среднего размера задач от\nвходной интенсивности"
             )
    plt.legend()

    plt.show()


if __name__ == '__main__':
    main()
