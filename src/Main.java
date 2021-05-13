public class Main {

    public static void main(String[] args) {
        double q = 1;//время перемещения q = 1, для симметричной системы
        double d = 0.15; //время необходимое для переноса данных
        double a = 1; //вероятность перехода данных на другие вычислительные мощности
        double quant = 0.01; // размер кванта
        FileWork fileUbuntu = new FileWork("/media/D/Pereezd/Labs/Димплом/Model/model.txt", false);
        //FileWork fileMac = new FileWork("/Users/andreyvasilyev/Desktop/Model/model.txt", false);

        for (float lyambda = (float) 0.1; lyambda < 1.0; lyambda += 0.1) {
            System.out.println("lyambda = " + lyambda);
            Model model = new Model(lyambda, a, q, d, quant);
            model.getModeling();
            fileUbuntu.write(lyambda, model.mD, model.lyambda_out, model.mediumSizeOfWork,
                    model.mDTheoretical);
            //fileMac.write(lyambda, model.mD, model.lyambda_out, model.mediumSizeOfWork);
            System.out.println("lyambda = " + lyambda + " M[D] = " + model.mD + " lyambda_out = " +
                    model.lyambda_out);
        }

    }
}
