import java.util.ArrayList;
import java.util.Random;

public class Model {

    int numberOfLocations = 2;//64
    ArrayList<Location> locations;
    Random random;
    double a;
    double q;
    private float T = 100;//10000
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
                for (int k = 0; k < locations.get(i).inputStream.size(); k++) {
                    WorkUser tmp = locations.get(i).inputStream.get(k);
                    if ((tmp.statusFinishedOrUnfinished == false) && (tmp.timeInCurrentLocation == 0)) {
                        ArrayList<Double> timeToMove = new ArrayList<>();
                        for (int j = 0; j < this.numberOfLocations; j++) {
                            if (j == k)
                                timeToMove.add(-1.0);
                            timeToMove.add(- (Math.log(Math.random()) / this.q));
                        }
                        int nextLocation = 0;
                        double tmpMinTimeToMove = timeToMove.get(0);
                        for (int j = 1; j < timeToMove.size(); j++) {
                            if ((timeToMove.get(j) < tmpMinTimeToMove) && (timeToMove.get(j) != -1.0)) {
                                nextLocation = j;
                                tmpMinTimeToMove = timeToMove.get(j);
                            }
                        }
                    }
                }
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