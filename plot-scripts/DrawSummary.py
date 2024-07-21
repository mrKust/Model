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
    with open("../data/summary.txt") as f:
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
    dataY9 = []
    dataY10 = []

    for line in data:
        dataX.append(line.pop(0))
        dataY1.append(line.pop(0)) #theor
        dataY2.append(line.pop(0)) #md exp
        dataY3.append(line.pop(0)) #aoi exp
        dataY4.append(line.pop(0)) #tmp exp
        dataY5.append(line.pop(0)) #md const
        if (dataX[-1] < 1.0):
            dataY6.append(line.pop(0)) # aoi const
            dataY7.append(line.pop(0)) # tmp const
            dataY8.append(line.pop(0)) # md uniform
            dataY9.append(line.pop(0)) # aoi uniform
            dataY10.append(line.pop(0)) # tmp uniform

    lineplot(dataX,
             [dataY1, dataY2, dataY3, dataY5, dataY6, dataY8, dataY9],
             ['theor D', 'D exp', 'AoI exp', 'D const', 'AoI const', 'D uniform', 'AoI uniform'],
             ['--go', '--bo', '--yo', '--ro', '-co', '--ko', '--mo'],
             "Входная интенсивность, " + chr(955),
             "Cредний возраст информации\nCредняя задержка",
             "Средняя задержка и средний возраст информации для системы\nс круговым опросом при разных законах обслуживания"
             )
    plt.legend()

    lineplot(dataX,
             [dataY4, dataY7, dataY10],
             ['Tmp exp', 'Tmp const', 'Tmp uniform'],
             ['-go', '--bo', '--yo'],
             "Входная интенсивность, " + chr(955),
             "Tmp",
             "Средняя задержка + 1/входная интенсивность"
             )
    plt.legend()

    plt.show()


if __name__ == '__main__':
    main()