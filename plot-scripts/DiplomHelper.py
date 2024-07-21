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
    dataY8 = []

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
            dataY8.append(line.pop(0))

    lineplot(dataX,
                 [dataY8],
                 ['Практические значения'],
                 ['-go'],
                 "x",
                 "y",
                 "Средняя задержка + 1/входная интенсивность"
                 )
    plt.legend()

    lineplot(dataX,
             [dataY4, dataY5],
             ['Теоретические значения', 'Практические значения'],
             ['-go', '--bo'],
             "age of information theor",
             "age of information modeling",
             "medium age of inforamtion"
             )
    plt.legend()

    lineplot(dataX,
             [dataY4, dataY5, dataY6, dataY7],
             ['Теоретические значения AoI', 'Практические значения AoI', 'Теоретические значения M[D]', 'Практические значения M[D]'],
             ['-go', '--bo', '-yo', '--ro'],
             "Входная интенсивность, " + chr(955),
             "Cредний возраст информации\n средняя задержка",
             "Средняя задержка и средний возраст информации"
             )
    plt.legend()



    lineplot(dataX,
             [dataY6, dataY7],
             ['Теоретические значения', 'Практические значения'],
             ['-go', '--bo'],
             "Входная интенсивность, " + chr(955),
             "Средняя задержка, D, квант",
             "Среднее значение задержки от входной интенсивности"
             )
    plt.legend()

    lineplot(dataX,
             [dataY1, dataX],
             ['Входной поток', 'Выходной поток'],
             ['-go', '--bo'],
             "Входная интенсивность, " + chr(955),
             "Выходная интенсивность, " + chr(955),
             "Среднее значение выходной интенсивности от\nвходной интенсивности"
             )
    plt.legend()

    plt.show()


if __name__ == '__main__':
    main()
