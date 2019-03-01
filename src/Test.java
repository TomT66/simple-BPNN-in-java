import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Test {

    public static void main(String[] args){
    	
        Bp bp = new Bp(32, 15, 6, 0.05);
        
        Random random = new Random();
        
        List<Integer> list = new ArrayList<Integer>();
        int origInputInt;
        Scanner in = new Scanner(System.in);
        for (int i = 0; i < 50000; i++) {
            int value = random.nextInt();
            list.add(value);
        }

        for(int i=0; i<30; i++) {
        	for(int value : list) {
        		double[] target = new double[4];
        		if(value >= 0) {
        			if((value&1) == 1) {
        				target[0] = 1;
        			}
        			else target[1] = 1;
        		}
        		else {
        			if((value&1) == 1) {
        				target[2] = 1;
        			}
        			else target[3] = 1;
        		}
        		double[] input = new double[32];
        		int index = 31;
        		do {
        			input[index--] = (value & 1);
        			value >>>=1;
        		}while(value != 0);
        		bp.train(input, target);
        	}        	
        }

        System.out.println(" training finished. Please input your number(input 0 to end.):");

        while(true){
        	int origInput = in.nextInt();
        	if(origInput == 0) break;
        	origInputInt = origInput;
        	double[] input = new double[32];
        	int index = 31;
        	do {
    			input[index--] = (origInput & 1);
    			origInput >>>=1;
    		}while(origInput != 0);
        	double[] result = new double[4];
        	bp.predict(input, result);
        	
        	double maxResult = result[0];
        	int idxOfMax = 0;
        	for(int i=1; i<result.length ;i++) {
        		if(result[i] > maxResult) {
        			idxOfMax = i;
        			maxResult = result[i];
        			i++;
        		}
        	}
        	switch(idxOfMax) {
        	case 0: System.out.println(origInputInt + "is a positive odd number");break;

        	case 1: System.out.println(origInputInt + "is a positive even number");break;

        	case 2: System.out.println(origInputInt + "is a negative odd number");break;

        	case 3: System.out.println(origInputInt + "is a negative even number");break;
        	}
        }
        in.close();
    }
}