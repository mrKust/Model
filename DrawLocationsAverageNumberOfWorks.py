import matplotlib.pyplot as plt


def lineplot(x_data, y_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
    ax.plot(x_data, y_data, 'b', lw=4)
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)

def main():

    dataLocations = []
    with open("D:\Pereezd\Labs\Научка\Model\LocationsAverangeNumber2D.txt") as f:
        for line in f:
            dataLocations.append([float(x) for x in line.split()])

    dataLocationNumber = []
    dataAverageNumberOfWorks = []

    for line in dataLocations:
        dataLocationNumber.append(line.pop(0))
        dataAverageNumberOfWorks.append(line.pop(0))

    print(dataLocationNumber)
    print(dataAverageNumberOfWorks)

    lineplot(dataLocationNumber, dataAverageNumberOfWorks, "Номер области", "Среднее кол-во задач", "Распределение задач по областям")

    plt.show()


if __name__ == '__main__':
    main()
