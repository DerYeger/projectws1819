package de.uniks.liverisk.util;

public class MathHelper {

    //based on https://technomanor.wordpress.com/2012/03/04/determinant-of-n-x-n-square-matrix/
    static double calculateDeterminant(double[][] matrix, int order) {
        double det = 0;
        int sign = 1;
        int p = 0;
        int q = 0;

        if(order == 1){
            det = matrix[0][0];
        }
        else{
            double subMatrix[][] = new double[order - 1][order - 1];
            for(int x = 0 ; x < order; x++){
                p=0;
                q=0;
                for(int i = 1; i < order; i++){
                    for(int j = 0; j < order; j++){
                        if(j != x){
                            subMatrix[p][q++] = matrix[i][j];
                            if(q % (order - 1) == 0) {
                                p++;
                                q=0;
                            }
                        }
                    }
                }
                det += matrix[0][x] * calculateDeterminant(subMatrix, order - 1) * sign;
                sign = -sign;
            }
        }
        return det;
    }
}
