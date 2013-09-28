package com.ml.nlp.clustering;
import java.util.Arrays;

public class EKmeans {
    protected double[][] centroids;	//[行][列], 行代表数据数量，列代表维度
    protected double[][] points;
    protected int idealCount;		//每个中心里理想点的个数
    protected double[][] distances;
    protected int[] assignments;	//点对应的中心
    protected boolean[] changes;	//记录中心对应的点是否改变
    protected int[] counts;			//中心对应的点的个数
    protected boolean[] dones;		//???
    protected int iteration;		//迭代次数
    protected boolean equal;		//是否开启均衡基数
    protected DistanceFunction distanceFunction;
    protected Listener listener;

    public EKmeans(double[][] centroids, double[][] points) {
        this.centroids = centroids;
        this.points = points;
        if (centroids.length > 0) {
            idealCount = points.length / centroids.length;
        } else {
            idealCount = 0;
        }
        distances = new double[centroids.length][points.length];
        assignments = new int[points.length];
        Arrays.fill(assignments, -1);
        changes = new boolean[centroids.length];
        Arrays.fill(changes, true);
        counts = new int[centroids.length];
        dones = new boolean[centroids.length];
        iteration = 128;
        equal = false;
        distanceFunction = EUCLIDEAN_DISTANCE_FUNCTION;
        listener = null;
    }

    

    public void run() {
        calculateDistances();
        int move = makeAssignments();
        int i = 0;
        while (move > 0 && i++ < iteration) {
            moveCentroids();
            calculateDistances();
            move = makeAssignments();
            if (listener != null) {
                listener.iteration(i, move);
            }
        }
    }

    protected void calculateDistances() {
        //计算所有点到各个中心点的距离
        for (int c = 0; c < centroids.length; c++) {
            if (!changes[c]) {
                continue;
            }
            double[] centroid = centroids[c];
            for (int p = 0; p < points.length; p++) {
                double[] point = points[p];
                distances[c][p] = distanceFunction.distance(centroid, point);
            }
            changes[c] = false;
        }
    }

    protected int makeAssignments() {
    	//move记录移动次数
        int move = 0;
        Arrays.fill(counts, 0);
        for (int p = 0; p < points.length; p++) {
        	//得到与点p最近的中心点nc
            int nc = nearestCentroid(p);
            if (nc == -1) {
                continue;
            }
            //若点p已指定的中心不属于中心nc
            if (assignments[p] != nc) {
                if (assignments[p] != -1) {
                    changes[assignments[p]] = true;
                }
                changes[nc] = true;
                assignments[p] = nc;
                move++;
            }
            //该中心对应点的个数加1
            counts[nc]++;
            //均衡基数，若点个数为100个，分为5类，那每个类idealCount为20个
            //若某个类的点个数大于20，那重新分配
            if (equal && counts[nc] > idealCount) {
                move += remakeAssignments(nc);
            }
        }
        return move;
    }

    protected int remakeAssignments(int cc) {
        int move = 0;
        double md = Double.MAX_VALUE;
        int nc = -1;
        int np = -1;
        //计算中心cc下的点到其他中心的距离
        for (int p = 0; p < points.length; p++) {
        	//排除非中心cc下的点
            if (assignments[p] != cc) {
                continue;
            }
            //计算p点到其他中心的距离，得到最近的
            for (int c = 0; c < centroids.length; c++) {
            	//排除自己
                if (c == cc || dones[c]) {
                    continue;
                }
                double d = distances[c][p];
                if (d < md) {
                    md = d;
                    nc = c;
                    np = p;
                }
            }
        }
        if (nc != -1 && np != -1) {
            if (assignments[np] != nc) {
                if (assignments[np] != -1) {
                    changes[assignments[np]] = true;
                }
                changes[nc] = true;
                assignments[np] = nc;
                move++;
            }
            //cc中心对应点的个数减1
            //nc中心对应点的个数加1
            counts[cc]--;
            counts[nc]++;
            if (counts[nc] > idealCount) {
                dones[cc] = true;	//????
                move += remakeAssignments(nc);
                dones[cc] = false;
            }
        }
        return move;
    }

    protected int nearestCentroid(int p) {
    	//计算点p离哪个中心点最近
        double md = Double.MAX_VALUE;
        int nc = -1;
        for (int c = 0; c < centroids.length; c++) {
            double d = distances[c][p];
            if (d < md) {
                md = d;
                nc = c;
            }
        }
        return nc;
    }

    protected void moveCentroids() {
        for (int c = 0; c < centroids.length; c++) {
            if (!changes[c]) {
                continue;
            }
            double[] centroid = centroids[c];
            int n = 0;
            Arrays.fill(centroid, 0);
            //计算中心c下所有点的均值，设为新的中心点
            for (int p = 0; p < points.length; p++) {
                if (assignments[p] != c) {
                    continue;
                }
                double[] point = points[p];
                n++;
                for (int d = 0; d < centroid.length; d++) {
                    centroid[d] += point[d];
                }
            }
            if (n > 0) {
                for (int d = 0; d < centroid.length; d++) {
                    centroid[d] /= n;
                }
            }
        }
    }
    
    public double[][] getCentroids() {
        return centroids;
    }

    public double[][] getPoints() {
        return points;
    }

    public double[][] getDistances() {
        return distances;
    }

    public int[] getAssignments() {
        return assignments;
    }

    public boolean[] getChanges() {
        return changes;
    }

    public int[] getCounts() {
        return counts;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public boolean isEqual() {
        return equal;
    }

    public void setEqual(boolean equal) {
        this.equal = equal;
    }

    public DistanceFunction getDistanceFunction() {
        return distanceFunction;
    }

    public void setDistanceFunction(DistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
    
    public static interface Listener {

        void iteration(int iteration, int move);
    }

    public static interface DistanceFunction {

        double distance(double[] p1, double[] p2);
    }
    public static final DistanceFunction EUCLIDEAN_DISTANCE_FUNCTION = new DistanceFunction() {

        @Override
        public double distance(double[] p1, double[] p2) {
            double s = 0;
            for (int d = 0; d < p1.length; d++) {
                s += Math.pow(Math.abs(p1[d] - p2[d]), 2);
            }
            double d = Math.sqrt(s);
            return d;
        }
    };
    public static final DistanceFunction MANHATTAN_DISTANCE_FUNCTION = new DistanceFunction() {

        @Override
        public double distance(double[] p1, double[] p2) {
            double s = 0;
            for (int d = 0; d < p1.length; d++) {
                s += Math.abs(p1[d] - p2[d]);
            }
            return s;
        }
    };
}