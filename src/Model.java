import java.util.ArrayList;
import java.util.Random;

public class Model {

    int numberOfLocations = 5;//64
    ArrayList<Location> locations;
    Random random;
    double a;
    double q;
    private float T = 1000;//10000
    public int N;
    public double mD;
    public double lyambda_out;
    public double sizeOfQuant;
    public double mediumSizeOfWork;

    public Model(float lyambda, double a, double q, double d, double quant) {

        locations = new ArrayList<>();
        random = new Random();
        this.sizeOfQuant = quant;
        for (int i = 0; i < numberOfLocations; i++) {
            locations.add(new Location(lyambda, T, q, i, quant));
        }
        this.a = a;
        this.q = q;
        this.N = 0;
        this.mD = 0;
        this.lyambda_out = 0;
        this.mediumSizeOfWork = 0;

    }

    public void getModeling() {
        System.out.println("Start of modeling");

        for (double t = 0; t < this.T; t += sizeOfQuant) {
            System.out.println("In t = " + t);

            for (int i = 0; i < locations.size(); i++) {
                locations.get(i).processingAtLocation(t);
                this.userSwitchLocation();
            }
        }

        ArrayList<Double> mDAtEachLocation = new ArrayList<>();
        ArrayList<Double> lyambdaOutAtEachLocation = new ArrayList<>();
        ArrayList<Double> mediumSizeOfWorkAtEachLocation = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            int tmpN = locations.get(i).countN();////считаем количество пользователей
            // выполнивших задачу за данное время в i локации
            mDAtEachLocation.add(locations.get(i).countMd(tmpN));
            lyambdaOutAtEachLocation.add(locations.get(i).countLyambda_out(tmpN, this.T));
            mediumSizeOfWorkAtEachLocation.add(locations.get(i).countMediumLengthOfWork());
        }
        this.mD = countMdAverage(mDAtEachLocation); // считаем среднюю задержку
        this.lyambda_out = countLyambda_outAverage(lyambdaOutAtEachLocation); //считаем выходную интенсивность
        this.mediumSizeOfWork = countAverage(mediumSizeOfWorkAtEachLocation);

        System.out.println("Summary");
        //по каждому пользователю
        for (int i = 0; i < locations.size(); i++) {
            ArrayList<WorkUser> tmpLocationInputStream = locations.get(i).inputStream;
            for (int k = 0; k < tmpLocationInputStream.size(); k++) {
                System.out.println("User №" + tmpLocationInputStream.get(k).userLocation + " do " +
                        tmpLocationInputStream.get(k).workProcessingValue +
                        " from his job. Full size = " + tmpLocationInputStream.get(k).workInfo.workSize);
            }
        }

        System.out.println("End of modeling");

    }

    public void userSwitchLocation() {
        for (int i = 0; i < locations.size(); i++) {
            Location tmpLocation = locations.get(i);
            Server tmpServer = tmpLocation.server;
            Random random = new Random();
            for (int k = 0; k < tmpServer.workUsersOnServer.size(); k++) {
                WorkUser tmpWorkUser = tmpServer.workUsersOnServer.get(k);
                if (tmpWorkUser.timeInCurrentLocation == 0) {
                    double probabilityToSwitch = random.nextDouble();
                    ArrayList<Pair> timeToNextLocation = new ArrayList<>();
                    for (int j = 0; j < locations.size(); j++) {
                        if (j == tmpWorkUser.userLocation)
                            continue;
                        //добавляем пару с значениями
                        //1 - Номер локации
                        //2 - Путь до данной локации (значение распределённое по экспоненциальному
                        // закону с параметром q)
                        timeToNextLocation.add(new Pair(j,
                                (int) Math.ceil(- (Math.log(Math.random()) / this.q) / this.sizeOfQuant)));
                    }

                    int nextLocation = (int) timeToNextLocation.get(0).windowIn;
                    double tmpClosetPath = timeToNextLocation.get(0).workSize;
                    for (int j = 0; j < timeToNextLocation.size(); j++) {
                        if (tmpClosetPath > timeToNextLocation.get(j).workSize) {
                            tmpClosetPath = timeToNextLocation.get(j).workSize;
                            nextLocation = (int) timeToNextLocation.get(j).windowIn;
                        }
                    }

                    //случай, когда задача пользователя уже находится отдельно от пользователя
                    //в случае симметричной системы, мы не переносим задачу, а только перемещаем
                    //пользователя
                    if (tmpWorkUser.userLocation != tmpWorkUser.workLocation) {
                        tmpWorkUser.userLocation = nextLocation;
                        continue;
                    }
                    //в этом случае меняем положение пользователя
                    if (tmpWorkUser.userLocation == tmpWorkUser.workLocation) {
                        //в случае, если случайное значение больше заданного значения
                        //вероятности, тогда пользователь решает перенести свою работу с одних серверов на другие
                        if (probabilityToSwitch > this.a) {
                            tmpServer.removeJobToSwitchServer(tmpWorkUser);
                            locations.get(nextLocation).server.addNewJob(tmpWorkUser);
                            tmpWorkUser.workLocation = nextLocation;
                            tmpWorkUser.userLocation = nextLocation;

                        }
                    }
                }
            }
        }
    }

    public double countMdAverage(ArrayList<Double> locationsData) {
        double tmp = 0;
        for (int i = 0; i < locationsData.size(); i++) {
            tmp += locationsData.get(i);
        }
        return tmp / locationsData.size();
    }

    public double countLyambda_outAverage(ArrayList<Double> locationsData) {
        double tmp = 0;
        for (int i = 0; i < locationsData.size(); i++) {
            tmp += locationsData.get(i);
        }
        return tmp / locationsData.size();
    }

    public double countAverage(ArrayList<Double> locationsData) {
        double tmp = 0;
        for (int i = 0; i < locationsData.size(); i++) {
            tmp += locationsData.get(i);
        }
        return tmp / locationsData.size();
    }


}