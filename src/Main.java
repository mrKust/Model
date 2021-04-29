public class Main {

    public static void main(String[] args) {

        //float lyambda = 0.5F; //интенсивность входного потока
        double q = 100;//верни потом это значение q = 0.0000001
        //double q = 0.15; //время перемещения q = 1, для симметричной системы
        double d = 0.15; //время необходимое для переноса данных
        double a = 0.5; //вероятность перехода данных на другие вычислительные мощности
        double quant = 0.01; // размер кванта
        FileWork file = new FileWork("/media/D/Pereezd/Labs/Димплом/Model/model.txt", false);

        for (float lyambda = (float) 0.1; lyambda < 2.0; lyambda += 0.1) {
        //for (float lyambda = (float) 0.1; lyambda < 0.3; lyambda += 0.1) {
            System.out.println("lyambda = " + lyambda);
            Model model = new Model(lyambda, a, q, d, quant);
            model.getModeling();
            file.write(lyambda, model.mD, model.lyambda_out, model.mediumSizeOfWork);
            System.out.println("lyambda = " + lyambda + " M[D] = " + model.mD + " lyambda_out = " +
                    model.lyambda_out);
        }

    }
}
