import matplotlib.pyplot as plt


def lineplot(x_data, y_data, y2_data, x_label="", y_label="", title=""):
    _, ax = plt.subplots()
#    ax.plot(x_data, y_data, 'b', lw=4, label="С временем на перенос")
    ax.plot(x_data, y2_data, 'g--', lw=4, label="Без времени на перенос")
    ax.set_title(title)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)


def main():
    data = []
    with open("D:\Pereezd\Labs\Научка\Model\ModelResultsWithTransferTime\LambdaCritFromNumberOfLoc.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    dataNumberOfLocations = []
    dataLambdaCriticalWithTransferTime = []

    for line in data:
        dataNumberOfLocations.append(line.pop(0))
        dataLambdaCriticalWithTransferTime.append(line.pop(0))

    data = []
    with open("D:\Pereezd\Labs\Научка\Model\ModelResultsWithoutTransferTime\LambdaCritFromNumberOfLoc.txt") as f:
        for line in f:
            data.append([float(x) for x in line.split()])

    dataNumberOfLocations = []
    dataLambdaCriticalWithoutTransferTime = []

    for line in data:
        dataNumberOfLocations.append(line.pop(0))
        dataLambdaCriticalWithoutTransferTime.append(line.pop(0))

    print(dataNumberOfLocations)
    print(dataLambdaCriticalWithTransferTime)
    print(dataLambdaCriticalWithoutTransferTime)

    lineplot(dataNumberOfLocations, dataLambdaCriticalWithTransferTime, dataLambdaCriticalWithoutTransferTime, "Кол-во областей", chr(955) + " критическая", chr(955) + " критеческая от кол-ва областей")
    plt.legend()

    plt.show()


if __name__ == '__main__':
    main()
