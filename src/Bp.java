import java.util.Random;

public class Bp {

    private double[] hiden1_in;
    private double[][] hiden1_w;
    private double[] hiden1_errors;

    private double[] hiden2_out;
    private double[][] out_w;
    
    private double[] out_errors;

    private double[] target;

    private double rate;

    public Bp(int input_nodes, int hidenlyr1, int hidenly2, double rate) {
        super();

        hiden1_in = new double[input_nodes + 1];

        hiden1_w = new double[hidenlyr1][input_nodes + 1];
        hiden1_errors = new double[hidenlyr1];

        hiden2_out = new double[hidenlyr1 + 1];
        out_w = new double[hidenly2][hidenlyr1 + 1];
        out_errors = new double[hidenly2];

        target = new double[hidenly2];

        this.rate = rate;
        init_weight();
    }

    public void init_weight() {

        set_weight(hiden1_w);
        set_weight(out_w);
    }

    private void set_weight(double[][] w) {
    	Random random = new Random();
        for (int i = 0, len = w.length; i != len; i++)
            for (int j = 0, len2 = w[i].length; j != len2; j++) {
                w[i][j] = random.nextDouble();
            }
    }

    private void setHide1_x(double[] Data) {
        if (Data.length != hiden1_in.length - 1) {
            throw new IllegalArgumentException("数据大小与输入层节点不匹配");
        }
        System.arraycopy(Data, 0, hiden1_in, 1, Data.length);
        hiden1_in[0] = 1.0;
    }

    private void setTarget(double[] target) {
        this.target = target;
    }

    public void train(double[] TrainData, double[] target) {

        setHide1_x(TrainData);
        setTarget(target);

        double[] output = new double[out_w.length + 1];
        forword(hiden1_in, output);

        backpropagation(output);

    }

    public void backpropagation(double[] output) {

        get_out_error(output, target, out_errors);
        get_hide_error(out_errors, out_w, hiden2_out, hiden1_errors);
        update_weight(hiden1_errors, hiden1_w, hiden1_in);
        update_weight(out_errors, out_w, hiden2_out);
    }

    public void predict(double[] data, double[] output) {

        double[] out_y = new double[out_w.length + 1];
        setHide1_x(data);
        forword(hiden1_in, out_y);
        System.arraycopy(out_y, 1, output, 0, output.length);
    }
    
    
    public void update_weight(double[] err, double[][] w, double[] x) {

        double newweight = 0.0;
        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < w[i].length; j++) {
                newweight = rate * err[i] * x[j];
                w[i][j] = w[i][j] + newweight;
            }

        }
    }

    public void get_out_error(double[] output, double[] target, double[] out_error) {
        for (int i = 0; i < target.length; i++) {
            out_error[i] = (target[i] - output[i + 1]) * output[i + 1] * (1d - output[i + 1]);
        }

    }


    public void get_hide_error(double[] NeLaErr, double[][] Nextw, double[] output, double[] error) {

        for (int k = 0; k < error.length; k++) {
            double sum = 0;
            for (int j = 0; j < Nextw.length; j++) {
                sum += Nextw[j][k + 1] * NeLaErr[j];
            }
            error[k] = sum * output[k + 1] * (1d - output[k + 1]);
        }
    }

    public void forword(double[] x, double[] output) {

        get_net_out(x, hiden1_w, hiden2_out);
        get_net_out(hiden2_out, out_w, output);

    }


    private double get_node_put(double[] x, double[] w) {
        double z = 0d;

        for (int i = 0; i < x.length; i++) {
            z += x[i] * w[i];
        }
        return 1d / (1d + Math.exp(-z));
    }

    private void get_net_out(double[] x, double[][] w, double[] net_out) {

        net_out[0] = 1d;
        for (int i = 0; i < w.length; i++) {
            net_out[i + 1] = get_node_put(x, w[i]);
        }
    }
}